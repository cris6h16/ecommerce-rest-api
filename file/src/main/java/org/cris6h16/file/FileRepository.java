package org.cris6h16.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileRepository {
    String upload(MultipartFile multipartFile, int retries);
    void deleteFileByUrl(String fileUrl, int retries);
}
