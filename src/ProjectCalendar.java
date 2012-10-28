import java.util.*;
import java.awt.event.*;

public class ProjectCalendar implements ActionListener{
	private static Calendar pCal;
	private CalendarView cView;
	
	public ProjectCalendar() {
		pCal = Calendar.getInstance();
	}
	
	public void printDays(Calendar date) {
		// clear table
		cView.getTableModel().setRowCount(0);
		date.set(Calendar.DAY_OF_MONTH, 1);
		// set labels
		cView.getLblMonth().setText(date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US));
		Integer year = date.get(Calendar.YEAR);
		cView.getLblYear().setText(year.toString());
		// Sunday == 1, Monday == 2 etc.
		int column = (date.get(Calendar.DAY_OF_WEEK) + 5) % 7;
		int row = 0;
		int numberOfDays = date.getActualMaximum(Calendar.DATE);
		int currentDay = 1;
		boolean firstColumn = true;
		cView.getTableModel().addRow(new Object[]{});
		while(numberOfDays > 0) {
			if (firstColumn) {
				firstColumn = false;
				cView.getTableModel().addRow(new Object[]{});
			}
			else if (column == 0) {
				cView.getTableModel().addRow(new Object[]{});
				row++;
			}
			cView.getTableModel().setValueAt(currentDay, row, column);
			column = (column + 1) % 7;
			currentDay++;
			numberOfDays--;
		}
	}
	
  public void actionPerformed(ActionEvent e) {
  	if("prevMonth".equals(e.getActionCommand())) {
  		pCal.set(Calendar.MONTH, (pCal.get(Calendar.MONTH)+11)%12);
  		if(pCal.get(Calendar.MONTH) == 11 ) {
  			pCal.set(Calendar.YEAR, pCal.get(Calendar.YEAR)-1);
  		}
  	}
  	else if("nextMonth".equals(e.getActionCommand())) {
  		pCal.set(Calendar.MONTH, (pCal.get(Calendar.MONTH)+1)%12);
  		if(pCal.get(Calendar.MONTH) == 0 ) {
  			pCal.set(Calendar.YEAR, pCal.get(Calendar.YEAR)+1);
  		}
  	}
  	printDays(pCal);
  }
  
  public void openCalendarWindow() {
  	if (this.cView == null)
  		this.cView = new CalendarView(this);
  	else
  		cView.openWindow();
  	printDays(pCal);
  }

}
