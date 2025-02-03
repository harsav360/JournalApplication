package com.harsav360.journal.scheduler;

import com.harsav360.journal.entity.JournalEntry;
import com.harsav360.journal.entity.User;
import com.harsav360.journal.enums.Sentiment;
import com.harsav360.journal.model.SentimentData;
import com.harsav360.journal.repository.UserRepositoryImpl;
import com.harsav360.journal.service.EmailService;
import com.harsav360.journal.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    KafkaTemplate<String, SentimentData> kafkaTemplate;

    private final EmailService emailService;
    private final UserRepositoryImpl userRepositoryImp;
    private final SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    public UserScheduler(EmailService emailService, UserRepositoryImpl userRepositoryImp, SentimentAnalysisService sentimentAnalysisService) {
        this.emailService = emailService;
        this.userRepositoryImp = userRepositoryImp;
        this.sentimentAnalysisService = sentimentAnalysisService;
    }

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendSaMail() {
        List<User> users = userRepositoryImp.getUserForSA();
        for (User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Object> sentiments = journalEntries.stream()
                    .filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(JournalEntry::getSentiment)
                    .collect(Collectors.toList());
            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
            for (Object sentiment : sentiments) {
                if (sentiment != null)
                    sentimentCounts.put((Sentiment) sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if (mostFrequentSentiment != null) {
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days " + mostFrequentSentiment).build();
                try{
                    kafkaTemplate.send("weekly_sentiments", sentimentData.getEmail(), sentimentData);
                }catch (Exception e){
                    emailService.sendMail(sentimentData.getEmail(), "Sentiment for previous week", sentimentData.getSentiment());
                }
            }
        }
    }
}


