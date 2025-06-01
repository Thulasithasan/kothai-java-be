package com.dckap.kothai.controller.v1;

import com.dckap.kothai.payload.request.ScheduleRequest;
import com.dckap.kothai.service.impl.GoogleCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendar")
public class GoogleCalendarController {

    @Autowired
    private GoogleCalendarService calendarService;

    @PostMapping("/schedule")
    public String scheduleMeeting(@RequestBody ScheduleRequest request) {
        return calendarService.createGoogleMeetEvent(request);
    }
}
