package com.cos.photogramstart.web.dto.user;

import javax.validation.constraints.NotBlank;

import com.cos.photogramstart.domain.User.User;

import lombok.Data;

@Data
public class UserUpdateDto {

	@NotBlank
	private String name; // 필수 값

	@NotBlank
	private String password;// 필수 값

	private String website;
	private String bio;
	private String phone;
	private String gender;

	// 조금 위험함. 코드 수정이 필요할 예정
	public User toEntity() {
		return User.builder().name(name)// 이름을 기재 안했으면 문제 !! validation 체크해야함
				.password(password)// 패스워드를 기재 안했으면 문제 !! validation 체크해야함
				.website(website).bio(bio).phone(phone).gender(gender).build();
	}
}