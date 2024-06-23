package develop.jpaboard.dto;

import develop.jpaboard.domain.Article;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddArticleRequest {
    private String title;
    private String content;
    private Long viewCount;

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .viewCount(viewCount)
                .build();
    }
}
