package com.cos.photogramstart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.Subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;

@Service
public class SubscribeService {
	
	@Autowired
	SubscribeRepository subscribeRepository;
	
	
	@Transactional(readOnly = true)
	public List<SubscribeDto> 구독리스트(int principalId,int pageUserId){
		return null;
	}
	
	@Transactional
	public void 구독하기(int fromUserId,int toUserId) {
		try {
			subscribeRepository.mSubscribe(fromUserId, toUserId);
		} catch (Exception e) {
			throw new CustomApiException("이미 구독을 하였습니다");
		}
	}
	
	@Transactional  
	public void 구독취소하기(int fromUserId,int toUserId) {
		subscribeRepository.mUnSubscribe(fromUserId, toUserId);
	}
}