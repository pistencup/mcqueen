package com.github.pistencup.mcqueen.sampleserverb;

import com.github.pistencup.mcqueen.camera.EnableMcqueenCamera;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@SpringBootApplication
@EnableMcqueenCamera
@EnableFeignClients
@RestController
public class SampleServerBApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleServerBApplication.class, args);
    }

    //Logger logger = LoggerFactory.getLogger("serverb");

    @RequestMapping("/sa")
    public String sampleAction(String a, HttpServletResponse response){
        //logger.warn(JSON.toJSONString(CloudContext.getCurrent()));
        //response.addCookie(new Cookie("aaa", "bbb"));

        return "i am server-b";
    }
}

