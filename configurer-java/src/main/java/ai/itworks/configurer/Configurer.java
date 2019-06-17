package ai.itworks.configurer;

import ai.itworks.configurer.schema.SchemaGenerationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Service
public class Configurer {

    private static final Logger log = LoggerFactory.getLogger(Configurer.class);

    public static final ObjectMapper mapper = new ObjectMapper();


    private List<Object> configurations = new ArrayList<>();

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

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
