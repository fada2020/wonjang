package com.example.wonjang.controller;

import com.example.wonjang.model.Feedback;
import com.example.wonjang.service.LectureService;
import com.example.wonjang.utils.FileUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Slf4j
@RequestMapping("/api/v1")
@RestController
public class ApiV1Controller {
    private final LectureService lectureService;
    private final FileUtil fileUtil;
    public ApiV1Controller(LectureService lectureService, FileUtil fileUtil) {
        this.lectureService = lectureService;
        this.fileUtil = fileUtil;
    }
    @PostMapping("/feedback")
    public ResponseEntity feedback(
            @Valid @ModelAttribute("feedback") Feedback feedback

    ) {
    return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/image-upload")
    // @RequestParam은 자바스크립트에서 설정한 이름과 반드시 같아야합니다.
    public ResponseEntity<?> imageUpload(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
        try {

            String uploadDirectory = fileUtil.saveUUIDFile(file, "announce");


            log.debug("************************ 업로드 컨트롤러 실행 ************************");
            log.debug("uploadDirectory= {}", uploadDirectory);

            // Ajax에서 업로드 된 파일의 이름을 응답 받을 수 있도록 해줍니다.
            return ResponseEntity.ok(uploadDirectory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("이미지 업로드 실패");
        }

    }
}
