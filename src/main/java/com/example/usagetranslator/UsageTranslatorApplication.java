package com.example.usagetranslator;

import com.example.usagetranslator.service.UsageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UsageTranslatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsageTranslatorApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(UsageService usageService) {
		return args -> {
			usageService.processUsage("/data/Sample_Report.csv", "/data/typemap.json");
		};
	}
}
