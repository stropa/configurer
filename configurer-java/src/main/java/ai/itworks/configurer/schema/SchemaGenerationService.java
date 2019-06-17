package ai.itworks.configurer.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SchemaGenerationService {

    private ObjectMapper mapper = new ObjectMapper();
    private JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);


    public String generateSchema(Class c) {
        String schemaJsonString = null;
        try {
            JsonSchema schema = schemaGen.generateSchema(c);
            schemaJsonString = mapper.writeValueAsString(schema);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.debug(schemaJsonString);
        return schemaJsonString;
    }

}
