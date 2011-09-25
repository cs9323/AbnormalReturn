package component.visualization;

import org.osoa.sca.annotations.Remotable;

import util.exceptions.ComputingServiceException;
import util.models.VisualizationModel;
import util.models.VisualizationResponseModel;

@Remotable
public interface Visualization {
    public VisualizationResponseModel VisualizeData(VisualizationModel request) throws ComputingServiceException;
}
