package com.programisto.devis_rapide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync(proxyTargetClass = true)
public class DevisRapideApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevisRapideApplication.class, args);
	}

}
