package develop.jpaboard.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "comments")
@Entity
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    Article article;

    @Builder
    public Comment(Article article,String content) {
        this.article = article;
        this.content = content;
    }
    // 비즈니스 메서드
    public void updateComment(String content) {
        this.content = content;
    }
}
