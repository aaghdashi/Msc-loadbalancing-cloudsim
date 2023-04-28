/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.examples.MinMin_algorithm;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.*;

/**
 *
 * @author shadi
 */
public class Myvm extends Vm {

    private double waitingtime=0;

    private int state=0;

    public Myvm(int vmId,
            int userId,
            double mips,
            int numberOfPes,
            int ram,
            long bw,
            long size,
            String vmm,
            CloudletScheduler cloudletScheduler) {

        //  TODO Auto-generated constructor stub
        super(vmId, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
    }
    
    public void setwaitingtime(double waitingtime){
            this.waitingtime+=waitingtime;
        }
        public double getWaitingtime(){
            return this.waitingtime;
        }
//        public void updatewaitingtime(double waitingtime){
//            this.waitingtime-=waitingtime;
//        }
        
         public void setstate(){
            this.state=1;
        }

}
