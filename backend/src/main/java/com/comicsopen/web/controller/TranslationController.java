package com.comicsopen.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/translation")
public class TranslationController {
    private final RestTemplate client = new RestTemplate();
    private final String baseUrl;
    public TranslationController(@Value("${comics.fastapi.base-url}") String baseUrl) {
        this.baseUrl = baseUrl.replaceAll("/+$", "");
    }
    @PostMapping(value="/ocr", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> ocr(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty() || file.getSize() > 8 * 1024 * 1024)
            return ResponseEntity.badRequest().body(Map.of("error", "8MB 이하의 이미지를 선택하세요."));
        String type = file.getContentType();
        if (type == null || !Set.of("image/png", "image/jpeg", "image/webp").contains(type))
            return ResponseEntity.badRequest().body(Map.of("error", "PNG, JPEG 또는 WEBP 이미지만 가능합니다."));
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", new ByteArrayResource(file.getBytes()) {
            @Override public String getFilename() { return "upload-image.png"; }
        });
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        try {
            ResponseEntity<Object> response = client.postForEntity(baseUrl + "/ocr", new HttpEntity<>(form, headers), Object.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception ex) {
            return ResponseEntity.status(502).body(Map.of("error", "OCR 서버에 연결할 수 없습니다."));
        }
    }
    @PostMapping(value="/translate", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> translate(@RequestBody Map<String, String> input) {
        String text = input.getOrDefault("text", "");
        if (text == null || text.isBlank() || text.length() > 10000)
            return ResponseEntity.badRequest().body(Map.of("error", "번역 텍스트는 1~10000자로 입력하세요."));
        try {
            Object body = client.postForObject(baseUrl + "/translate", Map.of("text", text), Object.class);
            return ResponseEntity.ok(body);
        } catch (Exception ex) {
            return ResponseEntity.status(502).body(Map.of("error", "번역 서버에 연결할 수 없습니다."));
        }
    }
}
