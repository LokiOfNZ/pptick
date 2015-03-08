package pptick;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PPChecker {
	
	private Map<Date, Map<Double, Tick>> dateMap = new TreeMap<>();
	private List<String> tickSummaries = new ArrayList<>();
	
	private boolean run = true;

	@Scheduled(initialDelay = 0, fixedDelay = 900000)
	public void doSomething() {
		if(run) {
			Date date = new Date();
	        RestTemplate restTemplate = new RestTemplate();
	        List<Tick> ticks = Arrays.asList(restTemplate.getForObject("https://propertypartner.co/marketplace/secondary/UKCR03PH001/available", Tick[].class));
	        for(Tick tick : ticks) {
	        	tick.setDate(date);
	        	Map<Double, Tick> tickMap = new TreeMap<>();
	        	tickMap.put(tick.getAskPrice(), tick);
	        	dateMap.put(date, tickMap);
	        }
	        String tickSummary = "[" + date + "] " 
	        		+ ticks.get(0) + " " 
	        		+ ticks.get(1) + " " 
	        		+ ticks.get(2) + " " 
	        		+ ticks.get(3) + " " 
	        		+ ticks.get(4) + " " 
	        		+ ticks.get(5) + " " 
	        		+ ticks.get(6) + " " 
	        		+ ticks.get(7) + " " 
	        		+ ticks.get(8);
	        tickSummaries.add(tickSummary);
	        System.out.println(tickSummary);
		}
	}

	public boolean getRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	public Map<Date, Map<Double, Tick>> getDateMap() {
		return dateMap;
	}

	public void setDateMap(Map<Date, Map<Double, Tick>> dateMap) {
		this.dateMap = dateMap;
	}

	public List<String> getTickSummaries() {
		return tickSummaries;
	}

	public void setTickSummaries(List<String> tickSummaries) {
		this.tickSummaries = tickSummaries;
	}

}
