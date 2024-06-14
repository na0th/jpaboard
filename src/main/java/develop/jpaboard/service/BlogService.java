package develop.jpaboard.service;

import develop.jpaboard.domain.Article;
import develop.jpaboard.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {
    private final BlogRepository blogRepository;

    public List<Article> findAll() {
        return blogRepository.findAll();
    }
}
