package develop.jpaboard.controller;

import develop.jpaboard.domain.Article;
import develop.jpaboard.dto.AddArticleRequest;
import develop.jpaboard.dto.ArticleListViewResponse;
import develop.jpaboard.dto.ArticleViewResponse;
import develop.jpaboard.repository.BlogRepository;
import develop.jpaboard.service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    @GetMapping("/new-article")
    public String newArticle(Model model) {
        model.addAttribute("article",new ArticleViewResponse());
        return "newArticle";
    }
    @PostMapping("/new-article")
    public String newArticle(@ModelAttribute("article") ArticleViewResponse article, Model model) {

        //넘어온 건 ArticleViewResponse고 만들어야 하는 건 AddArticleRequest..
        AddArticleRequest addArticleRequest = AddArticleRequest.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .build();

        blogService.save(addArticleRequest);

        return "redirect:/articles";
    }

}
