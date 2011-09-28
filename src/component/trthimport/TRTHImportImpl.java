package component.trthimport;

import java.rmi.RemoteException;
import java.util.UUID;

import org.apache.axis2.AxisFault;
import org.codehaus.jettison.json.JSONObject;

import component.trthimport.TRTHImportCacheServiceStub.TRTHImportCache;
import component.trthimport.TRTHImportCacheServiceStub.DateRange;
import component.trthimport.TRTHImportCacheServiceStub.TRTHImportCacheResponse;
import component.trthimport.TRTHImportCacheServiceStub.TimeRange;

import util.exceptions.ComputingServiceException;
import util.models.TRTHImportModel;
import util.models.TRTHImportResponseModel;

public class TRTHImportImpl implements TRTHImport {

  public static final String wsURL = "http://soc-server2.cse.unsw.edu.au:14080/axis2/services/TRTHImportCacheService";

  private TRTHImportModel request;
  private TRTHImportResponseModel response;
  private TRTHImportCacheServiceStub stub;
  private TRTHImportCache param;

  @Override
  public TRTHImportResponseModel ImportMarketData(TRTHImportModel request)
      throws ComputingServiceException {
    this.request = request;
    return importMarketDataImpl();
  }

  private TRTHImportResponseModel importMarketDataImpl()
      throws ComputingServiceException {

    System.out.println("Running TRTHImport Component");

    try {
      stub = new TRTHImportCacheServiceStub(wsURL);
    } catch (AxisFault e) {
      throw new ComputingServiceException(e);
    }

    response = new TRTHImportResponseModel();
    response.setTimeRange(request.getTimeRange());

    param = new TRTHImportCache();
    param.setMessageType(request.getMessageType());
    param.setDate(request.getDateRange());
    param.setTime(request.getTimeRange());
    param.setUseGMT(request.getUseGMT());
    param.setAddCorporateActions(request.getUseCorporateActions());

    param.setRIC(request.getMarketDataRIC());
    importData(1);

    param.setRIC(request.getIndexRIC());
    importData(2);

    param.setRIC(request.getRiskRIC());
    importData(3);

    return response;

  }

  private void importData(int process) throws ComputingServiceException {

    TRTHImportCacheResponse res = null;
    try {
      
      res = stub.tRTHImportCache(param);
    } catch (RemoteException e) {
      // TODO Auto-generated catch block
      throw new ComputingServiceException(e);
    }
    
    boolean status = true;
    String message = res.getMessage();

    if (res.getStatus().toUpperCase().equals("OK")) {
      status = true;
    } else {
      status = false;
    }
    
    switch(process) {
    case 1:
      response.setMarketDataEventSetID(message);
      response.setMarketDataStatus(status);
      break;
    case 2:
      response.setIndexEventSetID(message);
      response.setIndexStatus(status);
      break;
    case 3:
      response.setRiskFreeAssetEventSetID(message);
      response.setRiskFreeAssetStatus(status);
    }

  }

  @SuppressWarnings({"unused"})
  private TRTHImportResponseModel generateDummyData(TRTHImportModel request)
      throws ComputingServiceException {

    String messageType = request.getMessageType();
    String RIC = request.getMarketDataRIC();
    DateRange dateRange = request.getDateRange();
    TimeRange timeRange = request.getTimeRange();

    System.out.println("Running TRTHImport Component");

    TRTHImportResponseModel response = new TRTHImportResponseModel();

    UUID uuid = UUID.randomUUID();
    System.out.println("Generating market data");
    String filename = "rdth-" + uuid + ".csv";
    System.out.println("Market data: " + filename + " generated");
    response.setMarketDataEventSetID(filename);

    uuid = UUID.randomUUID();
    System.out.println("Generating index");
    filename = "rdth-" + uuid + ".csv";
    System.out.println("Index: " + filename + " generated");

    response.setIndexEventSetID(filename);

    uuid = UUID.randomUUID();
    System.out.println("Generating risk free asset");
    filename = "rdth-" + uuid + ".csv";
    System.out.println("Risk free asset: " + filename + " generated");

    response.setRiskFreeAssetEventSetID(filename);

    response.setTimeRange(timeRange);

    return response;

  }

}
