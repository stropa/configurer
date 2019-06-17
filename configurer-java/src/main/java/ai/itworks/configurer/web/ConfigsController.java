package ai.itworks.configurer.web;

import ai.itworks.configurer.Configurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/configurer")
public class ConfigsController {


    @Autowired
    private Configurer configurer;


    @GetMapping("/list/schemas")
    public List<String> listSchemas() {
        return configurer.listSchemas();
    }

    @GetMapping("/list/configs")
    public List<String> listConfigs() {
        return configurer.listConfigs();
    }

    @PostMapping(value = "/submit")
    public void submit(@RequestBody String submitted, HttpServletRequest request) {
        configurer.updateConfig(submitted, Integer.valueOf(request.getParameter("id")));
    }


}
