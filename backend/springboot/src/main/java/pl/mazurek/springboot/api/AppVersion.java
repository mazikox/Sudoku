package pl.mazurek.springboot.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppVersion {
    @Value("${build.version}")
    private String buildVersion;

    @GetMapping("/appversion")
    public String getVersion(){
        return buildVersion;
    }


}
