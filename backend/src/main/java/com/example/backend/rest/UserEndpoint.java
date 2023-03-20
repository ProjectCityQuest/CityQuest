package com.example.backend.rest;

import com.example.backend.dto.*;
import com.example.backend.entity.User;
import com.example.backend.mapper.UserLoginMapping;
import com.example.backend.mapper.UserRegisterMapping;
import com.example.backend.service.UserServiceImpl;
import com.example.backend.util.Emails;
import com.example.backend.util.Strings;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.PUT})
public class UserEndpoint {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/register")
    public ResponseEntity<Object> UserRegister(@RequestBody User user) {
        LOG.info("/register issued with parameter: " + user);

        if (UserServiceImpl.getUserByName(user.getUsername()) == null && UserServiceImpl.getUserByEmail(user.getEmail()) == null) {
            UserServiceImpl.addUser(user);
            try {
                String key = Strings.generateToken(24);
                Emails.sendMail(user.getEmail(), key);
                UserServiceImpl.addPendingEmail(key, user.getEmail());
                LOG.info("Email Verification for User '" + user.getUsername() + "' sent to '" + user.getEmail() + "'");
            } catch(MessagingException mex) {
                mex.printStackTrace();
            }
            return new ResponseEntity<Object>(new UserRegisterMapping(new UserRegisterDto(user), false), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Object>(new ErrorDto("Der Benutzername oder die Email existiert bereits"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> UserLogin(@RequestBody User user, HttpServletResponse cookieResponse) {
        LOG.info("/login issued with parameter: " + user);
        User currentUser = UserServiceImpl.getUserByName(user.getUsername());

        if (currentUser == null) {
            return new ResponseEntity<Object>(new ErrorDto("Falscher Benutzername oder falsches Passwort"), HttpStatus.BAD_REQUEST);
        } else if (!currentUser.getPassword().equals(user.getPassword())) {
            return new ResponseEntity<Object>(new ErrorDto("Falscher Benutzername oder falsches Passwort"), HttpStatus.BAD_REQUEST);
        } else if (!currentUser.isEmailIsVerified()) {
            return new ResponseEntity<Object>(new ErrorDto("Die E-Mail-Adresse wurde noch nicht verifiziert"), HttpStatus.UNAUTHORIZED);
        } else {
            currentUser.setToken(Strings.generateToken());
            ResponseEntity<Object> response = new ResponseEntity<Object>(new AuthorizationDto(currentUser.getToken()), HttpStatus.OK);
            return response;
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<Object> UserVerify(@RequestBody UserVerifyDto request) {
        LOG.info("/verify issued with parameter: " + request);
        Date date = UserServiceImpl.getPendingEmailVerificationsDate().getOrDefault(request.getKey(), null);
        String email = UserServiceImpl.getPendingEmailVerificationsEmail().getOrDefault(request.getKey(), null);
        if (date == null || email == null) {
            return new ResponseEntity<>(new ErrorDto("Der Link ist ungültig"), HttpStatus.UNAUTHORIZED);
        } else if(!email.equals(request.getEmail())) {
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

    @GetMapping("/users")
    public ResponseEntity<Object> checkUserToken(@CookieValue(value = "X-API-KEY") String token, HttpServletResponse cookieResponse) {
        LOG.info("/users issued with parameter: " + token);
        User user = UserServiceImpl.getUserByToken(token);
        if (user == null) {
            return new ResponseEntity<Object>(new ErrorDto("Es gibt keinen benutzer mit diesem Token"), HttpStatus.UNAUTHORIZED);
        } else {
            cookieResponse.addCookie(new Cookie("X-API-KEY", token));
            return new ResponseEntity<Object>(new UserLoginDto(user), HttpStatus.OK);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> userLogout(@CookieValue(value = "X-API-KEY") String token) {
        LOG.info("/logout issued with parameter: " + token);
        User user = UserServiceImpl.getUserByToken(token);
        if (user == null) {
            return new ResponseEntity<Object>(new ErrorDto("Es gibt keinen Benutzer mit diesem Token"), HttpStatus.BAD_REQUEST);
        } else {
            user.setToken(null);
            return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
        }
    }
}
