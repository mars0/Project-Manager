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
		// Test case PRACTICA 6:
		String[] preds = new String[0];
		myProject.addActivity("A", 5, preds, new int[]{4, 6, 0, 0});
		myProject.addActivity("B", 1, preds, new int[]{5, 0, 3, 0});
		myProject.addActivity("C", 5, preds, new int[]{0, 5, 5, 0});
		preds = new String[1];
		preds[0] = "B";
		myProject.addActivity("D", 6, preds, new int[]{2, 1, 0, 0});
		preds = new String[2];
		preds[0] = "A";
		preds[1] = "C";
		myProject.addActivity("E", 5, preds, new int[]{1, 5, 0, 0});
		preds[1] = "B";
		myProject.addActivity("F", 4, preds, new int[]{0, 3, 0, 6});
		preds = new String[3];
		preds[0] = "A";
		preds[1] = "B";
		preds[2] = "C";
		myProject.addActivity("G", 7, preds, new int[]{0, 2, 5, 0});
		preds = new String[4];
		preds[0] = "D";
		preds[1] = "E";
		preds[2] = "F";
		preds[3] = "G";
		myProject.addActivity("H", 2, preds, new int[]{0, 0, 3, 3});
		preds = new String[1];
		preds[0] = "E";
		myProject.addActivity("I", 2, preds, new int[]{3, 0, 0, 4});
		preds[0] = "G";
		myProject.addActivity("J", 2, preds, new int[]{2, 0, 0, 1});
		preds = new String[3];
		preds[0] = "H";
		preds[1] = "I";
		preds[2] = "J";
		myProject.addActivity("END", 0, preds, null);
		/*
		myProject.addActivity("A", 5, preds, new int[]{3, 2, 0, 0});
		myProject.addActivity("B", 5, preds, new int[]{2, 4, 0, 0});
		preds = new String[1];
		preds[0] = "A";
		myProject.addActivity("C", 4, preds, new int[]{3, 1, 0, 0});
		myProject.addActivity("D", 2, preds, new int[]{4, 3, 0, 0});
		preds[0] = "D";
		myProject.addActivity("E", 3, preds, new int[]{2, 0, 0, 0});
		myProject.addActivity("F", 3, preds, new int[]{1, 1, 0, 0});
		preds[0] = "B";
		myProject.addActivity("G", 4, preds, new int[]{3, 1, 0, 0});
		preds = new String[2];
		preds[0] = "F";
		preds[1] = "G";
		myProject.addActivity("H", 3, preds, new int[]{2, 2, 0, 0});
		preds = new String[1];
		preds[0] = "C";
		myProject.addActivity("I", 3, preds, new int[]{3, 3, 0, 0});
		preds = new String[2];
		preds[0] = "E";
		preds[1] = "I";
		myProject.addActivity("J", 2, preds, new int[]{4, 1, 0, 0});
		preds = new String[1];
		preds[0] = "J";
		myProject.addActivity("K", 3, preds, new int[]{5, 4, 0, 0});
		preds = new String[2];
		preds[0] = "H";
		preds[1] = "K";
		myProject.addActivity("END", 0, preds, null);*/
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
    else if("addResource".equals(e.getActionCommand())) {
    	myProject.addResource();
    }
    else if("deleteResource".equals(e.getActionCommand())) {
    	myProject.deleteResource();
    }
    else if ("criticalPath".equals(e.getActionCommand())) {
    	if(!myProject.getCriticalPath().computeCriticalPath())
  			view.printDebugln("Cannot compute critical path: Activity graph is not valid.");
    }
    else if("showCal".equals(e.getActionCommand())) {
    	myProject.getProjectCalendar().openCalendarWindow();
    }
    else if("showRes".equals(e.getActionCommand())) {
    	myProject.getResourceManager().openResourceWindow();
    }
    else if("valid".equals(e.getActionCommand())) {
    	if (myProject.isValidSubGraph(myProject.getActivityByName("START"), null))
    		view.printDebugln("Graph is valid.");
    	else
    		view.printDebugln("Graph is not valid.");
    }
    else if("setRes".equals(e.getActionCommand())) {
    	myProject.getResourceManager().openMaxResWindow();
    }
    else if("sequence".equals(e.getActionCommand())) {
    	Sequencing seq = new Sequencing(myProject);
    	seq.serialization();
    }
    else if("equalize".equals(e.getActionCommand())) {
    	Equalizing eq = new Equalizing(myProject);
    	eq.equalize();
      view.printDebugln("Equalized resource usage.");
    }
    myProject.printActivities();
  }
	
	public static void main(String[] args) {
	//	pCal = Calendar.getInstance();
		new ProjectManager();
	}

}
