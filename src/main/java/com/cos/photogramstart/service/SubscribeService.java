package com.cos.photogramstart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.Subscribe.SubscribeRepository;

@Service
public class SubscribeService {
	
	@Autowired
	SubscribeRepository subscribeRepository;
	
	@Transactional
	public void 구독하기(int fromUserId,int toUserId) {
		subscribeRepository.mSubscribe(fromUserId, toUserId);
	}
	
	@Transactional
	public void 구독취소하기(int fromUserId,int toUserId) {
		subscribeRepository.mUnSubscribe(fromUserId, toUserId);
	}
}
