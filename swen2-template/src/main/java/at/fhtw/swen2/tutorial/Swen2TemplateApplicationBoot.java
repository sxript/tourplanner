package at.fhtw.swen2.tutorial;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Swen2TemplateApplicationBoot {
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	public static void main(String[] args) {
		Application.launch(Swen2TemplateApplication.class, args);
	}

}
