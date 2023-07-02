package searchengine.model;

import lombok.Data;
import lombok.Value;
import org.springframework.context.annotation.Bean;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "site")
public class DBSite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "status_time")
    private LocalDateTime statusTime = LocalDateTime.now();

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String url;

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @OneToMany(mappedBy = "siteId", cascade = CascadeType.ALL)
    private List<DBPage> pages = new ArrayList<>();

    @OneToMany(mappedBy = "sitePageId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lemma> lemmas = new ArrayList<>();
}




