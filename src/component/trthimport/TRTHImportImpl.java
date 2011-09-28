package component.trthimport;

import java.rmi.RemoteException;
import java.util.UUID;

import org.apache.axis2.AxisFault;

import component.trthimport.TRTHImportCacheServiceStub.TRTHImportCache;
import component.trthimport.TRTHImportCacheServiceStub.DateRange;
import component.trthimport.TRTHImportCacheServiceStub.TRTHImportCacheResponse;
import component.trthimport.TRTHImportCacheServiceStub.TimeRange;

import util.exceptions.ComputingServiceException;
import util.models.TRTHImportModel;
import util.models.TRTHImportResponseModel;

public class TRTHImportImpl implements TRTHImport {

    private String TMP_DIR = System.getProperty("java.io.tmpdir");
    
    @Override
    public TRTHImportResponseModel ImportMarketData(TRTHImportModel request) throws ComputingServiceException{
      //  return generateDummyData(request);
    	return importMarketDataImpl(request);
    }
    
    private TRTHImportResponseModel importMarketDataImpl(TRTHImportModel request) throws ComputingServiceException {
        String wsURL = "http://soc-server2.cse.unsw.edu.au:14080/axis2/services/TRTHImportCacheService";
        
        System.out.println("Running TRTHImport Component");
        
        TRTHImportCacheServiceStub stub = null;
        try {
            stub = new TRTHImportCacheServiceStub(wsURL);
        } catch (AxisFault e) {
            throw new ComputingServiceException(e.getMessage());
        }
        stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(30000);
        TRTHImportCache wrapper = new TRTHImportCache();
        
        wrapper.setMessageType(request.getMessageType());
        wrapper.setRIC(request.getRIC());
        wrapper.setDate(request.getDateRange());
        wrapper.setTime(request.getTimeRange());
        wrapper.setUseGMT(request.getUseGMT());
        wrapper.setAddCorporateActions(request.getUseCorporateActions());
        
        TRTHImportCacheResponse response = null;
        
        try {
            response = stub.tRTHImportCache(wrapper);
        } catch (RemoteException e) {
            throw new ComputingServiceException(e.getMessage());
        }
        if(response.getStatus().equals("er"))
        	throw new ComputingServiceException(response.getMessage());
        System.out.println("Get: " + response.getMessage());
        TRTHImportResponseModel res = new TRTHImportResponseModel();
        res.setMarketDataEventSetID(response.getMessage());
        try {
            response = stub.tRTHImportCache(wrapper);
        } catch (RemoteException e) {
            throw new ComputingServiceException(e.getMessage());
        }
        if(response.getStatus().equals("er"))
        	throw new ComputingServiceException(response.getMessage());
        System.out.println("Get: " + response.getMessage());
        res.setIndexEventSetID(response.getMessage());
        try {
            response = stub.tRTHImportCache(wrapper);
        } catch (RemoteException e) {
            throw new ComputingServiceException(e.getMessage());
        }
        if(response.getStatus().equals("er"))
        	throw new ComputingServiceException(response.getMessage());
        System.out.println("Get: " + response.getMessage());
        res.setRiskFreeAssetEventSetID(response.getMessage());
        
        res.setTimeRange(request.getTimeRange());
        return res;
    }
    
    private TRTHImportResponseModel generateDummyData(TRTHImportModel request) throws ComputingServiceException{
        
        String messageType = request.getMessageType();
        String RIC = request.getRIC();
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
