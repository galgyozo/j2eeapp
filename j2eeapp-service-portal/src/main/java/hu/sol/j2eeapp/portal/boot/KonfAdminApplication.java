package hu.sol.j2eeapp.portal.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@SpringBootConfiguration
@ComponentScan(basePackages = { "hu.sol.j2eeapp" })
public class KonfAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(KonfAdminApplication.class, args);
	}
}
