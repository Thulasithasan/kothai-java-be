package com.dckap.kothai.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequest {
    private String title;
    private String description;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private List<String> attendeesEmails;
    private String docLink;
}
