package africa.semicolon.promeescuous.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@Getter
public class AppConfig {
    @Value("${mail.api.key}")
    private String mailApiKey;
    @Value("${app.dev.testtoken}")
    private String testToken;
    @Value("${cloud.api.key}")
    private String cloudApiKey;
    @Value("${cloud.api.name}")
    private String cloudApiName;
    @Value("${cloud.api.secret}")
    private String cloudApiSecret;
    
   @Bean
    public String getMailApiKey(){
        return mailApiKey;
    }
    
    @Bean
    public AppConfig getAppConfig(){
       return new AppConfig();
    }
    
}
