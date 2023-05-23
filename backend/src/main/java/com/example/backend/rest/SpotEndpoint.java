/**
 * Rest API endpoint for all user-related requests
 */

package com.example.backend.rest;

import com.example.backend.dto.ErrorDto;
import com.example.backend.entity.Spot;
import com.example.backend.entity.User;
import com.example.backend.service.SpotService;
import com.example.backend.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class SpotEndpoint {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/getspots")
    public ResponseEntity<Object> getSpots(@RequestHeader(value = "sessionKey") String token) {
        LOG.info("GET /getspots issued with parameter " + token);

        User user = UserServiceImpl.getUserByToken(token);
        if (user == null) {
            return new ResponseEntity<>(new ErrorDto("Der Token des Benutzers ist ungültig!"), HttpStatus.UNAUTHORIZED);
        }
        List<Spot> spots = SpotService.getSpots(user.getId());
        return new ResponseEntity<>(spots, HttpStatus.OK);
    }
}
