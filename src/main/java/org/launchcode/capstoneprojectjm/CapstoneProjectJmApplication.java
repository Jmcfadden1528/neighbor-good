package org.launchcode.capstoneprojectjm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;

@SpringBootApplication
@ComponentScan({"org.launchcode.capstoneprojectjm"})
public class CapstoneProjectJmApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapstoneProjectJmApplication.class, args);
	}
}
