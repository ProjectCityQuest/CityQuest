/**
 * Rest API endpoint for all user-related requests
 */

package com.example.backend.rest;

import com.example.backend.dto.*;
import com.example.backend.entity.User;
import com.example.backend.service.RatingsService;
import com.example.backend.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class RatingEndpoint {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());


    /**
     *
     * @param ratings receives a user object formatted as json
     * @return the http code and response body according to the openapi definition
     */

    @PostMapping("/submitrating")
    public ResponseEntity<Object> submitRating(@RequestBody SubmitRatingDto ratings, @RequestHeader(value = "sessionKey") String token) {
        LOG.info("POST /submitrating issued with parameter: " + ratings);

        User user = UserServiceImpl.getUserByToken(token);

        if (user == null) {
            return new ResponseEntity<>(new ErrorDto("Der Token ist ungueltig"), HttpStatus.UNAUTHORIZED);
        } else {
            RatingsService.submitRatings(new int[]{ratings.getDesign(), ratings.getNavigation(), ratings.getPuzzle(), ratings.getSammelbuch()});
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}
