package launch;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.tuscany.sca.host.embedded.SCADomain;

import component.computingservice.ComputingService;

public class Launch {
    public static void main(String[] args) {

        SCADomain scaDomain = SCADomain.newInstance("computingservice.composite");
        ComputingService service = scaDomain.getService(ComputingService.class,
                "ComputingServiceComponent/ComputingService");

        String input = new String();

        BufferedReader instreamReader = new BufferedReader(
                new InputStreamReader(System.in));
        
        System.out.println("Input: MessageType, RIC, StartTime, EndTime, " +
        		"StartDate, EndDate, " +
        		"useGMT, useCorporationAction");
        try {
            while((input = instreamReader.readLine()) != null) {
                String[] tokens = input.split(",");
                service.invoke(tokens[0], tokens[1], 
                               tokens[2], tokens[3], 
                               tokens[4], tokens[5], 
                               tokens[6], tokens[7]);
                System.out.println("Input: MessageType, RIC, " +
                		"StartTime, EndTime, StartDate, EndDate, " +
                        "useGMT, useCorporationAction");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        scaDomain.close();

    }
}
