package com.github.pistencup.mcqueen.sampleservera;

import com.github.pistencup.mcqueen.camera.EnableMcqueenCamera;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableMcqueenCamera
@EnableFeignClients
public class SampleServerAApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleServerAApplication.class, args);
	}

}

