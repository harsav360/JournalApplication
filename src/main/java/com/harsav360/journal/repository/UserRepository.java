package com.harsav360.journal.repository;

import com.harsav360.journal.entity.JournalEntry;
import com.harsav360.journal.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId>{

    User findByusername(String username);

}
