package util.models;

import java.io.Serializable;

public class TimeSeriesResponseModel implements Serializable{
	
	private String marketDataEventSetID;
	private String indexEventSetID;
	private String riskFreeAssetEventSetID;
	
	public TimeSeriesResponseModel() {}
	
	public TimeSeriesResponseModel(String marketDataEventSetID,
								   String indexEventSetID,
								   String riskFreeAssetEventSetID) {
		this.marketDataEventSetID = marketDataEventSetID;
		this.indexEventSetID = indexEventSetID;
		this.riskFreeAssetEventSetID = riskFreeAssetEventSetID;
	}

	public String getMarketDataEventSetID() {
		return marketDataEventSetID;
	}

	public void setMarketDataEventSetID(String marketDataEventSetID) {
		this.marketDataEventSetID = marketDataEventSetID;
	}

	public String getIndexEventSetID() {
		return indexEventSetID;
	}

	public void setIndexEventSetID(String indexEventSetID) {
		this.indexEventSetID = indexEventSetID;
	}

	public String getRiskFreeAssetEventSetID() {
		return riskFreeAssetEventSetID;
	}

	public void setRiskFreeAssetEventSetID(String riskFreeAssetEventSetID) {
		this.riskFreeAssetEventSetID = riskFreeAssetEventSetID;
	}
	
	
	
}
