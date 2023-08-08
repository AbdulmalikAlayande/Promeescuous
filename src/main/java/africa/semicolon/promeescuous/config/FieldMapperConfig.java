package africa.semicolon.promeescuous.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class FieldMapperConfig {
	
	@Bean
	public ModelMapper getMapper(){
		return new ModelMapper();
	}
}
