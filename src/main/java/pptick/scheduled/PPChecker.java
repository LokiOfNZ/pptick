package pptick.scheduled;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import pptick.domain.PropertySummary;
import pptick.domain.PropertySummary.SecondaryMarket;
import pptick.service.PropertyService;

@Component
public class PPChecker {

	@Autowired
	private PropertyService propertyService;
	
//	@Autowired
//	private SendGrid sendGrid;
//	
//	@Value("${ppcheck.email.defaultTo}")
//	private String emailTo;
//	
//	@Value("${ppcheck.email.defaultFrom}")
//	private String emailFrom;
	
	private boolean run = true;
	
	private Map<String, Double> lastPriceMap = new HashMap<String, Double>();

	@Scheduled(initialDelay = 0, fixedDelay = 900000)
	public void doSomething() {
		if(run) {
			Date date = new Date();
	        RestTemplate restTemplate = new RestTemplate();
	    	List<String> properties = propertyService.getProperties();
	        for(String prop : properties) {
		        PropertySummary property = restTemplate.getForObject("https://propertypartner.co/properties/" + prop + "/summary", PropertySummary.class);
		        
		        SecondaryMarket secondaryMarket = property.getSecondaryMarket();
		        if(secondaryMarket != null) {
			        Double thisPrice = secondaryMarket.getMinPrice();
			        Double lastPrice = lastPriceMap.get(prop);
			        if(lastPrice != null && thisPrice < lastPrice) {
	//		    		Email email = new Email();
	//		    		email.addTo(emailTo);
	//		    		email.setFrom(emailFrom);
	//		    		email.setSubject("Price drop alert! [" + prop + "]");
	//		    		email.setText("Price drop on property " + prop + " = " + lastPrice + " -> " + thisPrice);
	//		    		try {
	//		    			SendGrid.Response response = sendGrid.send(email);
	//		    			System.out.println("Price drop alert for property " + prop + " sent: " + response.getCode() + " / " + response.getMessage());
	//		    		} catch (SendGridException e) {
	//		    			e.printStackTrace();
	//		    		}
			        }
			        lastPriceMap.put(prop, property.getSecondaryMarket().getMinPrice());
			        
			        System.out.println("Processed property " + prop + " at " + date + " minPrice = " + property.getSecondaryMarket().getMinPrice());
		        }
	        }
		}
	}

	public boolean getRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

}
