import java.awt.event.*;

public class ProjectManager implements ActionListener {
	
	private Project myProject;
	private MainWindow view;
	private boolean editActivity = false;
	private int selectedRow;
	
	public ProjectManager() {
		this.view = new MainWindow(this);
		this.myProject = new Project("Test Project", view, 4);
		view.printDebugln("### Welcome to Project Manager PRO ###");
		myProject.printActivities();
	}
	
  public void actionPerformed(ActionEvent e)
  {
    if ("addActivity".equals(e.getActionCommand())) {
    	String name = view.getTxtName().getText();
    	if (editActivity == true) {
    		// save changes
    		String aName = (String)view.getTableModel().getValueAt(selectedRow, 0);
    		Activity a = myProject.getActivityByName(aName);
    		a.setName(name);
    		try {
    			a.setDuration(new Integer(view.getTxtDuration().getText()));
    		} catch (NumberFormatException ex) {
    			view.printDebugln("Cannot change duration of Activity " + name + ": wrong number format.");
    		}
    		if (!(view.getTxtPred().getText().equals("")))
    	    myProject.updatePredecessors(a, (view.getTxtPred().getText()).split(" "));
    		else
    			myProject.updatePredecessors(a, new String[0]);
	    	if (!(view.getTxtResources().getText().equals(""))) {
	    		String[] rString = (view.getTxtResources().getText().split(" "));
	    		for(int i=0; i<rString.length && i<myProject.getMaxNumOfResources(); i++) {
	    			int res;
	    			try {
	    				res = new Integer(rString[i]);
	      		} catch (NumberFormatException ex) {
	      			res = a.getResource(i);
	      			view.printDebugln("Cannot change resource "+ (i+1) + " of Activity " + name + ": wrong number format.");
	      		}
	    			if (res >= 0 && res <= myProject.getResourceLimits()[i])
	    				a.setResource(i, res);
	    			else
	    				view.printDebugln("Cannot edit resource " + (i+1) + ": please check resource limits.");
	    		}
	    	}
	    	
    		view.clearInputFields();
    		view.addActivityButtonToSave(false);
    		view.editActivityButtonToAbort(false);
    		editActivity = false;
    	}
    	else {
	    	view.printDebugln("Add activity " + name + ".");
	    	int duration = 0;
	    	try {
	    		duration = new Integer(view.getTxtDuration().getText());
	    	} catch (NumberFormatException ex) {
	    		view.printDebugln("Cannot set duration of Activity " + name + ": wrong number format.");
	    	}
	    	String[] predecessors;
	    	if (!(view.getTxtPred().getText().equals("")))
	    		predecessors = (view.getTxtPred().getText()).split(" ");
	    	else
	    		predecessors = new String[0];
	    	int[] resources = new int[myProject.getMaxNumOfResources()];
	    	if (!(view.getTxtResources().getText().equals(""))) {
	    		String[] rString = (view.getTxtResources().getText().split(" "));
	    		for(int i=0; i<rString.length && i<myProject.getMaxNumOfResources(); i++) { 
	    			int res = 0;
	    			try {
	    				res = new Integer(rString[i]);
	    			} catch (NumberFormatException ex) {
	      			view.printDebugln("Cannot set resource "+ (i+1) + " of Activity " + name + ": wrong number format.");
	      		}	
	    			if (res >= 0 && res <= myProject.getResourceLimits()[i])
	    				resources[i] = res;
	    			else
	    				view.printDebugln("Cannot add resource " + (i+1) + ": please check resource limits.");
	    		}
	    	}
	    	myProject.addActivity(name, duration, predecessors, resources);
    	}
    }
    else if ("deleteActivity".equals(e.getActionCommand())) {
    	int[] selectedRows = view.getTable().getSelectedRows();
    	if (selectedRows.length > 0) {
	    	view.printDebugln("Delete selected activities.");
	    	for (int i=0; i < selectedRows.length; i++) {
	    		int row = view.getTable().convertRowIndexToModel(selectedRows[i]);
	    		String aName = (String)view.getTableModel().getValueAt(row, 0);
	    		if (!aName.equals("START")) 
	    			myProject.deleteActivityByName(aName);
	    	}
    	}
    }
    else if ("editActivity".equals(e.getActionCommand())) {
    	if (editActivity == true) {
    		// abort editing
    		view.clearInputFields();
    		// set buttons to default label
    		view.addActivityButtonToSave(false);
    		view.editActivityButtonToAbort(false);
    		editActivity = false;
    	}
    	else {
	    	int[] selectedRows = view.getTable().getSelectedRows();
	    	if (selectedRows.length == 1) {
	    		selectedRow = view.getTable().convertRowIndexToModel(selectedRows[0]);
	    		String aName = (String)view.getTableModel().getValueAt(selectedRow, 0);
	    		if ("START".equals(aName)) {
	    			view.printDebugln("Cannot edit the START activity.");
	    			return;
	    		}
	    		Activity a = myProject.getActivityByName(aName);
	    		view.getTxtName().setText(a.getName());
	    		view.getTxtDuration().setText(a.getDuration().toString());
	    		String preds = "";
	    		for (Activity c: a.getPredecessors()) {
	    			preds += c.getName() + " "; 
	    		}
	    		if (preds.length() > 1)
	    			preds = preds.substring(0, preds.length()-1); //delete last blank
	    		view.getTxtPred().setText(preds);
	    		String resString = "";
	    		for (int i=view.getColumnOffset(); i<view.getTableModel().getColumnCount(); i++) {
	    			resString += view.getTableModel().getValueAt(selectedRow, i) + " ";
	    		}
	    		resString = resString.substring(0, resString.length()-1); //delete last blank
	    		view.getTxtResources().setText(resString);
	    		// change button labels
	    		view.addActivityButtonToSave(true);
	    		view.editActivityButtonToAbort(true);
	    		editActivity = true;
	    	}
	    	else {
	    		view.printDebugln("Cannot edit more than one activity at a time.");
	    	}
    	}
    }
    else if ("addResource".equals(e.getActionCommand())) {
    	myProject.addResource();
    }
    else if ("deleteResource".equals(e.getActionCommand())) {
    	myProject.deleteResource();
    }
    else if ("criticalPath".equals(e.getActionCommand())) {
    	if(!myProject.getCriticalPath().computeCriticalPath())
  			view.printDebugln("Cannot compute critical path: Activity graph is not valid.");
    	/*print times
    	for (int i=0; i<myProject.getActivities().size(); i++) {
    		Activity a = ((ArrayList<Activity>)myProject.getActivities()).get(i);
    		view.printDebugln(a.getName() + ": " + a.getTimeMin() + ", " + a.getTimeMax());
    	}*/
    }
    else if ("showCal".equals(e.getActionCommand())) {
    	myProject.getProjectCalendar().openCalendarWindow(myProject.getActivityByName("START"));
    }
    else if ("showRes".equals(e.getActionCommand())) {
    	myProject.getResourceManager().openResourceWindow();
    }
    else if ("valid".equals(e.getActionCommand())) {
    	if (myProject.isValidSubGraph(myProject.getActivityByName("START"), null))
    		view.printDebugln("Graph is valid.");
    	else
    		view.printDebugln("Graph is not valid.");
    }
    else if ("setRes".equals(e.getActionCommand())) {
    	myProject.getResourceManager().openMaxResWindow();
    }
    else if ("sequence".equals(e.getActionCommand())) {
    	Sequencing seq = new Sequencing(myProject);
    	seq.serialization();
    }
    else if ("equalize".equals(e.getActionCommand())) {
    	Equalizing eq = new Equalizing(myProject);
    	if (eq.equalize()) 
    		view.printDebugln("Equalized resource usage.");
    	else
  			view.printDebugln("Cannot equalize ressource usage: please check project schedule.");
    }
    else if ("changeDate".equals(e.getActionCommand())) {
    	int[] selectedRows = view.getTable().getSelectedRows();
    	// can only edit one activity date at a time
    	if (selectedRows.length == 1) {
    		selectedRow = view.getTable().convertRowIndexToModel(selectedRows[0]);
    		String aName = (String)view.getTableModel().getValueAt(selectedRow, 0);
    		if (aName.equals("END")) {
    			view.printDebugln("Cannot edit date of END activity.");
    		}
    		// start activity is a special case == start of the project
    		else if (aName.equals("START")){
    			myProject.getProjectCalendar().openCalendarWindow(myProject.getActivityByName(aName));
    		}
    		// edit other activities only if successor relations are correct
    		else if (myProject.hasValidSchedule(myProject.getActivityByName("START"), null)) {
    			Activity a = myProject.getActivityByName(aName);
    			myProject.getProjectCalendar().openCalendarWindow(a);
    		}
        else {
        	view.printDebugln("Cannot modifiy start date: please check project schedule.");
        }
    	}
    	else
    		view.printDebugln("Please select exactly one activity from the table.");
    }
    myProject.printActivities();
  }
	
	public static void main(String[] args) {
		new ProjectManager();
	}

}
