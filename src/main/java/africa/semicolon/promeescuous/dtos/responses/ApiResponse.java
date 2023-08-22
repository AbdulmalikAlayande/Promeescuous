package africa.semicolon.promeescuous.dtos.responses;

import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@Builder
@ToString
public class ApiResponse <T>{
	
	private T data;
}
