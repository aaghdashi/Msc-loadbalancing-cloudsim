import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;

/**
 * CloudletCreator Creates Cloudlets as per the User Requirements.
 * @author Linda J
 *
 */
public class CloudletCreator {
	
	
	//cloudlet creator
	public ArrayList<Cloudlet> createUserCloudlet(int reqTasks,int brokerId) throws IOException{
	ArrayList<Cloudlet> cloudletList = new ArrayList<Cloudlet>();
        ArrayList<Integer> requests = new ArrayList<Integer>();
		
    	//Cloudlet properties
        boolean readFile=false;
        int id = 0;
    	int pesNumber=1;
//        long[] length = {};
      	long fileSize = 64000000;
    	long outputSize = 64000000;
    	UtilizationModel utilizationModel = new UtilizationModelFull();


        if (!readFile) {
            try 
            {
                BufferedReader csvReader = new BufferedReader(new FileReader(".\\data\\sim1.txt"));
                String row;
                String[] data;
                while ((row = csvReader.readLine()) != null) {
                data = row.split(",");
                requests.add(Integer.parseInt(data[0].trim()));                        
                }
                csvReader.close();

                readFile = true;
            }
            catch(Exception e){
System.out.println(e.getMessage());
}
        } 
    	
    	for(id=0;id<reqTasks;id++){
    		
    		Cloudlet task = new Cloudlet(id, requests.get(id), pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
    		task.setUserId(brokerId);
    		
    		
    		//System.out.println("Task"+id+"="+(task.getCloudletLength()));
    		
    		//add the cloudlets to the list
        	cloudletList.add(task);
    	}

    	System.out.println("SUCCESSFULLY Cloudletlist created :)");

		return cloudletList;
		
	}

}
