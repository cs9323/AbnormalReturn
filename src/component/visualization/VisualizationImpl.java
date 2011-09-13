package component.visualization;


import util.exceptions.ComputingServiceException;
import util.models.VisualizationModel;
import util.models.VisualizationResponseModel;

public class VisualizationImpl {

     public VisualizationResponseModel visualize(VisualizationModel inputs)
            throws ComputingServiceException{
    	 return dummy(inputs);
     }
     
     private VisualizationResponseModel dummy(VisualizationModel inputs)
            throws ComputingServiceException{
         System.out.println("Invoking Visualization...");
         VisualizationResponseModel vrm=new VisualizationResponseModel();
         vrm.setcsvURL("http://soc-server2.cse.unsw.edu.au:8080/crcnews//visualchart/?path=merge-3292477.csv");
         return vrm;
     }

}
