package pptick.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

	public List<String> getProperties() {
		List<String> properties = new ArrayList<>();
		
		try {
			Document doc = Jsoup.connect("https://propertypartner.co/s/uk").userAgent("Mozilla").get();
			
			Elements propertyLinks = doc.select(".gCol12.p0.box.link");
			for(Element link : propertyLinks) {
				String sLink = link.attr("onclick");
				sLink = sLink.replace("window.location ='/properties/", "");
				sLink = sLink.substring(0, sLink.indexOf("/#"));
				properties.add(sLink);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return properties;
	}
	
}
