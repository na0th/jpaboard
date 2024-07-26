package develop.jpaboard.dto;

import develop.jpaboard.domain.Article;
import develop.jpaboard.domain.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDto {

    private Long id;
    private String title;
    private String content;

    private Long viewCount;

    private Long commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentDto> comments;

    private List<FileDto> files;

    public ArticleDto(Article article) {
        this.id = article.getId();
        this.title= article.getTitle();
        this.content= article.getContent();
        this.viewCount = article.getViewCount();
        this.commentCount = article.getCommentCount();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.comments = article.getComments().stream()
                .map(CommentDto::new)
                .toList();
        this.files = article.getFiles().stream()
                .map(file -> new FileDto(
                        file.getId(),
                        file.getFileName(),
                        file.getContentType(),
                        file.getSize(),
                        file.getData()
                ))
                .toList();
    }
}
