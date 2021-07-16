package com.hydev.security1.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hydev.security1.model.User;
import com.hydev.security1.repository.UserRepository;

// 시큐리티 설정에서 loginProcessingUrl("/login")을 걸어놨기 때문에
// /login요청이 오면 자동으로 PrincipalDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행

@Service
public class PrincipalDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	// 함수종료시 @AuthenticaionPrincipal 어노테이션이 만들어짐
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("username : "+username);
		User userEntity = userRepository.findByUsername(username); // Jpa 네임 함수
		if(userEntity != null) {
			return new PrincipalDetails(userEntity); // 이거 리턴은 session(Authentication(내부 UserDetails)) 이렇게 들어가게됨
		}
		return null;
	}
}
