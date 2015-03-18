package pptick.domain;

import java.util.Date;
import java.util.List;

public class TickList {

	private String id;
	private String propertyId;
	private Date date;
	private List<Tick> ticks;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Tick> getTicks() {
		return ticks;
	}

	public void setTicks(List<Tick> ticks) {
		this.ticks = ticks;
	}

	@Override
	public String toString() {
		return "[" + propertyId + " / " + date + "] " + ticks.get(0).getAskPrice() + " = " + ticks.get(0).getUnits() + " (" + ticks.get(1).getAskPrice() + " = " + ticks.get(1).getUnits() + ", " + ticks.get(2).getAskPrice() + " = " + ticks.get(2).getUnits() + ")";
	}

}
