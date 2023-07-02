package searchengine.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@Table(name = "lemma", indexes = {@javax.persistence.Index(name = "lemma_list", columnList = "lemma")})
@NoArgsConstructor
public class Lemma implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id", referencedColumnName = "id")
    private DBSite sitePageId;

    @Column(nullable = false)
    private String lemma;

    @Column(nullable = false)
    private int frequency;

    public Lemma(DBSite sitePageId, String lemma, int frequency) {
        this.sitePageId = sitePageId;
        this.lemma = lemma;
        this.frequency = frequency;
    }

    @OneToMany(mappedBy = "lemma", cascade = CascadeType.ALL)
    private List<Index> indexList = new ArrayList<>();
}
