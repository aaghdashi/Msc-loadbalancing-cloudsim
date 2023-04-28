/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;

/**
 *
 * @author Arman
 */
public class ESCEBroker extends DatacenterBroker{
    	private Map<Integer, Integer> currentAllocationCounts;
        protected Map<Integer, Integer> vmAllocationCounts;	
	private Map<Integer, VirtualMachineState> vmStatesList;

    public ESCEBroker(String name) throws Exception {
        super(name);
        vmAllocationCounts = new HashMap<Integer, Integer>();
    }
     
    public void scheduleTaskstoVms(){
		
		ArrayList<Cloudlet> clist = new ArrayList<Cloudlet>();
		
		for (Cloudlet cloudlet : getCloudletSubmittedList()) {
			clist.add(cloudlet);
                        bindCloudletToVm(cloudlet.getCloudletId(), getNextAvailableVm());
			//System.out.println("cid:"+ cloudlet.getCloudletId());
		}
		
		setCloudletReceivedList(clist);
                
	}
	
    
    
    public int getNextAvailableVm(){
		int vmId = -1;
		
		//Find the vm with least number of allocations
		
		//If all available vms are not allocated, allocated the new ones
		if (currentAllocationCounts.size() < vmStatesList.size()){
			for (int availableVmId : vmStatesList.keySet()){
				if (!currentAllocationCounts.containsKey(availableVmId)){
					vmId = availableVmId;
					break;
				}				
			}
		} else {
			int currCount;
			int minCount = Integer.MAX_VALUE;
			
			for (int thisVmId : currentAllocationCounts.keySet()){
				currCount = currentAllocationCounts.get(thisVmId);
				if (currCount < minCount){
					minCount = currCount;
					vmId = thisVmId;
				}
			}
		}
		
		allocatedVm(vmId);
		
		return vmId;
		
	}
	protected void allocatedVm(int currVm){
		
		Integer currCount = vmAllocationCounts.get(currVm);
		if (currCount == null){
			currCount = 0;
		}
		vmAllocationCounts.put(currVm, currCount + 1);		
	}
                
        
    // Gets the vm list
    @Override
    public List<MyVm> getVmsCreatedList() {
        return (List<MyVm>) vmsCreatedList;
    }
private double getCompletionTime(MyCloudlet cloudlet, Vm vm){
		double waitingTime = cloudlet.getWaitingTime();
		double execTime = cloudlet.getCloudletLength() / (vm.getMips()*vm.getNumberOfPes());
		
		double completionTime = execTime + waitingTime;
		
		return completionTime;
	}
	
	private double getExecTime(MyCloudlet cloudlet, Vm vm){
		return cloudlet.getCloudletLength() / (vm.getMips()*vm.getNumberOfPes());
                
	}
        private double getSubmissionTime(MyCloudlet cloudlet)
        {
            return cloudlet.getSubmissionTime();
        }
        private double getDeadline(MyCloudlet cloudlet){         
            return cloudlet.getDeadLine();
        }
    // Gets the cloudlet list.
    @Override
    public List<MyCloudlet> getCloudletList() {
        return (List<MyCloudlet>) cloudletList;
    }        
        public double calculatewaitingtimeofvm(int vmid) {
        double waitingtime = 0.0;
        waitingtime = getVmsCreatedList().get(vmid).getWaitingtime();

        return waitingtime;
    }
    
}
