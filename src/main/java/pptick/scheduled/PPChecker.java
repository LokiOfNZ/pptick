package pptick.scheduled;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import pptick.domain.Tick;
import pptick.domain.TickList;
import pptick.domain.mongo.TickListMongoRepo;
import pptick.domain.mongo.TickMongoRepo;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import com.sendgrid.SendGrid.Email;

@Component
public class PPChecker {
	
	@Value("#{'${pp.properties}'.split(',')}") 
	private List<String> properties;
	
	@Autowired
	private TickMongoRepo tickMongoRepo;
	
	@Autowired
	private TickListMongoRepo tickListMongoRepo;
	
	@Autowired
	private SendGrid sendGrid;
	
	@Value("${ppcheck.email.defaultTo}")
	private String emailTo;
	
	@Value("${ppcheck.email.defaultFrom}")
	private String emailFrom;
	
	private boolean run = true;
	
	private Map<String, Double> lastPriceMap = new HashMap<String, Double>();

	@Scheduled(initialDelay = 0, fixedDelay = 900000)
	public void doSomething() {
		if(run) {
			Date date = new Date();
	        RestTemplate restTemplate = new RestTemplate();
	        for(String prop : properties) {
		        List<Tick> ticks = Arrays.asList(restTemplate.getForObject("https://propertypartner.co/marketplace/secondary/" + prop + "/available", Tick[].class));
		        for(Tick tick : ticks) {
		        	tick.setPropertyId(prop);
		        	tick.setDate(date);
		        	tickMongoRepo.insert(tick);
		        }
		        TickList tickList = new TickList();
		        tickList.setPropertyId(prop);
		        tickList.setDate(date);
		        tickList.setTicks(ticks);
	        	tickListMongoRepo.insert(tickList);
		        
		        Double thisPrice = tickList.getTicks().get(0).getAskPrice();
		        Double lastPrice = lastPriceMap.get(prop);
		        if(lastPrice != null && thisPrice < lastPrice - 1) {
		    		Email email = new Email();
		    		email.addTo(emailTo);
		    		email.setFrom(emailFrom);
		    		email.setSubject("Price drop alert! [" + prop + "]");
		    		email.setText("Price drop on property " + prop + " = " + lastPrice + " -> " + thisPrice);
		    		try {
		    			SendGrid.Response response = sendGrid.send(email);
		    			System.out.println("Price drop alert for property " + prop + " sent: " + response.getCode() + " / " + response.getMessage());
		    		} catch (SendGridException e) {
		    			e.printStackTrace();
		    		}
		        }
		        lastPriceMap.put(prop, tickList.getTicks().get(0).getAskPrice());
		        
		        System.out.println("Processed property " + prop + " at " + date);
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
