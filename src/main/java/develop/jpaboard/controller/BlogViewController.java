package develop.jpaboard.controller;

import develop.jpaboard.domain.Article;
import develop.jpaboard.domain.Comment;
import develop.jpaboard.dto.*;
import develop.jpaboard.repository.BlogRepository;
import develop.jpaboard.service.BlogService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class BlogViewController {

    private final BlogService blogService;
    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles",articles);

        return "articleList";
    }
    @Transactional
    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article findArticle = blogService.findById(id);
        findArticle.incrementViewCount();
        model.addAttribute("article",new ArticleViewResponse(findArticle));
        return "article";
    }
    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null){
            model.addAttribute("article",new ArticleViewResponse());
        }else{
            Article findArticle = blogService.findById(id);
            model.addAttribute("article",new ArticleViewResponse(findArticle));
        }

        return "newArticle";
    }
    @PostMapping("/new-article")
    public String newArticle(@ModelAttribute("article") ArticleViewResponse article, Model model) {

        //넘어온 건 ArticleViewResponse고 만들어야 하는 건 AddArticleRequest..
        AddArticleRequest addArticleRequest = AddArticleRequest.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .viewCount(article.getViewCount())
                .build();

        blogService.save(addArticleRequest);

        return "redirect:/articles";
    }

    @PostMapping("/articles/{id}/edit")
    public String updateArticle(@PathVariable Long id, @ModelAttribute("article") UpdateArticleRequest updateArticleRequest, Model model) {
        blogService.update(id, updateArticleRequest);
        return "redirect:/articles/"+id;
    }

    @GetMapping("/articles/{id}/delete")
    public String deleteArticle(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/articles";
    }

    @PostMapping("/articles/{id}/comments")
    public String addComment(@PathVariable Long id, AddCommentRequest request) {
        blogService.addComment(id,request);
        return "redirect:/articles/"+id;
    }

    @PostMapping("/delete/comment")
    public String deleteComment(@RequestParam Long commentId) {
        blogService.deleteComment(commentId);
        return "redirect:/articles";
    }
}
