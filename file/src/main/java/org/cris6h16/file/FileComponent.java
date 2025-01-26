package org.cris6h16.file;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface FileComponent {
    Set<String> uploadImages(List<MultipartFile> list, long maxSizePerFile);
}
