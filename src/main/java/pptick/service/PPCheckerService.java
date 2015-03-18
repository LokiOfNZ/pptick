package pptick.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import pptick.domain.Tick;
import pptick.domain.TickList;
import pptick.domain.mongo.TickListMongoRepo;
import pptick.domain.mongo.TickMongoRepo;

@Service
public class PPCheckerService {
	
	@Value("#{'${pp.properties}'.split(',')}") 
	private List<String> properties;
	
	@Autowired
	private TickMongoRepo tickMongoRepo;
	
	@Autowired
	private TickListMongoRepo tickListMongoRepo;
	
	
	public void processTicksToTickLists() {
		System.out.println("Running ticks to tickLists");

        for(String prop : properties) {
        	Page<Tick> page = tickMongoRepo.findByPropertyId(prop, new PageRequest(0, 100000000, Direction.DESC, "date"));
        	List<Tick> ticks = page.getContent();
		
        	Map<Date, TickList> map = new HashMap<>();
        	for(Tick tick : ticks) {
        		TickList list = map.get(tick.getDate());
        		if(list == null) {
        			list = new TickList();
        			list.setDate(tick.getDate());
        			list.setPropertyId(prop);
        			list.setTicks(new ArrayList<Tick>());
        			map.put(tick.getDate(), list);
        		}
        		list.getTicks().add(tick);
        	}
        	
        	for(Entry<Date, TickList> entry : map.entrySet()) {
        		TickList list = entry.getValue();
        		Collections.sort(list.getTicks(), new Comparator<Tick>() {

					@Override
					public int compare(Tick o1, Tick o2) {
						return o1.getAskPrice().compareTo(o2.getAskPrice());
					}
				});
        		tickListMongoRepo.insert(list);
        		System.out.println(list);
        	}
        }
        
		System.out.println("Finished ticks to tickLists");
	}
}
