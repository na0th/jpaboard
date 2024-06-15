package develop.jpaboard.dto;


import develop.jpaboard.domain.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
public class ArticleViewResponse {
    private Long id;
    private String title;
    private String content;

    public ArticleViewResponse(Article article) {
        this.id = article.getId();
        this.title= article.getTitle();
        this.content= article.getContent();

    }

}
