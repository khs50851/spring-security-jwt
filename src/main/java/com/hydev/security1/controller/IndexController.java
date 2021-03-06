package com.hydev.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hydev.security1.config.auth.PrincipalDetails;
import com.hydev.security1.model.User;
import com.hydev.security1.repository.UserRepository;

@Controller
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(Authentication authentication,@AuthenticationPrincipal PrincipalDetails userDetails) { // 의존성 주입
		System.out.println("/test/login =======================");
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("authentication : "+principalDetails.getUser());
		
		System.out.println("userDetails : "+principalDetails.getUser());
		return "세션정보확인하기";
	}
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testLogin(Authentication authentication,@AuthenticationPrincipal OAuth2User oauth) { // 의존성 주입
		System.out.println("/test/oauth/login =======================");
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("authentication : "+oauth2User.getAttributes());
		System.out.println("oauth2User : "+oauth.getAttributes());
		
		return "세션정보확인하기";
	}
	
	// localhost:8090/
	// localhost:8090
	@GetMapping({"","/"})
	public String index() {
		// 머스테치 기본 폴더 src/main/resources/
		// 뷰리졸버 설정 : templates  (prefix), .mustache( suffix) 생략가능 (pom.xml에 의존성 등록 했기 때문)
		return "index";
	}
	
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails : "+principalDetails.getUser());
		
		System.out.println("principalDetails : "+principalDetails.getAttribute("sub"));
		System.out.println("principalDetails : "+principalDetails.getAttributes().get("response"));
		System.out.println("principalDetails : "+principalDetails.getAttributes());
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		userRepository.save(user); // 패스워드 암호화 해야함
		return "redirect:/loginForm";
	}
	
	@Secured("ROLE_ADMIN") //EnableGlobalMethodSecurity(securedEnabled = true) 간단하게 권한 걸기
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") //data가 실행되기 전에 실행됨 두개의 권한이 있을때 저렇게 쓸 수 있음
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터정보";
	}
	
}
