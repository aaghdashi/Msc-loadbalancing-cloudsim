package PSO;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import utils.Constants;
import utils.GenerateMatrices;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.text.DecimalFormat;
import java.util.*;

/**
 * A simple example showing how to create
 * a datacenter with two hosts and run two
 * cloudlets on it. The cloudlets run in
 * VMs with different MIPS requirements.
 * The cloudlets will take different time
 * to complete the execution depending on
 * the requested VM performance.
 * 
 **/
public class PSO_Scheduler {

    private static List<Cloudlet> cloudletList;
    private static List<Vm> vmList;
    private static Datacenter[] datacenter;
    private static List<Vm> vmlist1;
    private static List<Vm> vmlist2;
        
    private static int reqTasks = 2000;
    private static int reqVms = 10;

    private static PSO PSO_obj;
    private static double mapping[];
    private static double[][] commMatrix;
    private static double[][] execMatrix;

    private static List<Vm> createVM(int userId, int vms) {
        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<Vm> list = new LinkedList<Vm>();

        //VM Parameters
      int vmid = 0;
    	//int mips = 1000;
    	int[] mips={500,1000,1500,2000,2500};
    	long[] size = {20000,30000,40000,50000,50000}; //image size (MB)
    	int[] ram = {500,1000,2000,3000,4000}; //vm memory (MB)
    	long bw = 1000;
    	int[] pesNumber = {1,2,1,1,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1}; //number of cpus
    	String vmm = "Xen"; //VMM name
        //create VMs
        Vm[] vm = new Vm[vms];

        for (int i = 0; i < vms; i++) {
            vm[i] = new Vm(datacenter[i].getId(), userId, mips[i%5], pesNumber[i], ram[i%5], bw, size[i%5], vmm, new CloudletSchedulerTimeShared());
            list.add(vm[i]);
        }

        return list;
    }
		private static Datacenter createDatacenter(String name) {

		List<Host> hostList = new ArrayList<Host>();
		
//		hostList = createHost(1);
//		
//		String arch = "x86";
//		String os = "Linux";
//		String vmm = "Xen";
//		double time_zone = 10.0;
//		double cost = 3.0;
//		double costPerMem = 0.05;
//		double costPerStorage = 0.001;
//		double costPerBw = 0.0;
//		
//		LinkedList<Storage> storageList = new LinkedList<Storage>();
//
//		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
//				arch, os, vmm, hostList, time_zone, cost, costPerMem,
//				costPerStorage, costPerBw);
//
//		Datacenter datacenter = null;
//		
//		try {
//			datacenter = new Datacenter(name, characteristics, 
//								new VmAllocationPolicySimple(hostList), storageList, 0);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return datacenter;
//                    	List<Host> hostList = new ArrayList<Host>();

        // 2. A Machine contains one or more PEs or CPUs/Cores.
    	// In this example, it will have only one core.
    	List<Pe> peList = new ArrayList<Pe>();

    	int mips = 100000*reqVms;

        // 3. Create PEs and add these into a list.
    	peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

        //4. Create Host with its id and list of PEs and add them to the list of machines
        int hostId=0;
           int ram = 512000; //host memory (MB)
        long storage = 10000000; //host storage
        int bw = 100000;

        hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList,
    				new VmSchedulerTimeShared(peList)
    			)
    		); // This is our machine
        hostList.add(
    			new Host(
    				hostId++,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList,
    				new VmSchedulerTimeShared(peList)
    			)
    		); // This is our machine
        hostList.add(
    			new Host(
    				hostId++,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList,
    				new VmSchedulerTimeShared(peList)
    			)
    		); // This is our machine
        hostList.add(
    			new Host(
    				hostId++,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList,
    				new VmSchedulerTimeShared(peList)
    			)
    		); // This is our machine
        hostList.add(
    			new Host(
    				hostId++,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList,
    				new VmSchedulerTimeShared(peList)
    			)
    		); // This is our machine
        hostList.add(
    			new Host(
    				hostId++,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList,
    				new VmSchedulerTimeShared(peList)
    			)
    		); // This is our machine
        hostList.add(
    			new Host(
    				hostId++,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList,
    				new VmSchedulerTimeShared(peList)
    			)
    		); // This is our machine
        hostList.add(
    			new Host(
    				hostId++,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList,
    				new VmSchedulerTimeShared(peList)
    			)
    		); // This is our machine
        hostList.add(
    			new Host(
    				hostId++,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList,
    				new VmSchedulerTimeShared(peList)
    			)
    		); // This is our machine
        hostList.add(
    			new Host(
    				hostId++,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList,
    				new VmSchedulerTimeShared(peList)
    			)
    		); // This is our machine
//        
        // 5. Create a DatacenterCharacteristics object that stores the
        //    properties of a data center: architecture, OS, list of
        //    Machines, allocation policy: time- or space-shared, time zone
        //    and its price (G$/Pe time unit).
        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource
        double costPerMem = 0.1;		// the cost of using memory in this resource
        double costPerStorage = 0.1;	// the cost of using storage in this resource
        double costPerBw = 0.1;			// the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


        // 6. Finally, we need to create a PowerDatacenter object.
        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Success!! DatacenterCreator is executed!!");
        return datacenter;

	}

    private static List<Cloudlet> createCloudlet(int userId, int cloudlets, int idShift) throws FileNotFoundException, IOException {
        LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();
        ArrayList<Integer> requests = new ArrayList<Integer>();
        
        boolean readFile=false;
        //cloudlet parameters
        long fileSize = 64000000;
    	long outputSize = 64000000;
    	UtilizationModel utilizationModel = new UtilizationModelFull();
    	   	
    if (!readFile) {
            try {
                BufferedReader csvReader = new BufferedReader(new FileReader("G:\\Simul\\your_file4.txt"));
                String row;
                String[] data;
                while ((row = csvReader.readLine()) != null) {
                data = row.split(",");
                requests.add(Integer.parseInt(data[0].trim()));                        
                }
                csvReader.close();

                readFile = true;
            }} 
    for(int id=0;id<reqTasks;id++){
    		
    		Cloudlet task = new Cloudlet(idShift + id, requests.get(id), 1, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
    		task.setUserId(userId);
    		
    		
    		//System.out.println("Task"+id+"="+(task.getCloudletLength()));
    		
    		//add the cloudlets to the list
        	cloudletList.add(task);
    	}
    	System.out.println("SUCCESSFULLY Cloudletlist created :)");
		return cloudletList;
	}




    public static void main(String[] args) {
        Log.printLine("Starting PSO Scheduler...");
		new GenerateMatrices();

        commMatrix = GenerateMatrices.getCommMatrix();
        execMatrix = GenerateMatrices.getExecMatrix();
        PSO_obj = new PSO();
        mapping = PSO_obj.run();

        try {
            int num_user = 1;   // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            CloudSim.init(num_user, calendar, trace_flag);

            // Second step: Create Datacenters
             datacenter = new Datacenter[Constants.data_count];
                     datacenter[0]=createDatacenter("Datacenter_0");
//            List<Host> hostList = new ArrayList<Host>();
//       	 List<Pe> peList = new ArrayList<Pe>();
//       	 int mips = 1000;
//       	 peList.add(new Pe(0, new PeProvisionerSimple(mips)));
//       	 int hostId = 0;
//            int ram = 2048; //host memory (MB)
//            long storage = 1000000; //host storage
//            int bw = 10000;
//            hostList.add(
//                    new Host(
//                            hostId,
//                            new RamProvisionerSimple(ram),
//                            new BwProvisionerSimple(bw),
//                            storage,
//                            peList,
//                            new VmSchedulerTimeShared(peList)
//                    )
//            );
//            
//            String arch = "x86";      // system architecture
//            String os = "Linux";          // operating system
//            String vmm = "Xen";
//            double time_zone = 10.0;         // time zone this resource located
//            double cost = 3.0;              // the cost of using processing in this resource
//            double costPerMem = 0.05;        // the cost of using memory in this resource
//            double costPerStorage = 0.1;    // the cost of using storage in this resource
//            double costPerBw = 0.1;            // the cost of using bw in this resource
//            LinkedList<Storage> storageList = new LinkedList<Storage>();
//            
//            DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
//                    arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);
//            
//            datacenter = new Datacenter[Constants.data_count];
//            for (int i = 0; i < Constants.data_count; i++) {
//                 
//                 datacenter[i]=null;
//                 try {
//                     datacenter[i] = new Datacenter("Datacenter_" + i, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
//                 } catch (Exception e) {
//                     e.printStackTrace();
//                 }
//         }

            //Third step: Create Broker
            PSODatacenterBroker broker = new PSODatacenterBroker("Broker_0");
            int brokerId = broker.getId();

            //Fourth step: Create VMs and Cloudlets and send them to broker
              vmList =  createRequiredVms(reqVms, brokerId);
//                        vmlist2 =  createRequiredVms(reqVms, brokerId2);
//            vmList = createVM(brokerId, Constants.data_count);
            cloudletList = createCloudlet(brokerId, Constants.task_count, 0);

            
            HashSet<Integer> dcIds = new HashSet<>();
            HashMap<Integer, Integer> hm = new HashMap<>();
            for (Datacenter dc : datacenter) {
                if (!dcIds.contains(dc.getId()))
                    dcIds.add(dc.getId());
            }
            Iterator<Integer> it = dcIds.iterator();
            for (int i = 0; i < mapping.length; i++) {
                if (!hm.containsKey((int) mapping[i]))  hm.put((int) mapping[i], it.next());
            }
            for (int i = 0; i < mapping.length; i++)
                mapping[i] = hm.containsKey((int) mapping[i]) ? hm.get((int) mapping[i]) : mapping[i];
			
            broker.submitVmList(vmList);
            broker.setMapping(mapping);
            broker.submitCloudletList(cloudletList);


            // Fifth step: Starts the simulation
            CloudSim.startSimulation();

            List<Cloudlet> newList = broker.getCloudletReceivedList();

            CloudSim.stopSimulation();
			
            printCloudletList(newList);

            Log.printLine(PSO_Scheduler.class.getName() + " finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("Cloudsim has been terminated due to an unexpected error");
        }
    }

  
	public static ArrayList<Vm> createRequiredVms(int reqVms, int brokerId){
		
		ArrayList<Vm> vmlist = new ArrayList<Vm>();
		
    	//VM description
int vmid = 0;
    	//int mips = 1000;
    	int[] mips={500,1000,1500,2000,2500};
    	long[] size = {20000,30000,40000,50000,50000}; //image size (MB)
    	int[] ram = {500,1000,2000,3000,4000}; //vm memory (MB)
    	long bw = 1000;
    	int[] pesNumber = {1,2,1,1,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,1}; //number of cpus
    	String vmm = "Xen"; //VMM name
    	
    	
    	for(vmid=0;vmid<reqVms;vmid++){
    		//add the VMs to the vmList
    		vmlist.add(new Vm(vmid, brokerId, mips[vmid%5], pesNumber[vmid], ram[vmid%5], bw, 
    				size[vmid%5], vmm, new CloudletSchedulerTimeShared()));
    	}

    	System.out.println("VmsCreator function Executed... SUCCESS:)");
		return vmlist;
		
	}    
private static void printCloudletList(List<Cloudlet> list) {
	        int size = list.size();
	        Cloudlet cloudlet;

	        String indent = "    ";
	        Log.printLine();
	        Log.printLine("========== OUTPUT ==========");
	        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
	                "Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time"+ indent + "Response Time"+ indent + "Waiting Time"+indent + "Processing Cost");

	        DecimalFormat dft = new DecimalFormat("###.##");
	        for (int i = 0; i < size; i++) {
	            cloudlet = list.get(i);
	            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

	            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
	                Log.print("SUCCESS");

	            	Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
	                     indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())+
                             indent + indent + dft.format(cloudlet.getFinishTime())+
                             indent + indent+ indent + dft.format(cloudlet.getActualCPUTime())+
                
                                indent + indent + indent+ dft.format(cloudlet.getWaitingTime())+
                                "    "+ dft.format(cloudlet.getUtilizationOfCpu(CloudSim.clock())));
	            }
	        }

	    }
}