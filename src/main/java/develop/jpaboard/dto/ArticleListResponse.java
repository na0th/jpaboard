package develop.jpaboard.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleListResponse {
    private List<ArticleListViewResponse> articles;
    private int currentPage;
    private int size;
    private int totalPages;
    private long totalItems;

}
