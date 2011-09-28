package component.computingservice;

import org.osoa.sca.annotations.Remotable;

import util.exceptions.ComputingServiceException;

@Remotable
public interface ComputingService {
	public String invoke(String messageType, String marketDataRIC, String indexRIC, String riskRIC,
    		String startDate, String endDate, String startTime, String endTime,
            String useGMT, String useCorporationAction, 
            String Measurement, String intervalUnit, String intervalDuration,
            String mergeOption,
            String daysWindow, String modelType) 
    		throws ComputingServiceException;
}
