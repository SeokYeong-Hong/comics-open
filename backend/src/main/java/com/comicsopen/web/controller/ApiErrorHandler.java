package com.comicsopen.web.controller;
import org.springframework.http.*;import org.springframework.web.bind.MethodArgumentNotValidException;import org.springframework.web.bind.annotation.*;import java.util.Map;
@RestControllerAdvice public class ApiErrorHandler {@ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<?> validation(MethodArgumentNotValidException ex){return ResponseEntity.badRequest().body(Map.of("error","입력값을 확인하세요. (이메일 형식, 닉네임 2자 이상, 비밀번호 8자 이상, 글자 수 제한)"));}}
