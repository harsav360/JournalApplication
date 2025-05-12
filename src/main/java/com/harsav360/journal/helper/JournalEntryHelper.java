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

    public JournalEntry journalDtoToEntryMapper(JournalEntryDTO journalEntryDTO){
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setDate(journalEntryDTO.getDate());
        journalEntry.setTitle(journalEntryDTO.getTitle());
        journalEntry.setContent(journalEntryDTO.getContent());
        journalEntry.setSentiment(journalEntryDTO.getSentiment());
        return journalEntry;
    }
}
