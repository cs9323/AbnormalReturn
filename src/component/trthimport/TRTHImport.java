package component.trthimport;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.osoa.sca.annotations.Remotable;

import util.models.TRTHImportModel;
import util.models.TRTHImportResponseModel;

@Remotable
public interface TRTHImport {
    public TRTHImportResponseModel ImportMarketData(TRTHImportModel request) throws AxisFault, RemoteException;
}
