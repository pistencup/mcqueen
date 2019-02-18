package com.github.pistencup.mcqueen.sampleservera;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("server-b")
public interface ServerBClient {

    @GetMapping("/sa")
    String sa();
}
