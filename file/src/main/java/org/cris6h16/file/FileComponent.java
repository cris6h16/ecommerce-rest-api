package org.cris6h16.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileComponent {
    String upload(MultipartFile multipartFile);
}
