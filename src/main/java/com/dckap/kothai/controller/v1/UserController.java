package com.dckap.kothai.controller.v1;

import com.dckap.kothai.payload.response.ResponseEntityDto;
import com.dckap.kothai.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/users")
@Tag(name = "User Controller", description = "Endpoints for users")
@RequiredArgsConstructor
public class UserController {

    @NonNull
    private final UserService userService;

    @Operation(summary = "Get current user details", description = "Get current user details")
    @GetMapping(value = "/current-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseEntityDto> getCurrentUserDetails() {
        ResponseEntityDto response = userService.getCurrentUserDetails();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
