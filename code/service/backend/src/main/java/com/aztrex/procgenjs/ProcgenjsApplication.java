package com.aztrex.procgenjs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.aztrex.procgenjs"})
//@EnableJpaRepositories(basePackages = {"com.aztrex.procgenjs", "com.aztrex.common"})
//@EntityScan(basePackages = {"com.aztrex.procgenjs", "com.aztrex.common"})
public class ProcgenjsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcgenjsApplication.class, args);
	}

}
