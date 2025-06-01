package com.dckap.kothai.service.impl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;

import com.dckap.kothai.payload.request.ScheduleRequest;
import com.dckap.kothai.util.GoogleAuthorizeUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.security.GeneralSecurityException;
import java.io.IOException;

@Service
public class GoogleCalendarService {
    private static final List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/calendar");

    private static final String APPLICATION_NAME = "Interview Scheduler";
    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/credentials.json";

    public String createGoogleMeetEvent(ScheduleRequest request) {
        try {
            Calendar service = new Calendar.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    GoogleAuthorizeUtil.getCredentials(GoogleNetHttpTransport.newTrustedTransport(), CREDENTIALS_FILE_PATH)
            ).setApplicationName(APPLICATION_NAME).build();

            Event event = new Event()
                    .setSummary(request.getTitle())
                    .setLocation("Google Meet")
                    .setDescription(request.getDescription() + "\nGoogle Doc: " + request.getDocLink());

            EventDateTime start = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(request.getStartTime().toInstant().toEpochMilli()))
                    .setTimeZone("Asia/Colombo");

            EventDateTime end = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(request.getEndTime().toInstant().toEpochMilli()))
                    .setTimeZone("Asia/Colombo");

            event.setStart(start);
            event.setEnd(end);

            List<EventAttendee> attendees = new ArrayList<>();
            for (String email : request.getAttendeesEmails()) {
                attendees.add(new EventAttendee().setEmail(email));
            }
            event.setAttendees(attendees);

            event.setConferenceData(new ConferenceData()
                    .setCreateRequest(new CreateConferenceRequest()
                            .setRequestId(UUID.randomUUID().toString())
                            .setConferenceSolutionKey(new ConferenceSolutionKey().setType("hangoutsMeet"))));

            event = service.events().insert("primary", event)
                    .setConferenceDataVersion(1)
                    .execute();

            return "Meeting Created: " + event.getHtmlLink() + "\nGoogle Meet Link: "
                    + event.getConferenceData().getEntryPoints().get(0).getUri();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }
}
