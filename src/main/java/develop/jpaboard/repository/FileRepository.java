package develop.jpaboard.repository;

import develop.jpaboard.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface FileRepository extends JpaRepository<File,Long> {
}
