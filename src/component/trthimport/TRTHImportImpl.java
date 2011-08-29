package component.trthimport;

import java.io.File;
import java.io.PrintWriter;
import java.util.UUID;

import component.trthimport.TRTHImportWrapperServiceStub.DateRange;
import component.trthimport.TRTHImportWrapperServiceStub.TRTHImportWrapper;
import component.trthimport.TRTHImportWrapperServiceStub.TRTHImportWrapperResponse;
import component.trthimport.TRTHImportWrapperServiceStub.TimeRange;

import util.models.TRTHImportModel;

public class TRTHImportImpl implements TRTHImport {

    private String TMP_DIR = System.getProperty("java.io.tmpdir");
    
    @Override
    public String ImportMarketData(TRTHImportModel request) throws Exception{
        return importMarketDataImpl(request);
    }
    
    private String importMarketDataImpl(TRTHImportModel request) throws Exception {
        String wsURL = "http://soc-server2.cse.unsw.edu.au:14080/axis2/services/TRTHImportWrapperService";
        TRTHImportWrapperServiceStub stub = new TRTHImportWrapperServiceStub(wsURL);
        TRTHImportWrapper wrapper = new TRTHImportWrapper();
        
        wrapper.setMessageType(request.getMessageType());
        wrapper.setRIC(request.getRIC());
        wrapper.setDate(request.getDateRange());
        wrapper.setTime(request.getTimeRange());
        wrapper.setUseGMT(request.getUseGMT());
        wrapper.setAddCorporateActions(request.getUseCorporateActions());
        
        TRTHImportWrapperResponse response = stub.tRTHImportWrapper(wrapper);
        
        return response.getMessage();
    }
    
    private String generateDummyData(TRTHImportModel request) throws Exception{
        
        String messageType = request.getMessageType();
        String RIC = request.getRIC();
        DateRange dateRange = request.getDateRange();
        TimeRange timeRange = request.getTimeRange();

        UUID uuid = UUID.randomUUID();
        String filename = TMP_DIR + "/rdth-" + uuid + ".csv";
        PrintWriter out = new PrintWriter(new File(TMP_DIR + "/rdth-" + uuid + ".csv"));
        //out.println("#messageType,RIC,Date[G],Time[G],GMT Offset,Type,Price,Volumn,Bid Price,Bid Size,Ask Price,Ask Size");
        
        if(messageType == TRTHImportModel.END_OF_DAY) {
            out.print("messageType: End of Day");
        } else if(messageType == TRTHImportModel.TIME_AND_SALES) {
            out.print("messageType: Time and Sales");
        }
        
        out.println("RIC: " + RIC);
        out.println("DateRange: " + dateRange.toString());
        out.println("TimeRange: " + timeRange.toString());
        out.close();
        
        return "ok:" + filename;
        
    }
    
}
