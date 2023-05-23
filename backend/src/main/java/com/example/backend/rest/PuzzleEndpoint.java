/**
 * Rest API endpoint for all user-related requests
 */

package com.example.backend.rest;

import com.example.backend.dto.ErrorDto;
import com.example.backend.dto.GetPuzzleDto;
import com.example.backend.dto.GetPuzzleRequestDto;
import com.example.backend.entity.PuzzlePiece;
import com.example.backend.entity.User;
import com.example.backend.service.PuzzleService;
import com.example.backend.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class PuzzleEndpoint {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/getpuzzle")
    public ResponseEntity<Object> getPuzzle(@RequestHeader(value = "sessionKey") String token, @RequestBody GetPuzzleRequestDto request) {
        LOG.info("GET /getpuzzle issued with parameter " + request);

        User user = UserServiceImpl.getUserByToken(token);
        if (user == null) {
            return new ResponseEntity<>(new ErrorDto("Der Token des Benutzers ist ungültig!"), HttpStatus.UNAUTHORIZED);
        }
        List<PuzzlePiece> puzzlePieces = PuzzleService.getPuzzle(user.getId(),request.getPageIndex());
        return new ResponseEntity<>(new GetPuzzleDto(puzzlePieces, request.getPageIndex()), HttpStatus.OK);
    }
}
