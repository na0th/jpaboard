package develop.jpaboard.service;

import develop.jpaboard.domain.Article;
import develop.jpaboard.domain.Comment;
import develop.jpaboard.dto.AddArticleRequest;
import develop.jpaboard.dto.AddCommentRequest;
import develop.jpaboard.dto.UpdateArticleRequest;
import develop.jpaboard.repository.BlogRepository;
import develop.jpaboard.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class BlogService {
    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;

    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }
    public Article update(Long id, UpdateArticleRequest updateRequest) {
        Article article = findById(id);
        article.updateDetails(updateRequest.getTitle(), updateRequest.getContent());
        return blogRepository.save(article);
    }
    public List<Article> findAll() {
        return blogRepository.findAll();
    }
    public Article findById(Long id){
        return blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }
    public void delete(Long id) {
        blogRepository.deleteById(id);
    }

    public Comment addComment(Long articleId,AddCommentRequest request) {
        Article findArticle = findById(articleId);

        Comment comment = Comment.builder()
                .article(findArticle)
                .content(request.getContent())
                .build();
        findArticle.getComments().add(comment);

        return commentRepository.save(comment);
    }
}
