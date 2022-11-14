package com.cos.photogramstart.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.Image.Image;
import com.cos.photogramstart.service.ImageService;
import com.cos.photogramstart.service.LikesService;
import com.cos.photogramstart.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ImageApiController {

	@Autowired
	ImageService imageService;

	@Autowired
	LikesService likesService;

	@GetMapping("/api/image")
	public ResponseEntity<?> imageStory(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@PageableDefault(size = 3) Pageable pageable) {
		Page<Image> images = imageService.이미지스토리(principalDetails.getUser().getId(), pageable);
		return new ResponseEntity<>(new CMRespDto<>(1, "이미지스토리 불러오기 성공", images), HttpStatus.OK);
	}

	@PostMapping("/api/image/{imageId}/likes")
	public ResponseEntity<?> likes(@PathVariable int imageId,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		likesService.좋아요(imageId, principalDetails.getUser().getId());
		return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 성공", null), HttpStatus.OK);
	}

	@DeleteMapping("/api/image/{imageId}/likes")
	public ResponseEntity<?> usLikes(@PathVariable int imageId,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		likesService.좋아요취소(imageId, principalDetails.getUser().getId());
		return new ResponseEntity<>(new CMRespDto<>(1, "좋아요취소 성공", null), HttpStatus.OK);
	}
}
