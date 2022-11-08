package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.Image.ImageRepository;
import com.cos.photogramstart.web.dto.Image.ImageUploadDto;

@Service
public class ImageService {
	
	@Autowired
	ImageRepository imageRepository;
	
	@Value("${file.path}")
	private String uploadFolder;
	
	
	public void 사진업로드(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		UUID uuid = UUID.randomUUID(); //uuid
		String imageFileName = uuid+"_"+imageUploadDto.getFile().getOriginalFilename();
		System.out.println("이미지 파일이름"+imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder+imageFileName);
		
		//통신이 일어나거나,IO가 일어날 때, 예외가 발생할 수 있다.
		try {
			Files.write(imageFilePath,imageUploadDto.getFile().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}