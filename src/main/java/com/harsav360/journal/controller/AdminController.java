package com.harsav360.journal.controller;

import com.harsav360.journal.cache.AppCache;
import com.harsav360.journal.entity.User;
import com.harsav360.journal.model.UserDTO;
import com.harsav360.journal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    private final AppCache appCache;

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> all = userService.getAll();
        if (all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public void createUser(@RequestBody UserDTO user){
        userService.saveAdmin(user);
    }

    @GetMapping("clear-app-cache")
    public void clearAppCache(){
        appCache.init();
    }

}
