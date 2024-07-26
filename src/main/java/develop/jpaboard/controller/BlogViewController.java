package develop.jpaboard.controller;

import develop.jpaboard.domain.Article;
import develop.jpaboard.domain.Comment;
import develop.jpaboard.domain.File;
import develop.jpaboard.dto.*;
import develop.jpaboard.repository.BlogRepository;
import develop.jpaboard.service.BlogService;
import develop.jpaboard.service.FileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class BlogViewController {

    private final BlogService blogService;
    private final FileService fileService;

    @GetMapping("/articles")
    public String getArticles(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "3") int size) {
        Page<Article> articlesPage = blogService.findAll(page, size);
//        List<ArticleListViewResponse> articles = blogService.findAll().stream()
//                .map(ArticleListViewResponse::new)
//                .toList();

        model.addAttribute("articles", articlesPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", articlesPage.getTotalPages());
        model.addAttribute("totalItems", articlesPage.getTotalElements());

        return "articleList";
    }

    @Transactional
    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article findArticle = blogService.findById(id);
        findArticle.incrementViewCount();
        model.addAttribute("article", new ArticleViewResponse(findArticle));
        model.addAttribute("commentDto", new CommentDto());
        return "article";
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            Article findArticle = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(findArticle));
        }

        return "newArticle";
    }

    @Transactional
    @PostMapping("/new-article")
    public String newArticle(@ModelAttribute("article") ArticleViewResponse article, @RequestParam("image") MultipartFile imageFile, Model model) {

        //넘어온 건 ArticleViewResponse고 만들어야 하는 건 AddArticleRequest..
        AddArticleRequest addArticleRequest = AddArticleRequest.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .viewCount(article.getViewCount())
                .build();

        Article savedArticle = blogService.save(addArticleRequest);
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                File file = fileService.save(savedArticle, imageFile);
                savedArticle.addFile(file);
            }
        } catch (IOException e) {
            return "redirect:/error";
        }


        return "redirect:/articles/"+savedArticle.getId();
    }

    @Transactional
    @PostMapping("/articles/{id}/edit")
    public String updateArticle(@PathVariable Long id,
                                @ModelAttribute("article") UpdateArticleRequest updateArticleRequest,
                                @RequestParam("image") MultipartFile imageFile,
//                                @RequestParam("files[0].id") Long fileId,
                                Model model) throws IOException {
    /*
        fileId를 article에서 찾는 방법
        Article article = blogService.findById(id);
        Long fileId = article.getFiles().get(0).getId();
     */

        Article article = blogService.findById(id);
        blogService.update(id, updateArticleRequest);

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
            return "redirect:/error";
        }

        return "redirect:/articles/" + id;
    }

    @GetMapping("/articles/{id}/delete")
    public String deleteArticle(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/articles";
    }

    @PostMapping("/articles/{id}/comments")
    public String addComment(@PathVariable Long id, AddCommentRequest request) {
        blogService.addComment(id, request);
        return "redirect:/articles/" + id;
    }

    @Transactional
    @PostMapping("/delete/comment")
    public String deleteComment(@RequestParam("articleId") Long articleId, @RequestParam Long commentId) {
        Article findArticle = blogService.findById(articleId);
        blogService.deleteComment(commentId);
        findArticle.decrementCommentCount();
        return "redirect:/articles/"+articleId;
    }

    @GetMapping("/articles/{id}/editComment")
    public String updateComment(@ModelAttribute("article") UpdateCommentRequest request, @PathVariable Long id, @RequestParam Long commentId, Model model) {

//        Article findArticle = blogService.findById(id);
        Comment comment = blogService.findCommentById(commentId);

        CommentDto commentDto = new CommentDto(comment.getId(),comment.getContent(),comment.getUserName());

        model.addAttribute("id", id);
        model.addAttribute("comment", commentDto);

        return "editComment";
    }

    @PostMapping("{id}/editComment")
    public String updateComment(@PathVariable Long id, @ModelAttribute("comment") UpdateCommentRequest request) {

//        UpdateCommentRequest request = UpdateCommentRequest.builder()
//                .commentId(comment.getId())
//                .content(comment.getContent())
//                .build();

        blogService.updateComment(request.getId(), request);
        return "redirect:/articles/" + id;
    }

    @PostMapping("/delete/image")
    public String deleteImage(@RequestParam("articleId") Long articleId, @RequestParam("fileId") Long fileId){
        fileService.delete(fileId);
        return "redirect:/articles/"+articleId;
    }
}
