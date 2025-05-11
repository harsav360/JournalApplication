package com.harsav360.journal.model;

import com.harsav360.journal.enums.Sentiment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntryDTO {

    private ObjectId id;
    private String title;
    private String content;
    private LocalDateTime date;
    private Sentiment sentiment;
}