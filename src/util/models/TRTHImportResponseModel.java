package util.models;

import java.io.Serializable;

import component.trthimport.TRTHImportCacheServiceStub.TimeRange;

public class TRTHImportResponseModel implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private String marketDataEventSetID;
    private boolean marketDataStatus;
    private String indexEventSetID;
    private boolean indexStatus;
    private String riskFreeAssetEventSetID;
    private boolean riskFreeAssetStatus;

    private TimeRange timeRange;
    private boolean status = true;
    
    public TRTHImportResponseModel() {}
    
    public TRTHImportResponseModel(String marketDataEventSetID,
                                    String indexEventSetID,
                                    String riskFreeAssetEventSetID
                                    ) {
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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }
    
    public boolean getMarketDataStatus() {
      return marketDataStatus;
    }

    public void setMarketDataStatus(boolean marketDataStatus) {
      this.marketDataStatus = marketDataStatus;
    }

    public boolean getIndexStatus() {
      return indexStatus;
    }

    public void setIndexStatus(boolean indexStatus) {
      this.indexStatus = indexStatus;
    }

    public boolean getRiskFreeAssetStatus() {
      return riskFreeAssetStatus;
    }

    public void setRiskFreeAssetStatus(boolean riskFreeAssetStatus) {
      this.riskFreeAssetStatus = riskFreeAssetStatus;
    }
    
}
