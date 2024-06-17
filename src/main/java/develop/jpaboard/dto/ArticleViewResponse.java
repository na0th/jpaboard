package develop.jpaboard.dto;


import develop.jpaboard.domain.Article;
import develop.jpaboard.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class ArticleViewResponse {
    private Long id;
    private String title;
    private String content;

    private List<Comment> comments;
    public ArticleViewResponse(Article article) {
        this.id = article.getId();
        this.title= article.getTitle();
        this.content= article.getContent();

        this.comments = article.getComments();
    }

}
