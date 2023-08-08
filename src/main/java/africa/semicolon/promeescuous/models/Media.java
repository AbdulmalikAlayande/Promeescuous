package africa.semicolon.promeescuous.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    @ElementCollection
    @Enumerated(value = EnumType.STRING)
    private List<Reaction> reactions;
    @Column(unique = true, length = 300)
    private String url;
    @ManyToOne(cascade = CascadeType.DETACH)
    private User user;
}
