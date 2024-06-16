package develop.jpaboard.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "article_id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "title")
    private String title;

    @Builder
    public Article(String title, String content) {
        this.title = title;
        this.content = content;
    }
    // 비즈니스 메서드
    public void updateDetails(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
