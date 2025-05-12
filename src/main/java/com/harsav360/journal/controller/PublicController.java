package com.harsav360.journal.controller;

import com.harsav360.journal.entity.User;
import com.harsav360.journal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final UserService userService;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        try {
            return new ResponseEntity<>("Server is up and Running",HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/signup")
    public void createUser(@RequestBody User user){
        userService.saveNewEntry(user);
    }
}
