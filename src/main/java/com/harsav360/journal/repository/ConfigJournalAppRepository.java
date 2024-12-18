package com.harsav360.journal.repository;

import com.harsav360.journal.entity.ConfigJournalAppEntity;
import com.harsav360.journal.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalAppEntity, ObjectId>{



}
