package com.hydev.security1.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.hydev.security1.config.auth.PrincipalDetails;
import com.hydev.security1.model.User;
import com.hydev.security1.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	// 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
	
	// 함수종료시 @AuthenticaionPrincipal 어노테이션이 만들어짐
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// userRequest에 코드를 통해 엑세스토큰 받은거랑 사용자 프로필정보 다 받음
		System.out.println("getAttributes : "+super.loadUser(userRequest).getAttributes());
		
		System.out.println("getClientRegistration : "+userRequest.getClientRegistration()); // 어떤 oauth로 로그인 했는지 알수있음
		
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		// 구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 -> code리턴(oauth2클라이언트 라이브러리가 받음) -> 코드를 통해 AcceessToken 요청해서 받음
		// 여기까지가 userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원 프로필을 받음
		System.out.println("getAccessToken : "+userRequest.getAccessToken().getTokenValue());
		
		String provider = userRequest.getClientRegistration().getClientId(); // google
		String providerId = oauth2User.getAttribute("sub");
		String username = provider+"_"+providerId;
		String email = oauth2User.getAttribute("email");
		String password = bCryptPasswordEncoder.encode("임시비번");
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		}
		
		return new PrincipalDetails(userEntity,oauth2User.getAttributes());
	}
}
