package util.models;

import java.io.Serializable;

import component.trthimport.TRTHImportCacheServiceStub.DateRange;
import component.trthimport.TRTHImportCacheServiceStub.TimeRange;

public class TRTHImportModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String messageType;
    private String marketDataRIC;
    private String indexRIC;
    private String riskRIC;
    private DateRange dateRange;
    private TimeRange timeRange;
    private String useGMT;
    private String useCorporateActions;
    
    public TRTHImportModel() {}

    public TRTHImportModel(String messageType, String marketDataRIC,
        String indexRIC, String riskRIC, DateRange dateRange,
        TimeRange timeRange, String useGMT, String useCorporateActions) {
      super();
      this.messageType = messageType;
      this.marketDataRIC = marketDataRIC;
      this.indexRIC = indexRIC;
      this.riskRIC = riskRIC;
      this.dateRange = dateRange;
      this.timeRange = timeRange;
      this.useGMT = useGMT;
      this.useCorporateActions = useCorporateActions;
    }

    public String getMessageType() {
      return messageType;
    }

    public void setMessageType(String messageType) {
      this.messageType = messageType;
    }

    public String getMarketDataRIC() {
      return marketDataRIC;
    }

    public void setMarketDataRIC(String marketDataRIC) {
      this.marketDataRIC = marketDataRIC;
    }

    public String getIndexRIC() {
      return indexRIC;
    }

    public void setIndexRIC(String indexRIC) {
      this.indexRIC = indexRIC;
    }

    public String getRiskRIC() {
      return riskRIC;
    }

    public void setRiskRIC(String riskRIC) {
      this.riskRIC = riskRIC;
    }

    public DateRange getDateRange() {
      return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
      this.dateRange = dateRange;
    }

    public TimeRange getTimeRange() {
      return timeRange;
    }

    public void setTimeRange(TimeRange timeRange) {
      this.timeRange = timeRange;
    }

    public String getUseGMT() {
      return useGMT;
    }

    public void setUseGMT(String useGMT) {
      this.useGMT = useGMT;
    }

    public String getUseCorporateActions() {
      return useCorporateActions;
    }

    public void setUseCorporateActions(String useCorporateActions) {
      this.useCorporateActions = useCorporateActions;
    }
    
    
    
}
