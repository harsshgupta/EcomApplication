package com.project.ecomapplication.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.project.ecomapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.project.ecomapplication.exceptions.ObjectNotFoundException;
import com.project.ecomapplication.entities.User;

@Service
public class ProfilePhotoService {

    @Autowired
    UserRepository userRepository;

    public String uploadImage(String email, MultipartFile multipartFile) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User Not Found"));
        String[] arr = multipartFile.getContentType().split("/");
        Path uploadPath = Paths.get("/home/harsh/ecommerce-picture");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(user.getId() + "." + arr[1]);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file ", ioe);
        }
        return "Image Uploaded Successfully";
    }

    public ResponseEntity<?> getImage(String email) {

        String dir = "/home/harsh/ecommerce-picture";

        User userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User Not Found."));
        File file = new File(dir);
        for (File files : file.listFiles()) {

            String filesName = files.getName().split("\\.")[0];
            String fileType = "image/" + files.getName().split("\\.")[1];
            if (userEntity.getId() == Long.parseLong(filesName)) {
                byte[] bytes = new byte[(int) files.length()];

                FileInputStream fis = null;
                try {

                    fis = new FileInputStream(files);

                    fis.read(bytes);
                    System.out.println(bytes);
                } catch (Exception e) {
                    System.out.println(e.fillInStackTrace());
                }

                return ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.valueOf(fileType))
                        .body(bytes);
            }
        }

        return new ResponseEntity<>("cant display image", HttpStatus.BAD_REQUEST);

    }

}