package pptick.scheduled;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import pptick.domain.Tick;
import pptick.domain.mongo.TickMongoRepo;

@Component
public class PPChecker {
	
	@Value("#{'${pp.properties}'.split(',')}") 
	private List<String> properties;
	
	@Autowired
	private TickMongoRepo tickMongoRepo;
	
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
		        	Map<Double, Tick> tickMap = new TreeMap<>();
		        	tickMap.put(tick.getAskPrice(), tick);
		        	tickMongoRepo.insert(tick);
		        }
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
