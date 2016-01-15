package pptick.domain;

public class PropertySummary {
	
	private SecondaryMarket secondaryMarket;

	public SecondaryMarket getSecondaryMarket() {
		return secondaryMarket;
	}
	
	public void setSecondaryMarket(SecondaryMarket secondaryMarket) {
		this.secondaryMarket = secondaryMarket;
	}
	
	public class SecondaryMarket {
		private String propertySymbol;
		private Integer offerCount;
		private Integer totalUnits;
		private Double minPrice;
		private Double maxPrice;
		private Double averagePrice;
		public String getPropertySymbol() {
			return propertySymbol;
		}
		public void setPropertySymbol(String propertySymbol) {
			this.propertySymbol = propertySymbol;
		}
		public Integer getOfferCount() {
			return offerCount;
		}
		public void setOfferCount(Integer offerCount) {
			this.offerCount = offerCount;
		}
		public Integer getTotalUnits() {
			return totalUnits;
		}
		public void setTotalUnits(Integer totalUnits) {
			this.totalUnits = totalUnits;
		}
		public Double getMinPrice() {
			return minPrice;
		}
		public void setMinPrice(Double minPrice) {
			this.minPrice = minPrice;
		}
		public Double getMaxPrice() {
			return maxPrice;
		}
		public void setMaxPrice(Double maxPrice) {
			this.maxPrice = maxPrice;
		}
		public Double getAveragePrice() {
			return averagePrice;
		}
		public void setAveragePrice(Double averagePrice) {
			this.averagePrice = averagePrice;
		}
	}
}
