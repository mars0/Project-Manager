import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;


public class ResourceManager implements ActionListener {
	private Project project;
	private ResourceWindow rWindow;
	private MaxResourceSetter maxResWindow;
	
	public ResourceManager(Project p) {
		this.project = p;
	}
	
	public Project getProject() {
		return this.project;
	}
	
  public void actionPerformed(ActionEvent e) {
  	
  	if("Cancel".equals(e.getActionCommand())) {
  		maxResWindow.close();
  	}
  	else if("OK".equals(e.getActionCommand())) {
    	if (!(maxResWindow.getTextField().getText().equals(""))) {
    		String[] rString = (maxResWindow.getTextField().getText()).split(" ");
    		for(int i=0; i<rString.length && i<project.getMaxNumOfResources(); i++) {
    			int resLimit;
    			try {
    				resLimit = new Integer(rString[i]);
    			} catch (NumberFormatException ex) {
    				resLimit = project.getResourceLimits()[i];
    				project.getView().printDebugln("Cannot change resource limit " + (i+1) + ": wrong number format.");
    			}
    			project.setResLimit(i, resLimit);
    		}
    		maxResWindow.close();
    	}
  	}
  }
  
  public int[][] calcDailyResources() {
  	int res[][];
  	int length = project.getLength();
  	if (length > 0) { 
  		res = new int[length][project.getUsedResources()];
  		// for each day and each activity
  		for (int day=0; day<length; day++) {
  			Iterator<Activity> it = project.getActivities().iterator();
  			while (it.hasNext()) {
  				Activity a = it.next();
  				// check if activity is in execution on that day
  				if (a.getStartDay() <= day && day < a.getStartDay()+a.getDuration()) {
  					// add resources
  					for (int r=0; r<project.getUsedResources(); r++)
  						res[day][r] += a.getResource(r);
  				}
  			}
  		}
  	}
  	else {
  		res = new int[0][0];
  	}
  	return res;
  }
  
  public boolean inResourceLimits() {
  	// get daily usage of resources (sum)
  	int[][] dailyRes = calcDailyResources();
  	// for each day and each resource
  	for (int day=0; day<project.getLength(); day++) {
  		for (int r=0; r<project.getUsedResources(); r++) {
  			// check limits
  			if (dailyRes[day][r] > project.getResourceLimits()[r])
  				return false;
  		}
  	}
  	return true;
  }
  
  public void openResourceWindow() {
  	if (this.rWindow == null) {
  		this.rWindow = new ResourceWindow(this);
  	  rWindow.openWindow();
  	}
  	else {
  		rWindow.openWindow();
  	}
  }
  
  public void openMaxResWindow() {
  	String currentLimits = "";
  	for (int i=0; i<project.getUsedResources(); i++) {
  		currentLimits += project.getResourceLimits()[i] + " ";
  	}
  	
  	if (this.maxResWindow == null) {
  		this.maxResWindow = new MaxResourceSetter(this);
  		maxResWindow.openWindow(currentLimits);
  	}
  	else {
  		maxResWindow.openWindow(currentLimits);
  	}
  }
}
