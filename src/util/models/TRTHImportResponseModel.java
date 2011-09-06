package util.models;

public class TRTHImportResponseModel {
    
    private String marketDataEventSetID;
    private String indexEventSetID;
    private String riskFreeAssetEventSetID;
    private String status;
    
    public TRTHImportResponseModel() {}
    
    public TRTHImportResponseModel(String marketDataEventSetID,
                                    String indexEventSetID,
                                    String riskFreeAssetEventSetID,
                                    String status) {
        this.marketDataEventSetID = marketDataEventSetID;
        this.indexEventSetID = indexEventSetID;
        this.riskFreeAssetEventSetID = riskFreeAssetEventSetID;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
