package develop.jpaboard.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.boot.model.relational.Sequence;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor
public class Article {

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "article_id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "title")
    private String title;

    @Column(name = "view")
    @ColumnDefault("0") //default 0
    private Long viewCount;

    @Column(name = "comment_count")
    @ColumnDefault("0") //default 0
    private Long commentCount;
    @PrePersist
    protected void onCreate() {
        if (this.viewCount == null) {
            this.viewCount = 0L;
        }
        if (this.commentCount == null) {
            this.commentCount = 0L;
        }
    }

    @OneToMany(mappedBy = "article",cascade = CascadeType.ALL)
    private List<Comment> comments ;

    @Builder
    public Article(String title, String content, Long viewCount) {
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
    }
    // 비즈니스 메서드
    public void updateDetails(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 조회수 증가 메서드
    public void incrementViewCount() {
        if (this.viewCount == null) {
            this.viewCount = 0L;
        }
        this.viewCount += 1;
    }
    //댓글 수 증가 메서드
    public void incrementCommentCount() {
        if (this.commentCount == null) {
            this.commentCount = 0L;
        }
        this.commentCount += 1;
    }

}
