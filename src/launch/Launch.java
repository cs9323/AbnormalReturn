package launch;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.tuscany.sca.host.embedded.SCADomain;

import component.computingservice.ComputingService;

public class Launch {
    public static void main(String[] args) throws Exception{

    	
        SCADomain scaDomain = SCADomain.newInstance("computingservice.composite");
        ComputingService service = scaDomain.getService(ComputingService.class,
                "ComputingServiceComponent/ComputingService");

        System.out.println("Input: MessageType, RIC, StartTime, EndTime, " +
        		"StartDate, EndDate, " +
        		"useGMT, useCorporationAction");
        try {
            service.invoke("EndOfDay", "BHP.AX", "BHP.AX", "BHP.AX",
            		"01-01-2004", "01-12-2009", "00:00:00.000", "23:59:59.999",
                    "true", "", 
                    "ClsPrice", "spot", "0",
                    "ByFile1ClosestBeforeOrEqualToEndInterval",
                    "3", "marketmodel");
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        while(reader.readLine() != null);
        
        scaDomain.close();

    }
}
