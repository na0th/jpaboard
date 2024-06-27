package develop.jpaboard.dto;

import develop.jpaboard.domain.Article;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateCommentRequest {

    private Long id;
    private String content;

}
