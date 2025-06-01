package com.dckap.kothai.controller.v1;

import java.util.Arrays;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dckap.kothai.payload.request.ChallengeFilterDto;
import com.dckap.kothai.payload.request.ChallengeRequestDto;
import com.dckap.kothai.payload.response.ResponseEntityDto;
import com.dckap.kothai.service.ChallengeService;
import com.dckap.kothai.type.ChallengeType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/challenges")
@Tag(name = "Challenge Controller", description = "Endpoints for challenges")
@RequiredArgsConstructor
public class ChallengeController {

    @NonNull
    private final ChallengeService challengeService;

    @Operation(summary = "Create a new challenge", description = "Create a new challenge")
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseEntityDto> createChallenge(@RequestBody ChallengeRequestDto challengeRequestDto) {
        ResponseEntityDto response = challengeService.createChallenge(challengeRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Fetch a challenge by id", description = "Fetch a challenge by id")
    @GetMapping(value = "/{challengeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseEntityDto> fetchChallengeById(@PathVariable Long challengeId) {
        ResponseEntityDto response = challengeService.fetchChallengeById(challengeId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get challenges by filter", description = "Get challenges by filter")
    @GetMapping(value = "/get-by-filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseEntityDto> getChallengesByFilter(@Valid ChallengeFilterDto challengeFilterDto) {
        ResponseEntityDto response = challengeService.getChallengesByFilter(challengeFilterDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Fetch my challenges", description = "Fetch my challenges")
    @GetMapping(value = "/fetch-my-challenges", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseEntityDto> fetchMyChallenges(@Valid ChallengeType challengeType) {
        ResponseEntityDto response = challengeService.fetchMyChallenges(challengeType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Fetch all challenge types", description = "Fetch all challenge types")
    @GetMapping(value = "/fetch-all-challenge-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseEntityDto> fetchAllChallengeTypes() {
        ResponseEntityDto response = new ResponseEntityDto(true, Arrays.asList(ChallengeType.values()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    

}
