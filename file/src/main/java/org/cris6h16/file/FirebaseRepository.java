package org.cris6h16.file;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.cris6h16.file.Exceptions.FileComponentException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.cris6h16.file.Exceptions.FileErrorCode.FILE_DELETE_BY_URL_ALL_RETRIES_ERROR;
import static org.cris6h16.file.Exceptions.FileErrorCode.FILE_DELETE_BY_URL_ERROR;
import static org.cris6h16.file.Exceptions.FileErrorCode.FILE_UPLOAD_ALL_RETRIES_ERROR;

@Slf4j
@Component
 class FirebaseRepository implements FileRepository {
    static String bucketName = "ecommerce-de918.firebasestorage.app";
    static String credentialsFile = "firebase-private-key.json";
    private final Storage storage;

    public FirebaseRepository() {
        this.storage = getStorage();
    }

    private Storage getStorage() {
        try {
            InputStream inputStream = FileComponentImpl.class.getClassLoader().getResourceAsStream(credentialsFile);
            if (inputStream == null) throw new IOException("File not found in resources: " + credentialsFile);
            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            return StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        } catch (IOException e) {
            log.error("Error getting storage: ", e);
            throw new IllegalStateException("Error initializing storage");
        }
    }

    @Override
    public void deleteFileByUrl(String fileUrl, int retries) {
        int attempts = 0;
        while (attempts++ < retries) {
            try {
                this._deleteFileByUrl(fileUrl);
                return;

            } catch (Exception e) {
                if (attempts >= retries) {
                    log.error("Failed to delete file: {} after {} attempts", fileUrl, attempts);
                } else {
                    log.warn("Retrying delete... attempt: {}", attempts);
                }
            }
        }

        throw new FileComponentException(FILE_DELETE_BY_URL_ALL_RETRIES_ERROR);
    }

    private void _deleteFileByUrl(String fileUrl) {
        try {
            String fileName = extractFileNameFromUrl(fileUrl);
            BlobId blobId = BlobId.of(bucketName, fileName);
            storage.delete(blobId);

        } catch (Exception e) {
            log.error("Error deleting file: ", e);
            throw new FileComponentException(FILE_DELETE_BY_URL_ERROR);
        }
    }

    // https://firebasestorage.googleapis.com/v0/b/ecommerce-de918.firebasestorage.app/o/1e71c9bc-fbc3-4086-99e9-2dfa3bf37c3b.jpg?alt=media
    private String extractFileNameFromUrl(String fileUrl) {
        String[] parts = fileUrl.split("/");
        return parts[parts.length - 1].split("\\?")[0];
    }

    private String uploadFile(File file, String fileName) {
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

        try {
            storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        } catch (IOException e) {
            throw new RuntimeException("Unable to read all bytes from file: " + e.getMessage(), e);
        }

        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) {
        try {
            // Obtener directorio temporal según el sistema operativo
            Path tempDir = Files.createTempDirectory("upload_");

            // Crear el archivo en el directorio temporal
            File tempFile = new File(tempDir.toFile(), fileName);

            // Escribir los bytes en el archivo
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(multipartFile.getBytes());
            }

            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("Unable to convert file: " + e.getMessage(), e);
        }
    }

    @Override
    public String upload(MultipartFile multipartFile) {
        String filename = getFilename(multipartFile.getOriginalFilename());

        File file = this.convertToFile(multipartFile, filename);
        String URL = this.uploadFile(file, filename);
        if (!file.delete()) log.error("Failed to delete file: {}", file.getName());

        return URL;
    }


    private String getFilename(String originalFilename) {
        return UUID.randomUUID().toString() + getExtension(originalFilename);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
