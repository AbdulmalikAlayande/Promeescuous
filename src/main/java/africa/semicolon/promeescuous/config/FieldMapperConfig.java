package africa.semicolon.promeescuous.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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
	@Bean
	public ObjectMapper getObjectMapper(){
		return new ObjectMapper();
	}
}
