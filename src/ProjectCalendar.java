import java.util.*;
import java.awt.event.*;

public class ProjectCalendar implements ActionListener{
	private static Calendar pCal;
	private CalendarView cView;
	private Map<Integer,Boolean> customDays;
	
	public ProjectCalendar() {
		pCal = Calendar.getInstance();
		customDays = new HashMap<Integer,Boolean>();
	}
	
	private void addCustomDay(Integer year, Integer month, Integer day, Boolean isHoliday) {
		String sYear, sMonth, sDay, sDate;
		sYear = year.toString();
		if (month < 10)
			sMonth = "0" + month.toString();
		else
			sMonth = month.toString();
		if (day < 10) 
			sDay = "0" + day.toString();
		else
			sDay = day.toString();
		sDate = sYear + sMonth + sDay;
		Integer date = Integer.parseInt(sDate);
		customDays.put(date, isHoliday);
	}
	
	public boolean isHoliday(String day) {
		boolean res = false;
		if (day.length() == 0) {
			res = false;
		}
		else {
			pCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
			if (pCal.get(Calendar.DAY_OF_WEEK) == 1 || pCal.get(Calendar.DAY_OF_WEEK) == 7) {
				res = true;
			}
		}
		return res;
	}
	
	public void printDays() {
		// clear table
		cView.getTableModel().setRowCount(0);
		pCal.set(Calendar.DAY_OF_MONTH, 1);
		// set labels
		cView.getLblMonth().setText(pCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US));
		Integer year = pCal.get(Calendar.YEAR);
		cView.getLblYear().setText(year.toString());
		// Sunday == 1, Monday == 2 etc.
		int column = (pCal.get(Calendar.DAY_OF_WEEK) + 5) % 7;
		int row = 0;
		int numberOfDays = pCal.getActualMaximum(Calendar.DATE);
		int currentDay = 1;
		boolean firstColumn = true;
		while(numberOfDays > 0) {
			if (firstColumn) {
				firstColumn = false;
				cView.getTableModel().addRow(new Object[]{"", "", "", "", "", "", ""});
			}
			else if (column == 0) {
				cView.getTableModel().addRow(new Object[]{"", "", "", "", "", "", ""});
				row++;
			}
			cView.getTableModel().setValueAt(currentDay, row, column);
			column = (column + 1) % 7;
			currentDay++;
			numberOfDays--;
		}
		// render cells
		cView.renderCell();
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
  	else if("btnAdd".equals(e.getActionCommand())) {

  		//addCustomDay(Integer year, Integer month, Integer day, true);
  	}
  	else if("btnDelete".equals(e.getActionCommand())) {
  		
  	}
  	printDays();
  }
  
  public void openCalendarWindow() {
  	if (this.cView == null)
  		this.cView = new CalendarView(this);
  	else
  		cView.openWindow();
  	printDays();
  }

}
