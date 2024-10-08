package develop.jpaboard.dto;

import develop.jpaboard.domain.Article;
import lombok.Getter;


@Getter
public class ArticleListViewResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final Long viewCount;
    private final Long commentCount;

    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.viewCount = article.getViewCount();
        this.commentCount = article.getCommentCount();

    }


}
