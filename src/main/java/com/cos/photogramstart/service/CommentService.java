package com.cos.photogramstart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.Comment.Comment;
import com.cos.photogramstart.domain.Comment.CommentRepository;
import com.cos.photogramstart.domain.Image.Image;
import com.cos.photogramstart.domain.User.User;
import com.cos.photogramstart.domain.User.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	UserRepository userRepository;

	@Transactional
	public Comment 댓글쓰기(String content, int imageId, int UserId) {

		// Tip (객체를 만들 때, id값만 담아서 insert 할 수 있다.)
		// 대신 return시에 image객체와 user객체는 id값만 가지고 있는 빈 객체를 리턴받는다.
		Image image = new Image();
		image.setId(imageId);

		User userEntity = userRepository.findById(UserId).orElseThrow(() -> {
			throw new CustomApiException("유저 아이디를 찾을 수 없습니다.");
		});

		Comment comment = new Comment();
		comment.setContent(content);
		comment.setImage(image);
		comment.setUser(userEntity);

		return commentRepository.save(comment);
	}

	@Transactional
	public void 댓글삭제(int id) {
		commentRepository.deleteById(id);
	}
}