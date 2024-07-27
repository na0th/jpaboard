package develop.jpaboard.controller;

import develop.jpaboard.domain.Article;
import develop.jpaboard.domain.Comment;
import develop.jpaboard.domain.File;
import develop.jpaboard.dto.*;
import develop.jpaboard.repository.BlogRepository;
import develop.jpaboard.repository.CommentRepository;
import develop.jpaboard.repository.FileRepository;
import develop.jpaboard.service.BlogService;
import develop.jpaboard.service.FileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
public class ApiViewController {
    private final BlogService blogService;
    private final FileService fileService;
    private final FileRepository fileRepository;

//  글 작성
    @Transactional
    @PostMapping("/articles")
    public ResponseEntity<?> addArticle(
            @ModelAttribute("article") AddArticleRequest request,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        Article savedArticle = blogService.save(request);

        ArticleDto articleDto = ArticleDto.builder()
                    .id(savedArticle.getId())
                    .title(savedArticle.getTitle())
                    .content(savedArticle.getContent())
                    .viewCount(savedArticle.getViewCount())
                    .files(savedArticle.getFiles().stream()
                            .map(file -> new FileDto(file.getId(), file.getFileName(), file.getContentType(), file.getSize(), file.getData()))
                            .toList())
                    .build();

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                File file = fileService.save(savedArticle, imageFile);
                savedArticle.addFile(file);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + e.getMessage());
                }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleDto);
    }
//  글 목록 조회
    @GetMapping("/articles")
    public ResponseEntity<ArticleListResponse> getArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        Page<Article> articlesPage = blogService.findAll(page, size);

        List<ArticleListViewResponse> articles = articlesPage.getContent().stream()
                .map(ArticleListViewResponse::new)
                .toList();

        ArticleListResponse response = ArticleListResponse.builder()
                .articles(articles)
                .currentPage(articlesPage.getNumber())
                .size(articlesPage.getSize())
                .totalPages(articlesPage.getTotalPages())
                .totalItems(articlesPage.getTotalElements())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);

    }
    //글 1개 조회
    @Transactional
    @GetMapping("/articles/{id}")
    public ResponseEntity<Map<String, Object>> getArticle(@PathVariable Long id)  {

        Article findArticle = blogService.findById(id);
        findArticle.incrementViewCount();

        Map<String, Object> response = new HashMap<>();
        response.put("article", new ArticleViewResponse(findArticle));
        response.put("commentDto", new CommentDto());

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);

    }

    //글 수정
    @Transactional
    @PutMapping("/articles/{id}")
    public ResponseEntity<?> updateArticle(@PathVariable Long id
                                                    ,@ModelAttribute("article") UpdateArticleRequest updateArticleRequest
                                                    ,@RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        Article article = blogService.update(id, updateArticleRequest);
        ArticleDto response = new ArticleDto(article);

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                try{
                    Long fileId = article.getFiles().get(0).getId();
                    fileService.update(fileId, imageFile);

                } catch(IndexOutOfBoundsException e){
                    File file = fileService.save(article, imageFile);
                    article.addFile(file);
                }
            }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + e.getMessage());
        }
        return ResponseEntity.status((HttpStatus.OK))
                .body(response);
    }
    //글 삭제
    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        blogService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 이미지 삭제
    @Transactional
    @DeleteMapping("/articles/{id}/image/{fileId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id,
                                            @PathVariable Long fileId
            ) {
        // 글, 파일 찾기
            Article article = blogService.findById(id);
            File file = fileService.findFileById(fileId);
        //삭제 article에서, file에서
            fileService.delete(fileId);
            article.removeFile(file);

            return ResponseEntity.noContent().build();

    }

    //댓글 작성
    @Transactional
    @PostMapping("/articles/{id}/comment")
    public ResponseEntity<?> addComment(@PathVariable Long id,
                                        AddCommentRequest commentDto) {
        blogService.addComment(id, commentDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentDto);
    }

    //댓글 수정
    @Transactional
    @PutMapping("/articles/{id}/comment/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long id,
                                           @ModelAttribute("commentDto") UpdateCommentRequest request) {
        blogService.updateComment(id, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                        .body(request);
    }
    //댓글 삭제
    @Transactional
    @DeleteMapping("/articles/{id}/comment")
    public ResponseEntity<?> deleteComment(@PathVariable Long id,
                                           @RequestParam Long commentId ) {

        Article article = blogService.findById(id);
        article.decrementCommentCount();

        blogService.deleteComment(commentId);

        return ResponseEntity.ok("Comment successfully deleted");
    }


}
