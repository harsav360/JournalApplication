package com.harsav360.journal.scheduler;

import com.harsav360.journal.entity.JournalEntry;
import com.harsav360.journal.entity.User;
import com.harsav360.journal.repository.UserRepositoryImpl;
import com.harsav360.journal.service.EmailService;
import com.harsav360.journal.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserScheduler {


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
        List<User> users =  userRepositoryImp.getUserForSA();
        for(User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
           List<String> filteredEntries = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getContent()).collect(Collectors.toList());
           String entry = String.join(" ",filteredEntries);
           String sentiment = sentimentAnalysisService.getSentiment(entry);
           emailService.sendMail(user.getEmail(),"Sentiment for last 7 days",sentiment);

        }
    }
}


