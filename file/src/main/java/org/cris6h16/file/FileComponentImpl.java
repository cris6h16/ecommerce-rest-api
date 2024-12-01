package org.cris6h16.file;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.file.Exceptions.FileComponentException;
import org.cris6h16.file.Exceptions.FileErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.cris6h16.file.Exceptions.FileErrorCode.FILE_CONTENT_TYPE_IS_NOT_IMAGE;
import static org.cris6h16.file.Exceptions.FileErrorCode.FILE_CONTENT_TYPE_IS_NULL;
import static org.cris6h16.file.Exceptions.FileErrorCode.FILE_IS_EMPTY;
import static org.cris6h16.file.Exceptions.FileErrorCode.FILE_SIZE_EXCEEDED;

@Service
@Slf4j
class FileComponentImpl implements FileComponent {

    private final FileRepository fileRepository;

    FileComponentImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public Set<String> uploadImages(List<MultipartFile> list) {
        areValidImages(list);

        Set<String> uploadedUrls = new HashSet<>();
        try {
            for (MultipartFile multipartFile : list) {
                String url = this.fileRepository.upload(multipartFile, 3);
                uploadedUrls.add(url);
            }
            return uploadedUrls;

        } catch (Exception e) {
            log.error("Error uploading files: ", e);
            this.rollbackUploads(uploadedUrls);
            throw e;
        }
    }

    private void areValidImages(List<MultipartFile> multipartFiles) {
        for (MultipartFile multipartFile : multipartFiles) {
            _isValidImg(multipartFile);
        }
    }

    private void rollbackUploads(Set<String> uploadedUrls) {
        for (String url : uploadedUrls) {
            this.fileRepository.deleteFileByUrl(url, 3);
        }
    }


    private void _isValidImg(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        long maxSize = 5 * 1024 * 1024;
        if (multipartFile.isEmpty()) {
            throw new FileComponentException(FILE_IS_EMPTY);
        }
        if (multipartFile.getSize() > maxSize) {
            throw new FileComponentException(FILE_SIZE_EXCEEDED);
        }
        if (contentType == null) {
            throw new FileComponentException(FILE_CONTENT_TYPE_IS_NULL);
        }
        if (!contentType.startsWith("image/")) {
            throw new FileComponentException(FILE_CONTENT_TYPE_IS_NOT_IMAGE);
        }
    }
}