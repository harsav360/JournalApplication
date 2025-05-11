package com.harsav360.journal.service;

import com.harsav360.journal.entity.JournalEntry;
import com.harsav360.journal.entity.User;
import com.harsav360.journal.helper.JournalEntryHelper;
import com.harsav360.journal.model.JournalEntryDTO;
import com.harsav360.journal.repository.JournalEntryRepo;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class JournalEntryService {

    private final JournalEntryRepo journalEntryRepo;
    private final UserService userService;
    private final JournalEntryHelper journalEntryHelper;

    private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName){
        try {
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepo.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);

        } catch (Exception e){
            logger.info("Here is complete message",e);
            throw new RuntimeException("An Error Occurred while saving the entry - ", e);
        }

    }

    public void saveEntry(JournalEntry journalEntry){
        JournalEntry saved = journalEntryRepo.save(journalEntry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepo.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName){
        try {
            User user = userService.findByUserName(userName);
            boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if(removed){
                userService.saveUser(user);
                journalEntryRepo.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("An Error occurred while deleting the entry.",e);
        }
    }

    public List<JournalEntryDTO> getUserJournalEntries(User user) {
        try {
            return user.getJournalEntries().stream()
                    .map(journalEntryHelper::journalEntryMapper)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error("Something bad happened. Caused - {}", ex.getMessage());
        }
        return null;
    }


}
