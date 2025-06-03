package com.dckap.kothai.scheduler;

import com.dckap.kothai.model.Challenge;
import com.dckap.kothai.service.ChallengeService;
import com.dckap.kothai.type.ChallengeType;
import com.dckap.kothai.repository.ChallengeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Collections;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyChallengeScheduler {

    private final ChallengeRepository challengeRepository;
    private ChallengeService challengeService;
    private List<String> paragraphs;

    public List<String> getRandomParagraphs() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            var resource = new ClassPathResource("paragraphs.json");
            paragraphs = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {
            });
            Collections.shuffle(paragraphs);
            return paragraphs.subList(0, Math.min(10, paragraphs.size()));
        } catch (IOException e) {
            log.error("Error generating random paragraphs", e);
            return new ArrayList<>();
        }
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Colombo") // set your correct time zone here
//    @Scheduled(fixedRate = 1000 * 60 * 60 * 2)
    public void create() {
        List<Map<String, Integer>> pointTimeAccuracySpeed = new ArrayList<>();

        pointTimeAccuracySpeed.add(Map.of("time", 70, "accuracy", 85, "speed", 35, "xp", 20));
        pointTimeAccuracySpeed.add(Map.of("time", 71, "accuracy", 86, "speed", 36, "xp", 19));
        pointTimeAccuracySpeed.add(Map.of("time", 72, "accuracy", 87, "speed", 37, "xp", 18));
        pointTimeAccuracySpeed.add(Map.of("time", 73, "accuracy", 88, "speed", 38, "xp", 17));
        pointTimeAccuracySpeed.add(Map.of("time", 74, "accuracy", 89, "speed", 39, "xp", 16));
        pointTimeAccuracySpeed.add(Map.of("time", 75, "accuracy", 90, "speed", 40, "xp", 15));
        pointTimeAccuracySpeed.add(Map.of("time", 76, "accuracy", 91, "speed", 41, "xp", 14));
        pointTimeAccuracySpeed.add(Map.of("time", 77, "accuracy", 92, "speed", 42, "xp", 13));
        pointTimeAccuracySpeed.add(Map.of("time", 78, "accuracy", 93, "speed", 43, "xp", 12));
        pointTimeAccuracySpeed.add(Map.of("time", 79, "accuracy", 94, "speed", 44, "xp", 11));
        pointTimeAccuracySpeed.add(Map.of("time", 80, "accuracy", 95, "speed", 45, "xp", 10));

        Collections.shuffle(pointTimeAccuracySpeed);
        Map<String, Integer> subbedList = pointTimeAccuracySpeed.subList(0, Math.min(1, pointTimeAccuracySpeed.size())).get(0);
        List<String> randomParagraphs = getRandomParagraphs();
        for (String paragraph : randomParagraphs) {
            Challenge build = Challenge.builder()
                    .title("Daily Challenge")
                    .description("Complete today''s challenge to maintain your streak and improve your skills!")
                    .type(ChallengeType.DAILY)
                    .level(1)
                    .content(paragraph)
                    .timeLimit(subbedList.get("time"))
                    .winXp(subbedList.get("xp"))
                    .loseXp(0)
                    .accuracy(subbedList.get("accuracy"))
                    .speed(subbedList.get("speed"))
                    .build();
            challengeRepository.save(build);
        }
        log.info("Random Paragraphs: {}", randomParagraphs);
    }
}
