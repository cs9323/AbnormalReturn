package component.trthimport;

import org.osoa.sca.annotations.Remotable;

import util.models.TRTHImportModel;
import util.models.TRTHImportResponseModel;

@Remotable
public interface TRTHImport {
    public TRTHImportResponseModel ImportMarketData(TRTHImportModel request) throws Exception;
}
