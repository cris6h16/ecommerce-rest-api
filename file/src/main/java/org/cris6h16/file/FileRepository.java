package org.cris6h16.file;

import org.springframework.web.multipart.MultipartFile;

 interface FileRepository {
    String upload(MultipartFile multipartFile);
    void deleteFileByUrl(String fileUrl, int retries);
}
