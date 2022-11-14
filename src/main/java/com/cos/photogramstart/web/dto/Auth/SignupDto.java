package com.cos.photogramstart.web.dto.Auth;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.cos.photogramstart.domain.User.User;

import lombok.Data;

// Dto는 통신을 위해서 필요한 데이터를 담아내는 오브젝트
@Data
public class SignupDto {

	@Size(min = 2, max = 20)
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	@NotBlank
	private String email;

	@NotBlank
	private String name;

	public User toEntity() {
		return User.builder().username(username).password(password).name(name).email(email).build();
	}
}