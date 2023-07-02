package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.model.DBPage;
import searchengine.model.DBSite;
import searchengine.model.Lemma;

import java.util.Collection;
import java.util.List;


@Repository
public interface PageRepository extends JpaRepository<DBPage, Long> {
    long countBySiteId(DBSite site_id);
    Iterable<DBPage> findBySiteId(DBSite site_id);
    @Query(value = "SELECT * FROM page p JOIN index_table i ON p.id = i.page_id WHERE i.lemma_id IN :lemmas", nativeQuery = true)
    List<DBPage> findByLemmaList(@Param("lemmas") Collection<Lemma> lemmaListId);
}
