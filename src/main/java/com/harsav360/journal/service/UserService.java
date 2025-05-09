package com.harsav360.journal.service;


import com.harsav360.journal.entity.User;
import com.harsav360.journal.model.UserDTO;
import com.harsav360.journal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepo;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveNewEntry(User user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(List.of("USER"));
            userRepo.save(user);
        } catch(Exception e) {
            log.error("Error occurred for {} : ",user.getUsername(),e);
        }

    }

    public void saveAdmin(UserDTO user){
        User userEntity = buildUserEntity(user);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRoles(List.of("USER","ADMIN"));
        userRepo.save(userEntity);
    }

    public void saveUser(User user){
        userRepo.save(user);
    }

    public List<User> getAll(){
        return userRepo.findAll();
    }

    public Optional<User> findById(ObjectId id){
        return userRepo.findById(id);
    }

    public void deleteById(ObjectId id){
        userRepo.deleteById(id);
    }

    public User findByUserName(String userName){
        return userRepo.findByusername(userName);
    }

    private User buildUserEntity(UserDTO userDto){
        return User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .sentimentAnalysis(userDto.isSentimentAnalysis())
                .password(userDto.getPassword()).build();

    }


}
