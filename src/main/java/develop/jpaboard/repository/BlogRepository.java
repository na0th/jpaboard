package develop.jpaboard.repository;

import develop.jpaboard.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article,Long> {
    Page<Article> findAll(Pageable pageable);
}
