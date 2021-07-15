package com.hydev.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hydev.security1.model.User;

// @Repository 어노테이션 없어도 JpaRepository 상속해서 자동으로 IoC등록됨
public interface UserRepository extends JpaRepository<User, Integer>{
	
	// findBy는 규칙 , Username은 문법 
	// select * from user where username=? 호출
	public User findByUsername(String username);
}
