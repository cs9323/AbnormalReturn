package util.models;

public class VisualizationModel {
	
       public VisualizationModel(String name,String meaning,String type) {
		     this.name=name;
		     this.meaning=meaning;
		     this.type=type;
	}
       String name;
       public String getName(){return name;}
       public void setName(String value){name=value;}
       
       String meaning;
       public String getMeaning(){return meaning;}
       public void setMeaning(String value){meaning=value;}
       
       String type;
       public String getType(){return type;}
       public void setType(String value){type=value;}
  
}
