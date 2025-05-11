package com.harsav360.journal.helper;

import com.harsav360.journal.entity.JournalEntry;
import com.harsav360.journal.model.JournalEntryDTO;
import org.springframework.stereotype.Component;

@Component
public class JournalEntryHelper {

    // Map Journal Entries to Journal DTO
    public JournalEntryDTO journalEntryMapper(JournalEntry journalEntry){
        return JournalEntryDTO.builder()
                .id(journalEntry.getId())
                .title(journalEntry.getTitle())
                .content(journalEntry.getContent())
                .sentiment(journalEntry.getSentiment())
                .build();
    }
}
