package pptick.domain;

import java.util.Date;

public class Tick {
	
	private String id;
	private String propertyId;
	private Double askPrice;
	private Integer units;
	private Date date;

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

	public Double getAskPrice() {
		return askPrice;
	}

	public void setAskPrice(Double askPrice) {
		this.askPrice = askPrice;
	}

	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "[" + propertyId + " / " + date + "] " + askPrice + " = " + units;
	}
	
}
