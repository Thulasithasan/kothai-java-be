package com.dckap.kothai.controller.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.dckap.kothai.payload.response.ResponseEntityDto;
import com.dckap.kothai.payload.request.UserChallengeRequestDto;
import com.dckap.kothai.service.UserChallengeService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/user-challenges")
@Tag(name = "User Challenge Controller", description = "Endpoints for user challenges")
@RequiredArgsConstructor
public class UserChallengeController {

    @NonNull
    private final UserChallengeService userChallengeService;
    
    @Operation(summary = "Create a new user challenge", description = "Create a new user challenge")
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseEntityDto> createUserChallenge(@RequestBody UserChallengeRequestDto userChallengeRequestDto) {
        ResponseEntityDto response = userChallengeService.createUserChallenge(userChallengeRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get all my user challenges", description = "Get all my user challenges")
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseEntityDto> getUserChallenges() {
        ResponseEntityDto response = userChallengeService.getUserChallenges();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
