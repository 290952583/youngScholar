package youngScholar.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "aliyun.taobao")
public class TaobaoProperties
{
    private String url;
    private String appKey;
    private String secret;
    private String iconUrl;
    private String password;
}
