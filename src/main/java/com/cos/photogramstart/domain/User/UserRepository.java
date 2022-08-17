package com.cos.photogramstart.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;


// 어노테이션이 없어도 JpaRepository 를 상속하면 ioc등록이 된다
public interface UserRepository extends JpaRepository<User, Integer> {
	//JPA QueryMethod
	User findByUsername(String username);
}
