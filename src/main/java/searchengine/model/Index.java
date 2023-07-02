package searchengine.model;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Getter
@Setter
@Table(name = "index_table", indexes = {@javax.persistence.Index(
        name = "page_id_list", columnList = "page_id"),
        @javax.persistence.Index(name = "lemma_id_list", columnList = "lemma_id")})
@NoArgsConstructor
public class Index implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "page_id", referencedColumnName = "id")
    private DBPage pageId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lemma_id", referencedColumnName = "id")
    private Lemma lemma;

    @Column(name = "my_rank")
    private Float rank;

//    public Index(searchengine.model.DBPage pageId, Lemma lemma, Float rank) {
//        this.pageId = pageId;
//        this.lemma = lemma;
//        this.rank = rank;
//    }
}
