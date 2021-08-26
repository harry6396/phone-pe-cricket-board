package com.phonepe.cricketscorecard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories({"com.phonepe.cricketscorecard"})
@EnableAutoConfiguration
public class CricketscorecardApplication {

	public static void main(String[] args) {
		SpringApplication.run(CricketscorecardApplication.class, args);
	}

}
