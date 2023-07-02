package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.model.Lemma;
import searchengine.model.DBPage;
import searchengine.model.Index;

import java.util.List;

@Repository
public interface IndexRepository extends JpaRepository<Index, Long> {
    @Query(value = "SELECT i.* FROM index_table i WHERE i.lemma_id IN :lemmas AND i.page_id IN :pages", nativeQuery = true)
    List<Index> findByPagesAndLemmas(@Param("lemmas") List<Lemma> lemmaListId,
                                           @Param("pages") List<DBPage> pageListId);

    List<Index> findByLemmaId (long lemmaId);
    List<Index> findByPageId (long pageId);
    Index findByLemmaIdAndPageId (long lemmaId, long pageId);

}
