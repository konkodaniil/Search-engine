package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import searchengine.model.DBSite;
import searchengine.model.Lemma;

import java.util.List;

public interface LemmaRepository extends JpaRepository<Lemma, Long> {
    long countBySitePageId(DBSite DBSite);

    List<Lemma> findBySitePageId(DBSite DBSite);
    @Query(value = "SELECT l.* FROM Lemma l WHERE l.lemma IN :lemmas AND l.site_id = :site", nativeQuery = true)
    List<Lemma> findLemmaListBySite(@Param("lemmas") List<String> lemmaList,
                                    @Param("site") DBSite DBSite);

    @Query(value = "SELECT l.* FROM Lemma l WHERE l.lemma = :lemma ORDER BY frequency ASC", nativeQuery = true)
    List<Lemma> findByLemma(@Param("lemma") String lemma);
}
