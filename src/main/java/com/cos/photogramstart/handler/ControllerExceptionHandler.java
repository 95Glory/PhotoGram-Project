package com.cos.photogramstart.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;

@ControllerAdvice
@RestController
public class ControllerExceptionHandler {

	@ExceptionHandler(CustomValidationException.class)
	public String ValidationException(CustomValidationException e) {
		//1.클라이언트에게 응답할 때는 Script 좋음.
		//2. Ajax 통신 - CMRespDto
		//3. Android 통신 - CMRespDto
		return Script.back(e.getErrorMap().toString());
//		return new CMRespDto(-1,e.getMessage(),e.getErrorMap());
	}
}
