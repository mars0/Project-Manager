import java.util.*;


public class Sequencing {
	
	//private int[][] availableResources;
	private Project project;
	private int numberOfRes;
	
	public Sequencing(Project p) {
		this.project = p;
		this.numberOfRes = project.getMaxNumOfResources();
	}
	
	private Activity leastLFT(List<Activity> activities) {
//		System.out.println("leastLFT");
		int min = Integer.MAX_VALUE;
		Activity res = null;
		Iterator<Activity> it = activities.iterator();
		while(it.hasNext()) {
			Activity a = it.next();
			if (a.getDuration()+a.getTimeMax() < min) {
				min = a.getDuration()+a.getTimeMax();
				res = a;
			}
		}
		return res;
	}
	
	private int latestFinishingTime(List<Activity> activities) {
//		System.out.println("latestFinishingTime");
		int time = 0;
		Iterator<Activity> it = activities.iterator();
		while(it.hasNext()) {
			Activity a = it.next();
			if (a.getStartDay()+a.getDuration() > time)
				time = a.getStartDay()+a.getDuration();
		}
		return time;
	}
	
	private int nextAvailable(Activity a, int start, Map<Integer,int[]> availableRes) {
	//	System.out.println("nextAvailable");
		int duration = a.getDuration();
		int counter = 0;
		int res[];

		while (duration > 0) {
			if (availableRes.get(start+counter) != null) {
				res = availableRes.get(start+counter);
				for (int r=0; r<project.getUsedResources(); r++) {
					if (res[r]-a.getResource(r) < 0) {
						start += counter;
						counter = 0;
						duration = a.getDuration();
						break;
					}
				}
			}
			counter++;
			duration--;
		}
		return start;
	}
	
	private void serialize(List<Activity> e, List<Activity> c, Map<Integer,int[]> availableRes) {
		List<Activity> elected = new ArrayList<Activity>(e);
		List<Activity> completed = new ArrayList<Activity>(c);
    Activity current = leastLFT(elected);
		project.getView().printDebugln("A: " + current.getName());
		elected.remove(current);
		int startDay = latestFinishingTime(current.getPredecessors());
		startDay = nextAvailable(current, startDay, availableRes);
		current.setStartDay(startDay);
		completed.add(current);

		//update resources
		for (int r=0; r<project.getUsedResources(); r++) {
			if (availableRes.get(startDay) != null)
				availableRes.get(startDay)[r] -= current.getResource(r);
			else
				System.out.println("Unknown start: " + startDay);
		}
		int[] lastUsage = new int[this.numberOfRes];
		System.arraycopy(project.getResourceLimits(), 0, lastUsage, 0, this.numberOfRes);
		for (int i=startDay; i<startDay+current.getDuration(); i++) {
			if (availableRes.get(i) != null) {
				for (int r=0; r<project.getUsedResources(); r++) {
					System.arraycopy(availableRes.get(i), 0, lastUsage, 0, this.numberOfRes);
					availableRes.get(i)[r] -= current.getResource(r);
				}
			}
		}
		if (availableRes.get(startDay+current.getDuration()) == null) {
			availableRes.put(startDay+current.getDuration(), lastUsage);
		}
		
		// updated elected
		Iterator<Activity> it = current.getSuccessors().iterator();
		while(it.hasNext()) {
			Activity a = it.next();
			Iterator<Activity> it2 = a.getPredecessors().iterator();
			boolean add = true;
			while(it2.hasNext()) {
				if (!completed.contains(it2.next()))
					add = false;
			}
			if (add) elected.add(a); 
		}
		
		// recursive call
		if (elected.size() > 0)
			serialize(elected, completed, availableRes);
	}
	
	public void serialization() {
		Map<Integer,int[]> availableRes = new HashMap<Integer,int[]>();
		int resLimits[] = new int[this.numberOfRes];
		System.arraycopy(project.getResourceLimits(), 0, resLimits, 0, this.numberOfRes);
		availableRes.put(0, resLimits);
		if (project.getCriticalPath().computeCriticalPath()) {
			serialize(project.getActivityByName("START").getSuccessors(), new ArrayList<Activity>(), availableRes);
			project.calculateEndDate();
			project.setActivityDates();
		}
 	}

}
