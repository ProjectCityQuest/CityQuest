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
                LOG.info("Email Verification for User '" + user.getUsername() + "' sent to '" + user.getEmail() + "'");
            } catch (MessagingException mex) {
                mex.printStackTrace();
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
                UserServiceImpl.getUserByEmail(request.getEmail()).setEmailIsVerified(true);
                UserServiceImpl.removePendingEmailVerification(request.getKey());
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
        LOG.info("GET /users issued with parameter: " + token);
        User user = UserServiceImpl.getUserByToken(token);
        if (user == null) {
            return new ResponseEntity<>(new ErrorDto("Es gibt keinen benutzer mit diesem Token"), HttpStatus.UNAUTHORIZED);
        } else {
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
            return new ResponseEntity<>(new ErrorDto("Es gibt keinen Benutzer mit diesem Token"), HttpStatus.BAD_REQUEST);
        } else {
            user.setToken(null);
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
            return new ResponseEntity<>(new ErrorDto("Es gibt keinen Benutzer mit diesem Token"), HttpStatus.BAD_REQUEST);
        } else {
            UserServiceImpl.removeUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping("/forgotpassword")
    public ResponseEntity<Object> resetPassword(@RequestBody User request) {
        LOG.info("GET /forgotpassword issued with parameters: " + request.getEmail());

        User user = UserServiceImpl.getUserByEmail(request.getEmail());

        if (user == null) {
            return new ResponseEntity<>(new ErrorDto("Es gibt keinen Benutzer mit der angegebenen Email-Adresse"), HttpStatus.BAD_REQUEST);
        }

        try {
            String key = Strings.generateToken(24);
            Emails.sendPasswordResetMail(user.getEmail(), key);
            UserServiceImpl.addPendingPasswordReset(key, user.getEmail());
            LOG.info("Password reset email for User '" + user.getUsername() + "' sent to '" + user.getEmail() + "'");
        } catch (MessagingException mex) {
            mex.printStackTrace();
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
                UserServiceImpl.changePassword(UserServiceImpl.getUserByEmail(request.getEmail()).getId(), request.getNew_password());
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
            return new ResponseEntity<>(new ErrorDto("Der Token des Benutzers ist ungueltig!"), HttpStatus.UNAUTHORIZED);
        }
        if (!user.getPassword().equals(request.getOldPassword())) {
            return new ResponseEntity<>(new ErrorDto("Das angegebene Passwort ist falsch"), HttpStatus.UNAUTHORIZED);
        } else {
            UserServiceImpl.changePassword(user.getId(), request.getNewPassword());
            LOG.info("Password of User:'" + user.getUsername() + "' with Id: '" + user.getId() + "' has been changed to: '" + user.getPassword());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}
