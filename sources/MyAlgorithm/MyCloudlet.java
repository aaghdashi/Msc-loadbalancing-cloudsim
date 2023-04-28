/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyAlgorithm;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import java.util.Random;
import org.cloudbus.cloudsim.Vm;
import java.math.MathContext;
/**
 *
 * @author Arman
 */

public class MyCloudlet extends Cloudlet{
        public int select_task=0;  // a index for select the task
        public Random deadline=new Random();
        private double d;
        private double rt;
    public MyCloudlet(int cloudletId, long cloudletLength, int pesNumber,
			long cloudletFileSize, long cloudletOutputSize,
			UtilizationModel utilizationModelCpu,
			UtilizationModel utilizationModelRam,
			UtilizationModel utilizationModelBw){
     
         super(cloudletId, cloudletLength, pesNumber, cloudletFileSize,
				cloudletOutputSize, utilizationModelCpu, utilizationModelRam,
				utilizationModelBw); 
         
         d=deadline.nextInt(4)+1;
     }
    public double getDeadLine(){
        return d;
    }
    public double getRemainingDeadline(){
        return (this.getDeadLine()-this.getSubmissionTime());
    }
 
}
