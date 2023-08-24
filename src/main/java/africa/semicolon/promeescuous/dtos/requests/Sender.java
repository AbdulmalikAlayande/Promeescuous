package africa.semicolon.promeescuous.dtos.requests;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Sender {
    @JsonProperty("name")
    private String name;
    @NonNull
    @JsonProperty("email")
    private String email;
}
