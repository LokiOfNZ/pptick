package pptick;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
 
@Controller
class HomeController {
	
	@Autowired
	private PPChecker ppChecker;
 
    @RequestMapping("/")
    public ModelAndView index() {
    	ModelAndView mav = new ModelAndView("index");
    	mav.addObject("started", ppChecker.getRun());
    	mav.addObject("tickSummaries", ppChecker.getTickSummaries());
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
}
