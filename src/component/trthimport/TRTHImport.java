package composite.trthimport;

import org.osoa.sca.annotations.Remotable;

import util.models.TRTHImportModel;

@Remotable
public interface TRTHImport {
    public String ImportMarketData(TRTHImportModel request);
}
