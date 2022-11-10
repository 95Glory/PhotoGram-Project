package com.cos.photogramstart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.Likes.LikesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikesService {

	@Autowired
	LikesRepository likesRepository;

	@Transactional
	public void 좋아요(int imageId, int principalID) {
		likesRepository.mLikes(imageId, principalID);
	}

	@Transactional
	public void 좋아요취소(int imageId, int principalID) {
		likesRepository.mUnLikes(imageId, principalID);
	}
}
