package com.dckap.kothai.security;

import com.dckap.kothai.constant.CommonMessageConstant;
import com.dckap.kothai.exception.ModuleException;
import com.dckap.kothai.model.User;
import com.dckap.kothai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	private final AuthorityService authorityService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository.findByEmail(email)
			.map(this::createUserDetails)
			.orElseThrow(() -> new ModuleException(CommonMessageConstant.COMMON_ERROR_USER_NOT_FOUND));
	}

	private UserDetails createUserDetails(User user) {
		return DckapUserDetails.builder()
			.username(user.getEmail())
			.password(user.getPassword())
			.enabled(user.getIsActive())
			.authorities(authorityService.getAuthorities(user))
			.build();
	}

}
