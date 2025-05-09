package com.harsav360.journal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private ObjectId id;
    private String username;
    private String email;
    private String password;
    private boolean sentimentAnalysis;
    private List<String> roles;
}

