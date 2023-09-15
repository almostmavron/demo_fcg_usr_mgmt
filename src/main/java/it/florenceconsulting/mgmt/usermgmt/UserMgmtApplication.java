package it.florenceconsulting.mgmt.usermgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"it.florenceconsulting.mgmt.usermgmt"})
public class UserMgmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserMgmtApplication.class, args);
	}

}
