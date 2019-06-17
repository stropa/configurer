package ai.itworks.configurer.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class GenerateSchemaExample {

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
// configure mapper, if necessary, then create schema generator
        JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
        JsonSchema schema = schemaGen.generateSchema(ConfigPropertiesExample.class);

        log.debug(mapper.writeValueAsString(schema));
    }

}
