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
            service.invoke("0","BHP.AX","00:00:00.000","23:59:59.999","01-01-2011","03-01-2011","1","0");
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        /*BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        while(reader.readLine() != null);*/
        
        scaDomain.close();

    }
}
