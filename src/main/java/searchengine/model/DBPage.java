package searchengine.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Index;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Getter
@Setter
//@Table(name = "page", indexes = @Index(columnList = "path"))

@Table(name = "page", indexes = {@Index(name = "path_list", columnList = "path")})
@NoArgsConstructor
public class DBPage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id", referencedColumnName = "id")
    private DBSite siteId;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private int code;

    @Column(length = 1677721500, columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "pageId", cascade = CascadeType.ALL)
    private List<searchengine.model.Index> indexList = new ArrayList<>();


//    public DBPage(DBSite siteId, String path, int code, String content) {
//        this.siteId = siteId;
//        this.path = path;
//        this.code = code;
//        this.content = content;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DBPage dbPage = (DBPage) o;
        return code == dbPage.code && Objects.equals(id, dbPage.id) && Objects.equals(siteId, dbPage.siteId) && Objects.equals(path, dbPage.path) && Objects.equals(content, dbPage.content) && Objects.equals(indexList, dbPage.indexList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, siteId, path, code, content, indexList);
    }

    @Override
    public String toString() {
        return "DBPage{" +
                "id=" + id +
                ", DBSite=" + siteId +
                ", path='" + path + '\'' +
                ", code=" + code +
                ", content='" + content + '\'' +
                ", indexList=" + indexList +
                '}';
    }
}

//    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Index> index = new ArrayList<>();