package com.harsav360.journal.controller;

import com.harsav360.journal.entity.User;
import com.harsav360.journal.model.AuthRequest;
import com.harsav360.journal.model.AuthResponse;
import com.harsav360.journal.service.JwtService;
import com.harsav360.journal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        try {
            return new ResponseEntity<>("Server is up and Running",HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtService.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }



    @PostMapping("/signup")
    public void createUser(@RequestBody User user){
        userService.saveNewEntry(user);
    }
}
