package org.cris6h16.file;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.file.Exceptions.FileComponentException;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.cris6h16.file.Exceptions.FileErrorCode.EMPTY_FILE_LIST;
import static org.cris6h16.file.Exceptions.FileErrorCode.EMPTY_MULTIPART_FILE;
import static org.cris6h16.file.Exceptions.FileErrorCode.FILE_SIZE_EXCEEDED;
import static org.cris6h16.file.Exceptions.FileErrorCode.INVALID_CONTENT_TYPE;
import static org.cris6h16.file.Exceptions.FileErrorCode.INVALID_FILE_NAME;

@Service
@Slf4j
class FileComponentImpl implements FileComponent {

    private final FileRepository fileRepository;

    FileComponentImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public Set<String> uploadImages(List<MultipartFile> list, long maxSizePerFile) {
        areFilesValid(list, maxSizePerFile);

        Set<String> uploadedUrls = new HashSet<>(3);
        try {
            for (MultipartFile multipartFile : list) {
                String url = fileRepository.upload(multipartFile);
                uploadedUrls.add(url);
            }
            return uploadedUrls;

        } catch (RuntimeException e) {
            this.rollbackUploads(uploadedUrls);
            throw e;
        }
    }

    private void areFilesValid(List<MultipartFile> list, long maxSizePerFile) {
        if (list == null || list.isEmpty()) throw new FileComponentException(EMPTY_FILE_LIST);
        for (MultipartFile multipartFile : list) {
            _isValidImg(multipartFile, maxSizePerFile);
        }
    }

    private void _isValidImg(MultipartFile file, long maxSize) {
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();

        if (file.isEmpty()) {
            throw new FileComponentException(EMPTY_MULTIPART_FILE);
        }
        if (file.getSize() > maxSize) {
            throw new FileComponentException(FILE_SIZE_EXCEEDED);
        }
        if (contentType == null || contentType.isEmpty()) {
            throw new FileComponentException(INVALID_CONTENT_TYPE);
        }
        if (!originalFilename.matches("^.*\\..*$")) {
            throw new FileComponentException(INVALID_FILE_NAME);
        }
    }

    private void rollbackUploads(Set<String> uploadedUrls) {
        for (String url : uploadedUrls) {
            this.fileRepository.deleteFileByUrl(url, 3);
        }
    }


}