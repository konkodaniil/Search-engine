package searchengine.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.DBSite;

@Repository
public interface SiteRepository extends JpaRepository<DBSite, Long> {

    @EntityGraph(attributePaths = "pages")
    DBSite findByUrl(String url);
}
