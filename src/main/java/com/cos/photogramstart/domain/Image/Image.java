package com.cos.photogramstart.domain.Image;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import com.cos.photogramstart.domain.Likes.Likes;
import com.cos.photogramstart.domain.User.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String caption; // 오늘 나 너무 피곤해!!
	private String postImageUrl; // 사진을 전송받아서 그 사진을 서버에 특정 폴더에 저장 - DB에 그 저장된 경로를 insert

	@JsonIgnoreProperties({ "images" })
	@JoinColumn(name = "userId")
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;

	// 이미지 좋아요
	@JsonIgnoreProperties({ "image" })
	@OneToMany(mappedBy = "image")
	private List<Likes> likes;

	@Transient // DB에 칼럼이 만들어지지 않는다.
	private Boolean likeState;
	
	@Transient // DB에 칼럼이 만들어지지 않는다.
	private int likeCount;

	// 댓글

	private LocalDateTime createDate;

	@PrePersist
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}