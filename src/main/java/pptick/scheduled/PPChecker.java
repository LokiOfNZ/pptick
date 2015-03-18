package pptick.scheduled;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import pptick.domain.Tick;
import pptick.domain.TickList;
import pptick.domain.mongo.TickListMongoRepo;
import pptick.domain.mongo.TickMongoRepo;

@Component
public class PPChecker {
	
	@Value("#{'${pp.properties}'.split(',')}") 
	private List<String> properties;
	
	@Autowired
	private TickMongoRepo tickMongoRepo;
	
	@Autowired
	private TickListMongoRepo tickListMongoRepo;
	
	private boolean run = true;

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
