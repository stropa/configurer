package ai.itworks.configurer;

import ai.itworks.configurer.schema.SchemaGenerationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
@Service
public class Configurer implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(Configurer.class);

    public static final ObjectMapper mapper = new ObjectMapper();


    private List<Object> configurations = new ArrayList<>();

    Properties source = new Properties();

    @Autowired
    private Environment environment;

    @Autowired
    private SchemaGenerationService schemaGenerationService;

    public List<String> listSchemas() {
        return configurations.stream().map(Object::getClass).map(schemaGenerationService::generateSchema).collect(Collectors.toList());
    }

    public List<String> listConfigs() {

        return configurations.stream().map(c -> {
            try {
                return mapper.writeValueAsString(c);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

    }

    public void updateConfig(String configJson, int index) {
        Class<?> aClass = configurations.get(index).getClass();
        try {
            Object updatedConfig = mapper.readValue(configJson, aClass);
            log.debug("Updated config: " + updatedConfig.getClass().getName() + " : " + mapper.writeValueAsString(updatedConfig));
            // TODO: apply config update

            for (Field field : aClass.getDeclaredFields()) {
                Annotation[] annotations = field.getDeclaredAnnotations();
                Arrays.stream(annotations).filter(a -> a.annotationType().getName().equals(Value.class.getName())).findFirst().ifPresent(v -> {
                    Optional.of(((Value) v).value()).ifPresent(pfn -> {
                        try {
                            field.setAccessible(true);
                            source.setProperty(StringUtils.substringBetween(pfn, "{", "}"), field.get(updatedConfig).toString()); // TODO: formatting
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MutablePropertySources propertySources = ((AbstractEnvironment) environment).getPropertySources();
        propertySources.addFirst(
                new PropertiesPropertySource("configurer_runtime", source)
        );
    }
}
