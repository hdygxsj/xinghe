package com.gf.career.space;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gf.career.**")
public class CareerSpaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CareerSpaceApplication.class, args);
	}

}