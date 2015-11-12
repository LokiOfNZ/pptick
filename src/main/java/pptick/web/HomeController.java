package pptick.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import pptick.scheduled.PPChecker;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGrid.Email;
import com.sendgrid.SendGridException;
 
@Controller
class HomeController {
	
	@Autowired
	private PPChecker ppChecker;
	
	@Autowired
	private SendGrid sendGrid;
	
	@Value("${ppcheck.email.defaultTo}")
	private String emailTo;
	
	@Value("${ppcheck.email.defaultFrom}")
	private String emailFrom;
 
    @RequestMapping("/")
    public ModelAndView index() {
    	ModelAndView mav = new ModelAndView("index");
    	mav.addObject("started", ppChecker.getRun());
        return mav;
    }
 
    @RequestMapping("/start")
    public View start() {
    	ppChecker.setRun(true);
        return new RedirectView("/");
    }
 
    @RequestMapping("/stop")
    public View stop() {
    	ppChecker.setRun(false);
        return new RedirectView("/");
    }
 
    @RequestMapping("/test-email")
    public View testEmail() {
		
		Email email = new Email();
		email.addTo(emailTo);
		email.setFrom(emailFrom);
		email.setSubject("Yay worked!");
		email.setText("Whoooops");
		try {
			SendGrid.Response response = sendGrid.send(email);
			System.out.println(response.getCode());
			System.out.println(response.getMessage());
		} catch (SendGridException e) {
			System.out.println(e);
		}
		
        return new RedirectView("/");
    }
}
