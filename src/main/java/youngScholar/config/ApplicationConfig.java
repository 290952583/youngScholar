package youngScholar.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({SmsProperties.class, PingxxProperties.class, TaobaoProperties.class})
public class ApplicationConfig
{
}
