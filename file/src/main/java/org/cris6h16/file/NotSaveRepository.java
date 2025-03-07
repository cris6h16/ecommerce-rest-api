package org.cris6h16.file;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Profile("test")
class NotSaveRepository implements FileRepository{

    @Override
    public String upload(MultipartFile multipartFile) {
        return "https://www.shutterstock.com/image-vector/default-ui-image-placeholder-wireframes-600nw-1037719192.jpg";
    }

    @Override
    public void deleteFileByUrl(String fileUrl, int retries) {
    }
}
