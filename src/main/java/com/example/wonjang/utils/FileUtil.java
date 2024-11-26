package com.example.wonjang.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUtil {
    @Value("${file-path.upload-path}")
    private String uploadPath;
    public String saveFileToServer(MultipartFile file,  WebApplicationContext context) throws IOException {
        // 서버에 저장할 경로
        String uploadDirectory = context.getServletContext().getRealPath("/resources/assets/images/upload");

        // 업로드 된 파일의 이름
        String originalFileName = file.getOriginalFilename();

        // 업로드 된 파일의 확장자
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 업로드 될 파일의 이름 재설정 (중복 방지를 위해 UUID 사용)
        String uuidFileName = UUID.randomUUID().toString() + fileExtension;

        // 위에서 설정한 서버 경로에 이미지 저장
        file.transferTo(new File(uploadDirectory, uuidFileName));
        return uploadDirectory;
    }
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
    public String saveUUIDFile(MultipartFile file, String uploadDir) throws IOException {
        if (file != null && !file.isEmpty()) {
            // 파일 이름 생성
            // 업로드 된 파일의 이름
            String originalFileName = file.getOriginalFilename();

            // 업로드 된 파일의 확장자
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

            // 업로드 될 파일의 이름 재설정 (중복 방지를 위해 UUID 사용)
            String uuidFileName = UUID.randomUUID().toString() + fileExtension;

            // 업로드할 디렉토리와 파일 경로 설정
            File uploadDirectory = new File(uploadPath + uploadDir);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs(); // 디렉토리가 없으면 생성
            }

            // 업로드할 파일 생성
            File destinationFile = new File(uploadDirectory, uuidFileName);

            // 파일 저장
            file.transferTo(destinationFile);

            // 파일이 저장된 경로를 반환 (필요 시 DB에 저장할 수 있음)
            // 예: return destinationFile.getAbsolutePath();
            return "/storage/" + uploadDir + "/" + uuidFileName;
        }
        return null;
    }
}
