package component.trthimport;

import java.rmi.RemoteException;
import java.util.UUID;

import org.apache.axis2.AxisFault;

import component.trthimport.TRTHImportWrapperServiceStub.DateRange;
import component.trthimport.TRTHImportWrapperServiceStub.TRTHImportWrapper;
import component.trthimport.TRTHImportWrapperServiceStub.TRTHImportWrapperResponse;
import component.trthimport.TRTHImportWrapperServiceStub.TimeRange;

import util.models.TRTHImportModel;
import util.models.TRTHImportResponseModel;

public class TRTHImportImpl implements TRTHImport {

    private String TMP_DIR = System.getProperty("java.io.tmpdir");
    
    @Override
    public TRTHImportResponseModel ImportMarketData(TRTHImportModel request) throws Exception{
        return generateDummyData(request);
        
    }
    
    private TRTHImportResponseModel importMarketDataImpl(TRTHImportModel request) throws AxisFault, RemoteException {
        String wsURL = "http://soc-server2.cse.unsw.edu.au:14080/axis2/services/TRTHImportWrapperService";
        
        //System.out.println("1");
        TRTHImportWrapperServiceStub stub = new TRTHImportWrapperServiceStub(wsURL);
        stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(30000);
        TRTHImportWrapper wrapper = new TRTHImportWrapper();
        
        //System.out.println("2");
        
        wrapper.setMessageType(request.getMessageType());
        wrapper.setRIC(request.getRIC());
        wrapper.setDate(request.getDateRange());
        wrapper.setTime(request.getTimeRange());
        wrapper.setUseGMT(request.getUseGMT());
        wrapper.setAddCorporateActions(request.getUseCorporateActions());
        
        //System.out.println("3");
        TRTHImportWrapperResponse response = stub.tRTHImportWrapper(wrapper);
        //System.out.println("4");
        //System.out.println(response.getMessage());
        // TODO: Here need to generate a model
        return null;
    }
    
    private TRTHImportResponseModel generateDummyData(TRTHImportModel request) throws Exception{
        
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
