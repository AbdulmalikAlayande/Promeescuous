package africa.semicolon.promeescuous.dtos.responses;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ApiResponse <T>{
	
	private T data;
}
