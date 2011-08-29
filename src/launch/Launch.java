package launch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.tuscany.sca.host.embedded.SCADomain;

import component.computingservice.ComputingService;

public class Launch {
    public static void main(String[] args) {

    	
        SCADomain scaDomain = SCADomain.newInstance("computingservice.composite");
        ComputingService service = scaDomain.getService(ComputingService.class,
                "ComputingServiceComponent/ComputingService");

        System.out.println("Input: MessageType, RIC, StartTime, EndTime, " +
        		"StartDate, EndDate, " +
        		"useGMT, useCorporationAction");
        try {
            service.invoke("0","BHP.AX;RIO.AX","00:00:00:000","23:59:59:999","01-01-2011","01-03-2011","1","0");
        } catch (AxisFault e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } 

        scaDomain.close();

    }
}
