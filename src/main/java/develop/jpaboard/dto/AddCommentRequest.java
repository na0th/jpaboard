package develop.jpaboard.dto;

import develop.jpaboard.domain.Article;
import develop.jpaboard.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class AddCommentRequest {
    private Long articleId;

    private String content;

    public Comment toEntity(Article article) {
        return Comment.builder()
                .article(article)
                .content(content)
                .build();
    }
}
