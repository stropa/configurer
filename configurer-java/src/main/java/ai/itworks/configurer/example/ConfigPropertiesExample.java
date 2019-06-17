package ai.itworks.configurer.example;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties
public class ConfigPropertiesExample {

    private String stringPropery;

    private int intProperty;

}
