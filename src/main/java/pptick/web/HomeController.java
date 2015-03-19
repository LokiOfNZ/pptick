package pptick.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import com.sendgrid.SendGrid.Email;

import pptick.domain.TickList;
import pptick.domain.mongo.TickListMongoRepo;
import pptick.domain.mongo.TickMongoRepo;
import pptick.scheduled.PPChecker;
import pptick.service.PPCheckerService;
 
@Controller
class HomeController {
	
	@Value("#{'${pp.properties}'.split(',')}") 
	private List<String> properties;
	
	@Autowired
	private TickMongoRepo tickMongoRepo;
	
	@Autowired
	private TickListMongoRepo tickListMongoRepo;
	
	@Autowired
	private PPChecker ppChecker;
	
	@Autowired
	private PPCheckerService ppCheckerService;
	
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
    	mav.addObject("properties", properties);
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
 
    @RequestMapping("/process-ticks")
    public View processTicks() {
    	ppCheckerService.processTicksToTickLists();
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

    @RequestMapping("/property/{propertyId}")
    public ModelAndView view(@PathVariable String propertyId, @RequestParam(required=false) Integer page) {
    	ModelAndView mav = new ModelAndView("property");
    	mav.addObject("propertyId", propertyId);
    	
    	Page<TickList> tickLists = tickListMongoRepo.findByPropertyId(propertyId, new PageRequest(page == null ? 0 : page, 100, Direction.DESC, "date"));
    	mav.addObject("tickLists", tickLists);
    	
    	return mav;
    }
}
