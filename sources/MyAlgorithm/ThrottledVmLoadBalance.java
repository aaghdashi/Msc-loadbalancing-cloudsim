/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eval.eval;

import MyAlgorithm.MyCloudlet;
import MyAlgorithm.MyVm;
import MyAlgorithm.VirtualMachineState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Arman
 */


import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;

public class ThrottledVmLoadBalance extends DatacenterBroker{
    	private Map<Integer, Integer> currentAllocationCounts;
        protected Map<Integer, Integer> vmAllocationCounts;	
	private Map<Integer, MyAlgorithm.VirtualMachineState> vmStatesList;

    public ThrottledVmLoadBalance(String name) throws Exception {
        super(name);
        vmAllocationCounts = new HashMap<Integer, Integer>();
    }
        @Override
	protected void processResourceCharacteristics(SimEvent ev) {
		DatacenterCharacteristics characteristics = (DatacenterCharacteristics) ev.getData();
		getDatacenterCharacteristicsList().put(characteristics.getId(), characteristics);

		if (getDatacenterCharacteristicsList().size() == getDatacenterIdsList().size()) {
			createVmsInDatacenter(getDatacenterIdsList());
		}
	};
	
	protected void createVmsInDatacenter(List<Integer> datacenterIds) {
		
		// send as much vms as possible for this datacenter before trying the next one
		int requestedVms = 0;
		int i = 0;
		for (Vm vm : getVmList()) {
			
			int datacenterId = datacenterIds.get(i++ % datacenterIds.size());
			String datacenterName = CloudSim.getEntityName(datacenterId);
			
			if (!getVmsToDatacentersMap().containsKey(vm.getId())) {
				Log.printLine(CloudSim.clock() + ": " + getName() + ": Trying to Create VM #" + vm.getId() + " in " + datacenterName);
				sendNow(datacenterId, CloudSimTags.VM_CREATE_ACK, vm);
				requestedVms++;
			}
		}

		setVmsRequested(requestedVms);
		setVmsAcks(0);
	};
        /**
	 * @return VM id of the first available VM from the vmStatesList in the calling
	 * 			{@link DatacenterController}
	 */
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
		
		if (vmStatesList.size() > 0){
			int temp;
			for (Iterator<Integer> itr = vmStatesList.keySet().iterator(); itr.hasNext();){
				temp = itr.next();
                                
				int state = temp; //System.out.println(temp + " state is " + state + " total vms " + vmStatesList.size());
				if (VirtualMachineState.AVAILABLE.equals(state)){
					vmId = temp;
					break;
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

