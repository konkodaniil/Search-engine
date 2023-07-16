package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.LemmaEntity;
import searchengine.model.SiteEntity;

@Repository
public interface LemmaRepository extends JpaRepository<LemmaEntity, Integer> {
    LemmaEntity findByLemmaAndSiteEntityId(String lemma, int siteEntityId);

    int countLemmasBySiteEntity(SiteEntity siteEntity);
}
