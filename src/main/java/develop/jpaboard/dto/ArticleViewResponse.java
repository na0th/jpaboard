package develop.jpaboard.dto;


import develop.jpaboard.domain.Article;
import develop.jpaboard.domain.Comment;
import develop.jpaboard.domain.File;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class ArticleViewResponse {
    private Long id;
    private String title;
    private String content;

    private Long viewCount;

    private Long commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Comment> comments;

    private List<File> files;
    public ArticleViewResponse(Article article) {
        this.id = article.getId();
        this.title= article.getTitle();
        this.content= article.getContent();
        this.viewCount = article.getViewCount();
        this.commentCount = article.getCommentCount();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.comments = article.getComments();
        this.files = article.getFiles();
    }

}
