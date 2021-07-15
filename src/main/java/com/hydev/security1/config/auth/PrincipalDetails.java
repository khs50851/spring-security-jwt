package com.hydev.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hydev.security1.model.User;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행
// 로그인이 완료되면 시큐리티 session을 만들어줘서 넣어줌 (Security ContextHolder에 저장)
// 시큐리티가 가지고 있는 세션에 들어갈 수 있는 오브젝트는 정해져있음 (Authentication 타입 객체)
// 이 Authentication 타입의 객체 안에 User 정보가 있어야함.
// User오브젝트 타입 => UserDetails 타입이어야함
public class PrincipalDetails implements UserDetails{
	
	private User user; // 콤포지션
	
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	// 해당 유저의 권한 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		// 우리 사이트에서 1년동안 회원이 로그인을 안하면 휴면 계정으로 하기로 했다면
		// 유저 모델에 private Timestamp loginDate라는게 있어야 로그인할때 이 날짜를 넣고 경과하면 잠그면 됨
		// user.getLoginDate(); 이 날짜를 들고와서 현재시간 - 로그인시간 => 1년을 초과하면 return을 false로
		
		
		return true;
	}
	
}
