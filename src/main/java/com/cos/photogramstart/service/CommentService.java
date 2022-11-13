package com.cos.photogramstart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.Comment.Comment;
import com.cos.photogramstart.domain.Comment.CommentRepository;
import com.cos.photogramstart.domain.Image.Image;
import com.cos.photogramstart.domain.User.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

	@Autowired
	CommentRepository commentRepository;

	@Transactional
	public Comment 댓글쓰기(String content, int imageId, int UserId) {

		// Tip (객체를 만들 때, id값만 담아서 insert 할 수 있다.)
		Image image = new Image();
		image.setId(imageId);

		User user = new User();
		user.setId(UserId);

		Comment comment = new Comment();
		comment.setContent(content);
		comment.setImage(image);
		comment.setUser(user);

		return commentRepository.save(comment);
	}

	@Transactional
	public Comment 댓글삭제() {
		return null;
	}
}
