package ai.itworks.configurer.web;

import ai.itworks.configurer.example.ConfigPropertiesExample;
import ai.itworks.configurer.schema.SchemaGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configurer")
public class OwnSettingsController {

    @Autowired
    private SchemaGenerationService schemaGenerationService;


    @GetMapping("/settings")
    public String getSettings() {
        return schemaGenerationService.generateSchema(ConfigPropertiesExample.class);
    }

}
