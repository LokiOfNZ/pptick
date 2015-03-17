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

import pptick.domain.Tick;
import pptick.domain.mongo.TickMongoRepo;
import pptick.scheduled.PPChecker;
 
@Controller
class HomeController {
	
	@Value("#{'${pp.properties}'.split(',')}") 
	private List<String> properties;
	
	@Autowired
	private TickMongoRepo tickMongoRepo;
	
	@Autowired
	private PPChecker ppChecker;
 
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

    @RequestMapping("/property/{propertyId}")
    public ModelAndView view(@PathVariable String propertyId, @RequestParam(required=false) Integer page) {
    	ModelAndView mav = new ModelAndView("property");
    	mav.addObject("propertyId", propertyId);
    	
    	Page<Tick> ticks = tickMongoRepo.findByPropertyId(propertyId, new PageRequest(page == null ? 0 : page, 20, Direction.ASC, "date", "askPrice"));
    	mav.addObject("ticks", ticks);
    	
    	return mav;
    }
}
