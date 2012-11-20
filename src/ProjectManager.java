//import java.util.*;

//GUI stuff
//import javax.swing.*;
//import java.awt.*;
import java.awt.event.*;

/*TODO
 * return true or false from compute critical path and optimize computation
 * graphical output?
 * multiprojects ??
 * import/export from text file ??
 */

public class ProjectManager implements ActionListener{
	
	//private static Calendar pCal;
	private Project myProject;
	private MainWindow view;
	private boolean editActivity = false;
	private int selectedRow;
	
	public ProjectManager() {
		this.view = new MainWindow(this);
		myProject = new Project("Test Project", view, 4);
		view.printDebugln("### Welcome to Project Manager PRO ###");
		//view.printDebugln("Today is " + pCal.getTime());
		// Test case:
		String[] preds = new String[0];
		myProject.addActivity("A", 5, preds, null);
		myProject.addActivity("B", 3, preds, null);
		myProject.addActivity("C", 4, preds, null);
		preds = new String[2];
		preds[0] = "A";
		preds[1] = "B";
		myProject.addActivity("D", 2, preds, null);
		preds = new String[1];
		preds[0] = "C";
		myProject.addActivity("E", 1, preds, null);
		preds[0] = "A";
		myProject.addActivity("F", 2, preds, null);
		preds = new String[3];
		preds[0] = "D";
		preds[1] = "E";
		preds[2] = "F";
		myProject.addActivity("END", 0, preds, null);
		myProject.printActivities();
	}
	
  public void actionPerformed(ActionEvent e)
  {
    if("addActivity".equals(e.getActionCommand())) {
    	String name = view.getTxtName().getText();
    	if (editActivity == true) {
    		// save changes
    		String aName = (String)view.getTableModel().getValueAt(selectedRow, 0);
    		Activity a = myProject.getActivityByName(aName);
    		a.setName(name);
    		a.setDuration(Integer.parseInt(view.getTxtDuration().getText()));
    		if (!(view.getTxtPred().getText().equals("")))
    	    myProject.updatePredecessors(a, (view.getTxtPred().getText()).split(" "));
    		else
    			myProject.updatePredecessors(a, new String[0]);
	    	if (!(view.getTxtResources().getText().equals(""))) {
	    		String[] rString = (view.getTxtResources().getText().split(" "));
	    		for(int i=0; i<rString.length && i<myProject.getMaxNumOfResources(); i++) 
	    		  a.setResource(i, Integer.parseInt(rString[i]));
	    	}
	    	
    		view.clearInputFields();
    		view.addActivityButtonToSave(false);
    		view.editActivityButtonToAbort(false);
    		editActivity = false;
    	}
    	else {
	    	view.printDebugln("Add activity " + name + ".");
	    	int duration = Integer.parseInt(view.getTxtDuration().getText());
	    	String[] predecessors;
	    	if (!(view.getTxtPred().getText().equals("")))
	    		predecessors = (view.getTxtPred().getText()).split(" ");
	    	else
	    		predecessors = new String[0];
	    	int[] resources = new int[myProject.getMaxNumOfResources()];
	    	if (!(view.getTxtResources().getText().equals(""))) {
	    		String[] rString = (view.getTxtResources().getText().split(" "));
	    		for(int i=0; i<rString.length && i<myProject.getMaxNumOfResources(); i++) 
	    			resources[i] = Integer.parseInt(rString[i]);
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
    else if("editActivity".equals(e.getActionCommand())) {
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
    else if ("addArc".equals(e.getActionCommand())) {
    	String from = view.getTxtFrom().getText(); 
    	String to = view.getTxtTo().getText();
    	myProject.addArc(from, to);
    }
    else if ("removeArc".equals(e.getActionCommand())) {
    	String from = view.getTxtFrom().getText(); 
    	String to = view.getTxtTo().getText();
    	myProject.removeArc(from, to);
    }
    else if("addResource".equals(e.getActionCommand())) {
    	myProject.addResource();
    }
    else if("deleteResource".equals(e.getActionCommand())) {
    	myProject.deleteResource();
    }
    else if ("criticalPath".equals(e.getActionCommand())) {
    	myProject.getCriticalPath().computeCriticalPath();
    }
    else if("showCal".equals(e.getActionCommand())) {
    	myProject.getProjectCalendar().openCalendarWindow();
    }
    myProject.printActivities();
  }
	
	public static void main(String[] args) {
	//	pCal = Calendar.getInstance();
		new ProjectManager();
	}

}
