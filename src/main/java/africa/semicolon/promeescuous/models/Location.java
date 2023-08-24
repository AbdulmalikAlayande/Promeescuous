package africa.semicolon.promeescuous.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Location {
	
	private String country;
	private String state;
	
	public static Location of(String country, String state){
		return Location.builder()
				       .country(country)
				       .state(state)
				       .build();
	}
}
