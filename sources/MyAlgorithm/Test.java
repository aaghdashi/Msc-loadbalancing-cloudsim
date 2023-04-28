/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyAlgorithm;

import java.util.concurrent.ThreadLocalRandom;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

/**
 *
 * @author Arman
 */
public class Test {
    
    /**
     * The cloudlet list.
     */
    private static List<MyCloudlet> cloudletList;
    
    /**
     * The vmlist.
     */
    private static List<MyVm> vmlist1;
    

	private static int reqTasks = 10000;
	private static int reqVms = 100;


    /**
     * Creates main() to run this example
     */
    public static void main(String[] args) {

		Log.printLine("Starting My Algorithm...");

	        try {
	        	// First step: Initialize the CloudSim package. It should be called
	            	// before creating any entities.
	            	int num_user = 1;   // number of cloud users
	            	Calendar calendar = Calendar.getInstance();
	            	boolean trace_flag = false;  // mean trace events

	            	// Initialize the CloudSim library
	            	CloudSim.init(num_user, calendar, trace_flag);

	            	// Second step: Create Datacenters
	            	//Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
	            	@SuppressWarnings("unused")
			Datacenter datacenter0 = createDatacenter("Datacenter_0");

                        List<Host> hl=datacenter0.getHostList();
	            	//Third step: Create Broker
	            	MyDataCenterBroker broker = createBroker();
	            	int brokerId1 = broker.getId();
	            	//Fourth step: Create one virtual machine
	            	vmlist1 = new VmsCreator().createRequiredVms(reqVms, brokerId1);

	            	//submit vm list to the broker
	            	broker.submitVmList(vmlist1);

//	            	//Fifth step: Create two Cloudlets
	            	cloudletList = new CloudletCreator().createUserCloudlet(reqTasks, brokerId1);

                        //submit cloudlet list to the broker
	            	broker.submitCloudletList(cloudletList);
//    	
	            	//call the scheduling function via the broker
	            	broker.scheduleTaskstoVms();
            	
	            	// Sixth step: Starts the simulation
	            	CloudSim.startSimulation();


	            	// Final step: Print results when simulation is over
	            	List<MyCloudlet> newList = broker.getCloudletReceivedList();

                        for (Iterator<Host> iterator = hl.iterator(); iterator.hasNext();) {
                        Host next = iterator.next();                        
                        double hostcapacity=next.getNumberOfFreePes()*next.getAvailableMips()/next.getNumberOfPes()*next.getMaxAvailableMips();
                        Log.printLine( next.getId()+ " is "+hostcapacity/1000000);
                                               
                    }
                        CloudSim.stopSimulation();

	            	printCloudletList(newList);

	            	Log.printLine("My Algorithm finished!");
                        
                        
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            Log.printLine("The simulation has been terminated due to an unexpected error");
	        }
                
	    }
    

	private static Datacenter createDatacenter(String name){
			Datacenter datacenter=new DataCenterCreator().createUserDatacenter(name, reqVms);			

	        return datacenter;

	    }
    	//We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
    //to the specific rules of the simulated scenario
    private static MyDataCenterBroker createBroker() {

        MyDataCenterBroker broker = null;
        try {
            broker = new MyDataCenterBroker("Broker");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }

    /**
     * Prints the Cloudlet objects
     *
     * @param list list of Cloudlets
     */
    private static void printCloudletList(List<MyCloudlet> list) {
	        int size = list.size();
	        MyCloudlet cloudlet;

	        String indent = "    ";
	        Log.printLine();
	        Log.printLine("========== OUTPUT ==========");
	        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
	                "Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time"+ indent + "Response Time"+ indent + "deadline"+ indent + "remaing deadline"+ indent + "Waiting Time"+ indent + "Processin Cost");

	        DecimalFormat dft = new DecimalFormat("###.##");
	        for (int i = 0; i < size; i++) {
	            cloudlet = list.get(i);
	            Log.print(indent + cloudlet.getCloudletId() + indent + indent);
                    if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
	                Log.print("SUCCESS");

	            	Log.printLine(  "    " + cloudlet.getResourceId() + "    "+ cloudlet.getVmId() +
	                     "    "+ dft.format(cloudlet.getActualCPUTime()) + "    " + dft.format(cloudlet.getExecStartTime())+
                             "    " + dft.format(cloudlet.getFinishTime())+
                             "    " + dft.format(cloudlet.getActualCPUTime())+
                             "    "+ dft.format(cloudlet.getDeadLine())+
                                "    "+ dft.format(cloudlet.getRemainingDeadline())+
                                "    "+ dft.format(cloudlet.getWaitingTime())+
                                "    "+ dft.format(cloudlet.getProcessingCost()));
	            }
	        }

	    }
    
    
    
    
	private static List<Vm> ReCreateVM(int userId, int vms, int idShift, List<Vm> oldVm) {
            

		//Creates a container to store VMs. This list is passed to the broker later
		LinkedList<Vm> list = new LinkedList<Vm>();

		//VM Parameters
		int randomNum = ThreadLocalRandom.current().nextInt(200, 1001);

		long size = 10000; //image size (MB)
		int ram = 512; //vm memory (MB)
//		int mips = 200;
//		randomNum = ThreadLocalRandom.current().nextInt(1000, 2501);
//		long bw = randomNum;
//		randomNum = ThreadLocalRandom.current().nextInt(1,5);
//		int pesNumber = randomNum; //number of cpus
		
		String vmm = "Xen"; //VMM name

		//create VMs
		Vm[] vm = new Vm[vms];

		for(int i=0;i < vms;i++){
			vm[i] = new Vm(idShift + i, userId, oldVm.get(i).getMips(), oldVm.get(i).getNumberOfPes(), ram, oldVm.get(i).getBw(), size, vmm, new CloudletSchedulerTimeShared());
			list.add(vm[i]);
		}

		return list;
}
        private static List<Vm> createVM(int userId, int vms, int idShift) {

		//Creates a container to store VMs. This list is passed to the broker later
		LinkedList<Vm> list = new LinkedList<Vm>();

		//VM Parameters
		int randomNum = ThreadLocalRandom.current().nextInt(200, 1001);

		long size = 10000; //image size (MB)
		
		int ram = 512; //vm memory (MB)
//		int ram = ThreadLocalRandom.current().nextInt(200, 400);
//		int mips = ThreadLocalRandom.current().nextInt(200, 400);
//		randomNum = ThreadLocalRandom.current().nextInt(1000, 2501);
//		long bw = randomNum;
//		randomNum = ThreadLocalRandom.current().nextInt(1,5);
//		int pesNumber = randomNum; //number of cpus
		
		String vmm = "Xen"; //VMM name

		//create VMs
		Vm[] vm = new Vm[vms];

		for(int i=0;i < vms;i++){

			int mips = ThreadLocalRandom.current().nextInt(200, 400);
			randomNum = ThreadLocalRandom.current().nextInt(1000, 2501);
			long bw = randomNum;
			randomNum = ThreadLocalRandom.current().nextInt(1,5);
			int pesNumber = randomNum; //number of cpus

			vm[i] = new Vm(idShift + i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			list.add(vm[i]);
		}

		return list;
}
        
    
}
