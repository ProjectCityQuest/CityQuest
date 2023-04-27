/**
 * Rest API endpoint for all user-related requests
 */

package com.example.backend.rest;

import com.example.backend.dto.*;
import com.example.backend.entity.User;
import com.example.backend.mapper.UserRegisterMapping;
import com.example.backend.service.UserServiceImpl;
import com.example.backend.util.Emails;
import com.example.backend.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.PUT})
public class UserEndpoint {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());


    /**
     *
     * @param user receives a user object formatted as json
     * @return the http code and response body according to the openapi definition
     */

    @PostMapping("/register")
    public ResponseEntity<Object> UserRegister(@RequestBody User user) {
        LOG.info("POST /register issued with parameter: " + user);

        if (UserServiceImpl.getUserByName(user.getUsername()) == null && UserServiceImpl.getUserByEmail(user.getEmail()) == null) {
            UserServiceImpl.addUser(user);
            try {
                String key = Strings.generateToken(24);
                Emails.sendVerificationMail(user.getEmail(), key);
                UserServiceImpl.addPendingEmail(key, user.getEmail());
            } catch (MessagingException mex) {
                LOG.error("javax.mail.MessagingException occured while trying to send email verification to '" + user.getEmail() + "'");
            }
            return new ResponseEntity<>(new UserRegisterMapping(new UserRegisterDto(user), false), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ErrorDto("Der Benutzername oder die Email existiert bereits"), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     *
     * @param user receives a user object formatted as json
     * @return the http code and response body according to the openapi definition
     */

    @PostMapping("/login")
    public ResponseEntity<Object> UserLogin(@RequestBody User user) {
        LOG.info("POST /login issued with parameter: " + user);
        User currentUser = UserServiceImpl.getUserByName(user.getUsername());

        if (currentUser == null) {
            return new ResponseEntity<>(new ErrorDto("Falscher Benutzername oder falsches Passwort"), HttpStatus.BAD_REQUEST);
        } else if (!currentUser.getPassword().equals(user.getPassword())) {
            return new ResponseEntity<>(new ErrorDto("Falscher Benutzername oder falsches Passwort"), HttpStatus.BAD_REQUEST);
        } else if (!currentUser.isEmailIsVerified()) {
            return new ResponseEntity<>(new ErrorDto("Die E-Mail-Adresse wurde noch nicht verifiziert"), HttpStatus.UNAUTHORIZED);
        } else {
            currentUser.setToken(Strings.generateToken());
            LOG.info("Token for User: '" + user.getUsername() + "' has been set to: '" + currentUser.getToken() +"'");
            return new ResponseEntity<>(new AuthorizationDto(currentUser.getToken()), HttpStatus.OK);
        }
    }

    /**
     *
     * @param request contains credentials to verify the email of a user
     * @return the http code and response body according to the openapi definition
     */

    @PostMapping("/verify")
    public ResponseEntity<Object> UserVerify(@RequestBody UserVerifyDto request) {
        LOG.info("POST /verify issued with parameter: " + request);
        Date date = UserServiceImpl.getPendingEmailVerificationsDate().getOrDefault(request.getKey(), null);
        String email = UserServiceImpl.getPendingEmailVerificationsEmail().getOrDefault(request.getKey(), null);
        if (date == null || email == null) {
            return new ResponseEntity<>(new ErrorDto("Der Link ist ungültig"), HttpStatus.UNAUTHORIZED);
        } else if (!email.equals(request.getEmail())) {
            return new ResponseEntity<>(new ErrorDto("Der Link ist ungültig"), HttpStatus.UNAUTHORIZED);
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.MINUTE, -10);
            Date validDate = c.getTime();
            if (validDate.after(date)) {
                return new ResponseEntity<>(new ErrorDto("Der Link ist abgelaufen"), HttpStatus.UNAUTHORIZED);
            } else {
                User user = UserServiceImpl.getUserByEmail(request.getEmail());
                user.setEmailIsVerified(true);
                UserServiceImpl.removePendingEmailVerification(request.getKey());
                LOG.info("Email of User: '" + user.getUsername() + "' has been verified!");
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }
    }

    /**
     *
     * @param token is the sessionKey of a user and checks whether this key is valid
     * @return the http code and response body according to the openapi definition
     */

    @GetMapping("/getusers")
    public ResponseEntity<Object> checkUserToken(@RequestHeader(value = "sessionKey") String token) {
        LOG.info("GET /getusers issued with parameter: " + token);
        User user = UserServiceImpl.getUserByToken(token);
        if (user == null) {
            LOG.info("The given user token: '" + token + "' is not valid!");
            return new ResponseEntity<>(new ErrorDto("Es gibt keinen benutzer mit diesem Token"), HttpStatus.UNAUTHORIZED);
        } else {
            LOG.info("The given user token: '" + token + "' is valid and belongs to the User: '" + user.getUsername() + "'");
            return new ResponseEntity<>(new UserLoginDto(user), HttpStatus.OK);
        }
    }

    /**
     *
     * @param token is the sessionKey of a user to disable the key and logout the user
     * @return the http code and response body according to the openapi definition
     */

    @PostMapping("/logout")
    public ResponseEntity<Object> userLogout(@RequestHeader(value = "sessionKey") String token) {
        LOG.info("POST /logout issued with parameter: " + token);
        User user = UserServiceImpl.getUserByToken(token);
        if (user == null) {
            LOG.info("The given user token: '" + token + "' is not valid!");
            return new ResponseEntity<>(new ErrorDto("Es gibt keinen Benutzer mit diesem Token"), HttpStatus.BAD_REQUEST);
        } else {
            user.setToken(null);
            LOG.info("User: '" + user.getUsername() + "' has been logged out!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     *
     * @param token is the sessionKey of a user to delete the account of this user
     * @return the http code and response body according to the openapi definition
     */

    @DeleteMapping("/deleteusers")
    public ResponseEntity<Object> userDelete(@RequestHeader(value = "sessionKey") String token) {
        LOG.info("DELETE /users issued with parameter: " + token);
        User user = UserServiceImpl.getUserByToken(token);
        if (user == null) {
            LOG.info("The given user token: '" + token + "' is not valid!");
            return new ResponseEntity<>(new ErrorDto("Es gibt keinen Benutzer mit diesem Token"), HttpStatus.BAD_REQUEST);
        } else {
            UserServiceImpl.removeUser(user);
            LOG.info("The User: '" + user.getUsername() + "' has been deleted!");
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping("/forgotpassword/{email}")
    public ResponseEntity<Object> resetPassword(@PathVariable String email) {
        LOG.info("GET /forgotpassword issued with parameters: " + email);

        User user = UserServiceImpl.getUserByEmail(email);

        if (user == null) {
            LOG.info("The given email address: '" + email + "' is does not exist!");
            return new ResponseEntity<>(new ErrorDto("Es gibt keinen Benutzer mit der angegebenen Email-Adresse"), HttpStatus.BAD_REQUEST);
        }

        try {
            String key = Strings.generateToken(24);
            Emails.sendPasswordResetMail(user.getEmail(), key);
            UserServiceImpl.addPendingPasswordReset(key, user.getEmail());
        } catch (MessagingException mex) {
            LOG.error("javax.mail.MessagingException occured while trying to send a password reset email to '" + user.getEmail() + "'");
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     *
     * @param request contains the request contents according to the openapi definition
     * @see ChangePasswordDto
     * @return the http code and response body according to the openapi definition
     */

    @PatchMapping("/changepassword")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDto request) {
        LOG.info("PATCH /changepassword issued with parameter: " + request);

        Date date = UserServiceImpl.getPendingPasswordResetDate().getOrDefault(request.getKey(), null);
        String email = UserServiceImpl.getPendingPasswordResetEmail().getOrDefault(request.getKey(), null);
        if (date == null || email == null) {
            return new ResponseEntity<>(new ErrorDto("Der Link ist ungültig"), HttpStatus.UNAUTHORIZED);
        } else if (!email.equals(request.getEmail())) {
            return new ResponseEntity<>(new ErrorDto("Der Link ist ungültig"), HttpStatus.UNAUTHORIZED);
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.MINUTE, -10);
            Date validDate = c.getTime();
            if (validDate.after(date)) {
                return new ResponseEntity<>(new ErrorDto("Der Link ist abgelaufen"), HttpStatus.UNAUTHORIZED);
            } else {
                User user = UserServiceImpl.getUserByEmail(request.getEmail());
                UserServiceImpl.changePassword(user.getId(), request.getNew_password());
                UserServiceImpl.removePendingPasswordReset(request.getKey());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
    }

    @PatchMapping("/changepasswordauth")
    public ResponseEntity<Object> changePasswordAuth(@RequestBody ChangePasswordAuthDto request, @RequestHeader(value = "sessionKey") String token) {
        LOG.info("PATCH /changepasswordauth issued with parameter: " + request);

        User user = UserServiceImpl.getUserByToken(token);
        if (user == null) {
            return new ResponseEntity<>(new ErrorDto("Der Token des Benutzers ist ungültig!"), HttpStatus.UNAUTHORIZED);
        }
        if (!user.getPassword().equals(request.getOldPassword())) {
            return new ResponseEntity<>(new ErrorDto("Das angegebene Passwort ist falsch"), HttpStatus.UNAUTHORIZED);
        } else {
            UserServiceImpl.changePassword(user.getId(), request.getNewPassword());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @PostMapping("/verifyforgotpasswordkey")
    public ResponseEntity<Object> verifyForgotPasswordKey(@RequestBody VerifyForgotPasswordKeyDto request) {
        LOG.info("POST /verifyForgotPasswordKey issued with parameter: " + request);

        Date date = UserServiceImpl.getPendingPasswordResetDate().getOrDefault(request.getKey(), null);
        String email = UserServiceImpl.getPendingPasswordResetEmail().getOrDefault(request.getKey(), null);
        if (date == null || email == null) {
            return new ResponseEntity<>(new ErrorDto("Der Link ist ungültig"), HttpStatus.UNAUTHORIZED);
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.MINUTE, -10);
            Date validDate = c.getTime();
            if (validDate.after(date)) {
                return new ResponseEntity<>(new ErrorDto("Der Link ist abgelaufen"), HttpStatus.UNAUTHORIZED);
            } else {
                LOG.info("Der Key '" + request.getKey() + "' ist gültig");
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
    }

    @PostMapping("/changeusername")
    public ResponseEntity<Object> changeUsername(@RequestBody ChangeUsernameDto request, @RequestHeader(value = "sessionKey") String token) {
        LOG.info("POST /changeusername issued with parameter: " + request);

        User user = UserServiceImpl.getUserByToken(token);
        if (user == null) {
            return new ResponseEntity<>(new ErrorDto("Der Token des Benutzers ist ungültig!"), HttpStatus.UNAUTHORIZED);
        }
        UserServiceImpl.changeUsername(user, request.getUsername());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
