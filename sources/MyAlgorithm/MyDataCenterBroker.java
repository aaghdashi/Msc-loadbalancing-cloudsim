/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyAlgorithm;

import java.util.ArrayList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import MyAlgorithm.MyCloudlet;
import MyAlgorithm.MyVm;
import org.cloudbus.cloudsim.lists.VmList;

/**
 *
 * @author Arman
 */
public class MyDataCenterBroker extends DatacenterBroker {

    public MyDataCenterBroker(String name) throws Exception {
        super(name);

    }
        public boolean loadDegree(Vm vm){
                                  
            int ram= vm.getRam();
            int c_ram=vm.getCurrentAllocatedRam();

            long c_bw= vm.getCurrentAllocatedBw();
            long bw=vm.getBw();            
            
            long s_bw = c_bw/bw;
            int s_ram=c_ram/ram;
            if (((s_bw+s_ram)/2)<70) {
                return true;
            }
            else        
            {
                return false;
            }                
            
        }
    	public void scheduleTaskstoVms(){
		int reqTasks= cloudletList.size();
                
		int reqVms= vmList.size();
		int k=0;
		
		ArrayList<MyCloudlet> clist = new ArrayList<MyCloudlet>();
		ArrayList<Vm> vlist = new ArrayList<Vm>();
                
		for (MyCloudlet cloudlet : getCloudletList()) {
    		clist.add(cloudlet);
    		System.out.println("clist:" +clist.get(k).getCloudletId());
    		k++;
		}
		k=0;
		for (Vm vm : getVmList()) {
    		vlist.add(vm);
    		System.out.println("vlist:" +vlist.get(k).getId());
    		k++;
		}
		double completionTime[][] = new double[reqTasks][reqVms];
		double execTime[][] = new double[reqTasks][reqVms];
                double deadline[]=new double[reqTasks];                
                double arrive[]=new double[reqTasks];
               	double time =0.0;
		
                for (int i = 0; i < reqTasks; i++) {
                deadline[i]=getDeadline(clist.get(i));
                arrive[i]=getSubmissionTime(clist.get(i));
            }
		for(int i=0; i<reqTasks; i++){
			for(int j=0; j<reqVms; j++){
				time = getCompletionTime(clist.get(i), vlist.get(j));
				completionTime[i][j]= time;
				time = getExecTime(clist.get(i), vlist.get(j));
				execTime[i][j]= time;
				
				System.out.print(execTime[i][j]+" ");
				
			}
			System.out.println();
			
		}
		
		int Cloudlet=0;
		int minVm=0;
		double min=-1.0d;
		int mindead=0;
                int failedExe=0;
                
		for(int c=0; c< clist.size(); c++){
			
			for(int i=0;i<clist.size();i++){
                            if (deadline[i]<deadline[mindead] &&  deadline[i]>-1.0) {
                                mindead=i;
                                
                            }
				for(int j=0;j<(vlist.size()-1);j++){
//					if(completionTime[mindead][j+1] < completionTime[mindead][j] && completionTime[mindead][j+1] > -1.0){
                                            if (execTime[mindead][j]<=completionTime[mindead][j]+deadline[mindead] && deadline[mindead]+arrive[mindead]>completionTime[mindead][j]&& completionTime[mindead][j+1] > -1.0) {
                                                Cloudlet=mindead;
                                                minVm=j;
                                            }
                                        else
                                            {
                                                failedExe+=1;
                                            }
                                            
//					}
				}
			}		                     
                                min = getExecTime(clist.get(Cloudlet), vlist.get(minVm));
				for(int j=0; j<vlist.size(); j++){
					time = getExecTime(clist.get(Cloudlet), vlist.get(j));					
					if(time < min && time > -1.0){                                               					
                                                boolean a=loadDegree(vlist.get(j)) ;
                                                if (a) {
                                                minVm=j;                                                						
                                            }   
					}
                                        else{
                                                    min=time;
                                        }
			}
			
			bindCloudletToVm(Cloudlet, minVm);
			clist.remove(Cloudlet);
			
			for(int i=0; i<vlist.size(); i++){
				completionTime[Cloudlet][i]=-1.0;
                                deadline[Cloudlet]=-1.0;
			}
			
		}
		
		
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
        
    
    public int step = 0; // number of steps

    // Gets the vm list
    @Override
    public List<MyVm> getVmsCreatedList() {
        return (List<MyVm>) vmsCreatedList;
    }

    // Gets the cloudlet list.
    @Override
    public List<MyCloudlet> getCloudletList() {
        return (List<MyCloudlet>) cloudletList;
    }

    // Submit cloudlets to the created VMs
//    @Override
//    protected void submitCloudlets() {
//
////       for (int i = 0; i < getCloudletList().size(); i++) {
////
////            My_algorithm();
////        }
//
//        Log.printLine("===========================================");
//        int vmIndex = 0;
//
//        for (MyCloudlet cloudlet : getCloudletList()) {
//            MyVm vm;
//
//            // if user didn't bind this cloudlet and it has not been executed yet
//            if (cloudlet.getVmId() == -1) {
//                vm = getVmsCreatedList().get(vmIndex);
//            } else { // submit to the specific vm
//                vm = VmList.getById(getVmsCreatedList(), cloudlet.getVmId());
//                if (vm == null) { // vm was not created
//                    Log.printLine(CloudSim.clock() + ": " + getName() + ": Postponing execution of cloudlet "
//                            + cloudlet.getCloudletId() + ": bount VM not available");
//                    continue;
//                }
//            }
//
//            Log.printLine(CloudSim.clock() + ": " + getName() + ": Sending cloudlet "
//                    + cloudlet.getCloudletId() + " to VM #" + vm.getId());
//            cloudlet.setVmId(vm.getId());
//            //****
//            vm.setstate();
//
//            sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
//            cloudletsSubmitted++;
//            getCloudletSubmittedList().add(cloudlet);
//
//            vmIndex = (vmIndex + 1) % getVmsCreatedList().size();
//        }
//
//        // remove submitted cloudlets from waiting list
//        for (Cloudlet cloudlet : getCloudletSubmittedList()) {
//            getCloudletList().remove(cloudlet);
//        }
//    }
//

    public double calculatewaitingtimeofvm(int vmid) {
        double waitingtime = 0.0;
        waitingtime = getVmsCreatedList().get(vmid).getWaitingtime();

        return waitingtime;
    }
    


}

