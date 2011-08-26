package component.trthimport;

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.UUID;

import util.models.TRTHImportModel;

public class TRTHImportImpl implements TRTHImport {

    private String TMP_DIR = System.getProperty("java.io.tmpdir");
    
    @Override
    public String ImportMarketData(TRTHImportModel request) throws Exception{
        return generateDummyData(request);
    }
    
    private String generateDummyData(TRTHImportModel request) throws Exception{
        
        Integer messageType = request.getMessageType();
        String RIC = request.getRIC();
        Date startTime = request.getStartTime();
        Date endTime = request.getEndTime();
        Date startDate = request.getStartDate();
        Date endDate = request.getEndDate();

        if(startTime.after(endTime)) {
            throw new Exception("Start time is later than end time");
        }
        
        if(startDate.after(endDate)) {
            throw new Exception("Start date is later than end date");
        }
        
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
        out.println("startTime: " + startTime.toString());
        out.println("endTime: " + endTime.toString());
        out.println("startDate: " + startDate.toString());
        out.println("endDate: " + endDate.toString());
        
        out.close();
        
        return "ok:" + filename;
        
    }
    
}
