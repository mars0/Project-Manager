import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;


public class ResourceManager implements ActionListener{
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
  	
  	if("cancel".equals(e.getActionCommand())) {
  		maxResWindow.close();
  	}
  	else if("ok".equals(e.getActionCommand())) {
  		//TODO read new limits
  	}

  }
  
  public int[][] calcDailyResources() {
  	int res[][];
  	int length = project.getLength();
  	if (length > 0) { 
  		res = new int[length][project.getUsedResources()];
  		for (int day=0; day<length; day++) {
  			Iterator<Activity> it = project.getActivities().iterator();
  			while (it.hasNext()) {
  				Activity a = it.next();
  				if (a.getStartDay() <= day && day < a.getStartDay()+a.getDuration()) {
  					for (int r=0; r<project.getUsedResources(); r++)
  						res[day][r] += a.getResource(r);
  				}
  			}
  		}
  		/* debug output
  		for (int j=0; j<res.length; j++)
  			project.getView().printDebugln("res["+j+"] = " + res[j]); */
  	}
  	else {
  		res = new int[0][0];
  	}
  	return res;
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
