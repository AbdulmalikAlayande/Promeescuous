package africa.semicolon.promeescuous.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class AppConfig {
    @Value("${mail.api.key}")
    private String mailApiKey;
    @Getter
    @Value("${app.dev.testtoken}")
    private String testToken;
    
   @Bean
    public String getMailApiKey(){
        return mailApiKey;
    }
    
    @Bean
    public AppConfig getAppConfig(){
       return new AppConfig();
    }
    
}
