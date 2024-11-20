package com.example.wonjang.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class FileUtil {
    @Value("${file-path.upload-path}")
    private String uploadPath;
    public String saveFile(MultipartFile file, String uploadDir) throws IOException {
        // 파일이 비어 있지 않은지 확인
        if (file != null && !file.isEmpty()) {
            // 파일 이름 생성
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // 업로드할 디렉토리와 파일 경로 설정
            File uploadDirectory = new File(uploadPath + uploadDir);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs(); // 디렉토리가 없으면 생성
            }

            // 업로드할 파일 생성
            File destinationFile = new File(uploadDirectory, fileName);

            // 파일 저장
            file.transferTo(destinationFile);

            // 파일이 저장된 경로를 반환 (필요 시 DB에 저장할 수 있음)
            // 예: return destinationFile.getAbsolutePath();
            return "/storage/" + uploadDir + "/" + fileName;
        }
        return null;
    }
}
