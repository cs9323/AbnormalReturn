package component.computingservice;


import java.io.File;

import org.osoa.sca.annotations.Remotable;

import util.exceptions.ComputingServiceException;

@Remotable
public interface ComputingService {
    public File invoke(String messageType, String RIC, 
                          String startTime, String endTime,
                          String startDate, String endDate,
                          String useGMT, String useCorporateActions) 
                                  throws ComputingServiceException;
}
