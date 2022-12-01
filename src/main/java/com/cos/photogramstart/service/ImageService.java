package com.cos.photogramstart.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.Image.Image;
import com.cos.photogramstart.domain.Image.ImageRepository;
import com.cos.photogramstart.web.dto.Image.ImageUploadDto;

@Service
public class ImageService {

	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	
	@Transactional(readOnly = true)
	public List<Image> 인기사진() {
		return imageRepository.mPopular();
	}

	@Transactional(readOnly = true) // 영속성 컨텍스트 변경 감지를 해서, 더티체킹, flush(반영)
	public Page<Image> 이미지스토리(int principalId, Pageable pageable) {
		Page<Image> images = imageRepository.mStory(principalId, pageable);

		// images에 좋아요 상태담기
		images.forEach((image) -> {

			image.setLikeCount(image.getLikes().size());

			image.getLikes().forEach((like) -> {
				if (like.getUser().getId() == principalId) { // 해당 이미지에 좋아요한 사람들을 찾아서 현재 로긴한 사람이 좋아요 한것인지 비교
					image.setLikeState(true);
				}
			});

		});
		return images;
	}

	@Transactional
	public void 사진업로드(MultipartFile file,ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        String originalFilename = file.getOriginalFilename();//김영광.jpg
        int index = originalFilename.lastIndexOf(".");//'.'이라는 문자가 발견되는 위치에 해당하는 index값(위치값) = 3
        String ext = originalFilename.substring(index + 1);// index + 1 = 4 -> jpg
        
        String storeFileName = UUID.randomUUID() + "." + ext;// uuid.jpg
        String key = "upload/" + storeFileName;

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
			e.printStackTrace();
		}

        String storeFileUrl = amazonS3Client.getUrl(bucket, key).toString();
        Image image = imageUploadDto.toEntity(storeFileUrl, principalDetails.getUser());
        imageRepository.save(image);
    }		
}
	
	
//	@Transactional
//	public void 사진업로드(MultipartFile file,ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
//		UUID uuid = UUID.randomUUID(); // UUID
//		String imageFileName = uuid + "_" + imageUploadDto.getFile().getOriginalFilename();
//		
//
//		Path imageFilePath = Paths.get(uploadFolder + imageFileName);
//
//		// 통신이 일어나거나,IO가 일어날 때, 예외가 발생할 수 있다.
//		try {
//			Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		// image 테이블에 저장
//		Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser());
//		imageRepository.save(image);
//
//		// System.out.println(imageEntity.toString());
//	}
