package develop.jpaboard.service;

import develop.jpaboard.domain.Article;
import develop.jpaboard.domain.Comment;
import develop.jpaboard.dto.AddArticleRequest;
import develop.jpaboard.dto.AddCommentRequest;
import develop.jpaboard.dto.UpdateArticleRequest;
import develop.jpaboard.dto.UpdateCommentRequest;
import develop.jpaboard.repository.BlogRepository;
import develop.jpaboard.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<Article> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return blogRepository.findAll(pageable);
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
                .userName(request.getUserName())
                .build();

        findArticle.getComments().add(comment);

        findArticle.incrementCommentCount();

        return commentRepository.save(comment);
    }

    public Comment updateComment(Long commentId, UpdateCommentRequest request) {
        if (commentId == null) {
            throw new IllegalArgumentException("The given id must not be null!!");
        }
        Comment comment = findCommentById(commentId);
        comment.updateComment(request.getContent());
        return commentRepository.save(comment);

    }
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public Comment findCommentById(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("not found: " + commentId));
    }
}
