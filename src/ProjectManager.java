import java.util.*;

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
	
	private static Calendar pCal;
	private Project myProject;
	private MainWindow view;
	
	public ProjectManager() {
		this.view = new MainWindow(this);
		myProject = new Project("Test Project", view);
		view.printDebugln("### Welcome to Project Manager PRO ###");
		view.printDebugln("Today is " + pCal.getTime());
		// Test case:
		String[] preds = new String[0];
		myProject.addActivity("A", 5, preds, 0);
		myProject.addActivity("B", 3, preds, 0);
		myProject.addActivity("C", 4, preds, 0);
		preds = new String[2];
		preds[0] = "A";
		preds[1] = "B";
		myProject.addActivity("D", 2, preds, 0);
		preds = new String[1];
		preds[0] = "C";
		myProject.addActivity("E", 1, preds, 0);
		preds[0] = "A";
		myProject.addActivity("F", 2, preds, 0);
		preds = new String[3];
		preds[0] = "D";
		preds[1] = "E";
		preds[2] = "F";
		myProject.addActivity("END", 0, preds, 0);
		myProject.printActivities();
	}
	
  public void actionPerformed(ActionEvent e)
  {
    if("addActivity".equals(e.getActionCommand())) {
    	String name = view.getTxtName().getText();
    	view.printDebugln("Add activity " + name + ".");
    	int duration = Integer.parseInt(view.getTxtDuration().getText());
    	String[] predecessors;
    	if (!(view.getTxtPred().getText().equals("")))
    		predecessors = (view.getTxtPred().getText()).split(" ");
    	else
    		predecessors = new String[0];
    	int resources = Integer.parseInt(view.getTxtResources().getText());
    	myProject.addActivity(name, duration, predecessors, resources);
    }
    else if ("deleteActivity".equals(e.getActionCommand())) {
    	view.printDebugln("Delete selected activities.");
    	int[] selectedRows = view.getTable().getSelectedRows();
    	for (int i=0; i < selectedRows.length; i++) {
    		int row = view.getTable().convertRowIndexToModel(selectedRows[i]);
    		String aName = (String)view.getTableModel().getValueAt(row, 0);
    		if (!aName.equals("START")) 
    			myProject.deleteActivityByName(aName);
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
    else if ("criticalPath".equals(e.getActionCommand())) {
    	myProject.getCriticalPath().computeCriticalPath();
    }
    myProject.printActivities();
  }
	
	public static void main(String[] args) {
		pCal = Calendar.getInstance();
		new ProjectManager();
	}

}
