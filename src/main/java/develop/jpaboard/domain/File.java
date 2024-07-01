package develop.jpaboard.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Base64;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_contentType")
    private String contentType;
    @Column(name = "file_size")
    private Long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Lob
    @Column(name = "file_data")
    private byte[] data;

    public String getBase64Data() {
        return Base64.getEncoder().encodeToString(this.data);
    }

    // 비즈니스 메서드: 파일 정보 업데이트
    public void updateFileInfo(String fileName, String contentType, byte[] data, long size) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
        this.size = size;
    }

}
