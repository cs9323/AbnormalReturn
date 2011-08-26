package component.computingservice;

import java.text.SimpleDateFormat;

import org.osoa.sca.annotations.Reference;

import util.models.TRTHImportModel;
import component.trthimport.TRTHImport;

public class ComputingServiceImpl implements ComputingService {

    @Reference
    public TRTHImport trth;
    
    @Override
    public String invoke(String messageType, String RIC, 
                          String startTime, String endTime,
                          String startDate, String endDate,
                          String useGMT, String useCorporateActions) 
                                  throws Exception {
        
        
        TRTHImportModel request = 
                constructRequest(messageType, RIC, startTime, endTime, 
                                 startDate, endDate, useGMT, useCorporateActions);
        invokeComputingService(request);
        return null;
    }
    
    private TRTHImportModel 
        constructRequest(String messageType, String RIC, 
                         String startTime, String endTime,
                         String startDate, String endDate,
                         String useGMT, String useCorporateActions) 
                                 throws Exception{
        
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss:sss");
        
        return new TRTHImportModel(Integer.parseInt(messageType), 
                                    RIC, 
                                    timeFormatter.parse(startTime), 
                                    timeFormatter.parse(endTime), 
                                    dateFormatter.parse(startDate), 
                                    dateFormatter.parse(endDate), 
                                    Boolean.parseBoolean(useGMT), 
                                    Boolean.parseBoolean(useCorporateActions));
    }
    
    private String invokeComputingService(TRTHImportModel request) throws Exception {
        return trth.ImportMarketData(request);
    }

}
