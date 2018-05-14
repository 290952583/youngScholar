package youngScholar.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "pingxx")
public class PingxxProperties
{
	private String apiKey;
	private String appId;
	private String privateKey;
	private String publicKey;
	private String subject;
	private String body;
	private String description;
}
