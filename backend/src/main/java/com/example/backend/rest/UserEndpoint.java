package com.example.backend.rest;

import com.example.backend.dto.ErrorDto;
import com.example.backend.dto.UserLoginDto;
import com.example.backend.dto.UserRegisterDto;
import com.example.backend.entity.User;
import com.example.backend.mapper.UserLoginMapping;
import com.example.backend.mapper.UserRegisterMapping;
import com.example.backend.service.UserServiceImpl;
import com.example.backend.util.Strings;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.PUT})
public class UserEndpoint {
    @PostMapping("/register")
    public ResponseEntity<Object> UserRegister(@RequestBody User user) {
        if (UserServiceImpl.getUserByName(user.getUsername()) == null && UserServiceImpl.getUserByEmail(user.getEmail()) == null) {
            UserServiceImpl.addUser(user);
            return new ResponseEntity<Object>(new UserRegisterMapping(new UserRegisterDto(user), false), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Object>(new ErrorDto("Der Benutzername oder die Email existiert bereits"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> UserLogin(@RequestBody User user, HttpServletResponse cookieResponse) {
        User currentUser = UserServiceImpl.getUserByName(user.getUsername());

        if (currentUser == null) {
            return new ResponseEntity<Object>(new ErrorDto("Falscher Benutzername oder falsches Passwort"), HttpStatus.BAD_REQUEST);
        } else if (!currentUser.getPassword().equals(user.getPassword())) {
            return new ResponseEntity<Object>(new ErrorDto("Falscher Benutzername oder falsches Passwort"), HttpStatus.BAD_REQUEST);
        } else if (!currentUser.isEmailIsVerified()) {
            return new ResponseEntity<Object>(new ErrorDto("Die E-Mail-Adresse wurde noch nicht verifiziert"), HttpStatus.UNAUTHORIZED);
        } else {
            currentUser.setToken(Strings.generateToken());
            Cookie cookie = new Cookie("X-API-KEY", currentUser.getToken());
            cookie.setMaxAge(7 * 24 * 60 * 60);
            cookieResponse.addCookie(cookie);
            ResponseEntity<Object> response = new ResponseEntity<Object>(new UserLoginMapping(new UserLoginDto(currentUser)), HttpStatus.OK);
            return response;
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Object> checkUserToken(@CookieValue(value = "X-API-KEY") String token, HttpServletResponse cookieResponse) {
        User user = UserServiceImpl.getUserByToken(token);
        if (user == null) {
            return new ResponseEntity<Object>(new ErrorDto("Es gibt keinen benutzer mit diesem Token"), HttpStatus.UNAUTHORIZED);
        } else {
            cookieResponse.addCookie(new Cookie("X-API-KEY", token));
            return new ResponseEntity<Object>(new UserLoginDto(user), HttpStatus.OK);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> userLogout(@RequestHeader("X-API-KEY") String token) {
        User user = UserServiceImpl.getUserByToken(token);
        if (user == null) {
            return new ResponseEntity<Object>(new ErrorDto("Es gibt keinen Benutzer mit diesem Token"), HttpStatus.BAD_REQUEST);
        } else {
            user.setToken(null);
            return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
        }
    }
}
