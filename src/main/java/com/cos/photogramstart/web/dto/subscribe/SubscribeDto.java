package com.cos.photogramstart.web.dto.subscribe;

import com.cos.photogramstart.domain.User.User;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SubscribeDto {
	private int userId;
	private String Username;
	private String profileImageUrl;
	private Integer subscribeState;
	private Integer equalUserState;
}