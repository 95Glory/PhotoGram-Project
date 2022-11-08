package com.cos.photogramstart.web.dto.Image;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ImageUploadDto {
	private MultipartFile file;
	private String caption;
}