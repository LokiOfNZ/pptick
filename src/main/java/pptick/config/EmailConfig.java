package pptick.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sendgrid.SendGrid;

@Configuration
public class EmailConfig {
	
	@Value("${sendgrid_username}") 
	private String sendgrid_username;
	
	@Value("${sendgrid_password}")
	private String sendgrid_password;
	
	@Bean
	public SendGrid sendGrid() {
		return new SendGrid(sendgrid_username, sendgrid_password);
	}

}
