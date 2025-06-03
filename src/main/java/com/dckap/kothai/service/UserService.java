package com.dckap.kothai.service;

import com.dckap.kothai.model.User;
import com.dckap.kothai.payload.request.UserReq;
import com.dckap.kothai.payload.response.ResponseEntityDto;

public interface UserService {

    User getCurrentUser();

    ResponseEntityDto getCurrentUserDetails();
}
