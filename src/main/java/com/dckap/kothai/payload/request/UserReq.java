package com.dckap.kothai.payload.request;

import lombok.Data;

@Data
public class UserReq {
    private String email;
    private String phoneNumber;
    private String password;
}
