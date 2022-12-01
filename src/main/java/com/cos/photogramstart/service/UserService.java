package com.cos.photogramstart.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cos.photogramstart.domain.Subscribe.SubscribeRepository;
import com.cos.photogramstart.domain.User.User;
import com.cos.photogramstart.domain.User.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	SubscribeRepository subscribeRepository;
	
	@Autowired
	AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Transactional
	public User 회원프로필사진변경(int principalId, MultipartFile profileImageFile,String userProfileUrl) {
		
		if(userProfileUrl != null) {
			String key = userProfileUrl.substring(54);
			amazonS3Client.deleteObject(bucket, key);
		}

		ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(profileImageFile.getContentType());
        objectMetadata.setContentLength(profileImageFile.getSize());

        String originalFilename = profileImageFile.getOriginalFilename();//김영광.jpg
        int index = originalFilename.lastIndexOf(".");//'.'이라는 문자가 발견되는 위치에 해당하는 index값(위치값) = 3
        String ext = originalFilename.substring(index + 1);// index + 1 = 4 -> jpg
        
        String storeFileName = UUID.randomUUID() + "." + ext;// uuid.jpg
        String key = "upload/" + storeFileName;
        
        try (InputStream inputStream = profileImageFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
			e.printStackTrace();
		}
        
        String storeFileUrl = amazonS3Client.getUrl(bucket, key).toString();
        System.out.println("===========storeFileName=============");
        System.out.println(storeFileName);
        System.out.println("===========key=============");
        System.out.println(key);
        System.out.println("===========storeFileUrl=============");
        System.out.println(storeFileUrl);


		User userEntity = userRepository.findById(principalId).orElseThrow(() -> {
			// throw -> return 으로 변경
			return new CustomApiException("유저를 찾을 수 없습니다.");
		});
		userEntity.setProfileImageUrl(storeFileUrl);

		return userEntity;
	}

	 // 더티체킹으로 업데이트 됨.
	@Transactional(readOnly = true)
	public UserProfileDto 회원프로필(int pageUserId, int principalId) {
		UserProfileDto dto = new UserProfileDto();

		User userEntity = userRepository.findById(pageUserId).orElseThrow(() -> {
			throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
		});

		dto.setUser(userEntity);
		dto.setPageOwnerState(pageUserId == principalId);
		dto.setImageCount(userEntity.getImages().size());

		int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId);
		int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);

		dto.setSubscribeState(subscribeState == 1);
		dto.setSubscribeCount(subscribeCount);

		userEntity.getImages().forEach((image) -> {
			image.setLikeCount(image.getLikes().size());
		});

		return dto;
	}

	@Transactional
	public User 회원수정(int id, User user) {

		// 1. 영속화
		// 1. 무조건 찾았다.걱정마 get() 2. 못찾았어 익셉션 발동시킬게 orElseThrow()
		User userEntity = userRepository.findById(id).orElseThrow(() -> {
			return new CustomValidationApiException("찾을 수 없는 id입니다.");
		});

		// 2. 영속화된 오브젝트를 수정 - 더티체킹(업데이트완료)
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);

		userEntity.setName(user.getName());
		userEntity.setPassword(encPassword);
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getEmail());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());

		return userEntity;
	}
}