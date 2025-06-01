package com.dckap.kothai.security;

import com.dckap.kothai.model.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface AuthorityService {

	List<GrantedAuthority> getAuthorities(User user);

}
