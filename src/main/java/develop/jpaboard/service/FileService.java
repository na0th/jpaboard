package develop.jpaboard.service;


import develop.jpaboard.domain.Article;
import develop.jpaboard.domain.Comment;
import develop.jpaboard.domain.File;
import develop.jpaboard.repository.BlogRepository;
import develop.jpaboard.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final FileRepository fileRepository;

    public File findFileById(Long fileId){
        return fileRepository.findById(fileId).orElseThrow(() -> new IllegalArgumentException("not found: " + fileId));
    }
    public File save(Article article, MultipartFile imageFile) throws IOException {

        File file = File.builder()
                .fileName(imageFile.getOriginalFilename())
                .contentType(imageFile.getContentType())
                .data(imageFile.getBytes())
                .article(article)
                .size(imageFile.getSize())
                .build();

        return fileRepository.save(file);

    }

    public File update(Long fileId, MultipartFile imageFile) throws IOException {
        File file = findFileById(fileId);

        file.updateFileInfo(imageFile.getName()
                ,imageFile.getContentType()
                ,imageFile.getBytes()
                ,imageFile.getSize()
        );

        return fileRepository.save(file);
    }
}
