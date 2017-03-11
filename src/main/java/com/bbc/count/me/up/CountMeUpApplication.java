package com.bbc.count.me.up;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CountMeUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(CountMeUpApplication.class, args);
	}

}
