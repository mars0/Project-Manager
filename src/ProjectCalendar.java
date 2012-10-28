import java.util.*;
import java.awt.event.*;

public class ProjectCalendar implements ActionListener{
	private static Calendar pCal;
	private CalendarView cView;
	private Project project;
	private Map<Integer,Boolean> customDays;
	
	public ProjectCalendar(Project p) {
		this.project = p;
		pCal = Calendar.getInstance();
		customDays = new HashMap<Integer,Boolean>();
	}
	
	private void addCustomDay(Integer year, Integer month, Integer day, Boolean isHoliday) {
		String sMonth, sDay, sDate;
		if (month < 10)
			sMonth = "0" + month.toString();
		else
			sMonth = month.toString();
		if (day < 10) 
			sDay = "0" + day.toString();
		else
			sDay = day.toString();
		sDate = year.toString() + sMonth + sDay;
		Integer date = Integer.parseInt(sDate);
		customDays.put(date, isHoliday);
	}
	
	private Integer getCustomDayKey(Calendar currDay) {
		String sMonth, sDay, sDate;
		Integer day = currDay.get(Calendar.DAY_OF_MONTH);
		Integer month = currDay.get(Calendar.MONTH);
		Integer year = currDay.get(Calendar.YEAR);
		if (month < 10)
			sMonth = "0" + month.toString();
		else
			sMonth = month.toString();
		if (day < 10) 
			sDay = "0" + day.toString();
		else
			sDay = day.toString();
		sDate = year.toString() + sMonth + sDay;
		Integer dateKey = Integer.parseInt(sDate);
		return dateKey;
	}
	
	public boolean isHoliday(String day) {
		Calendar currCal = Calendar.getInstance();
		currCal.set(pCal.get(Calendar.YEAR), pCal.get(Calendar.MONTH), Integer.parseInt(day));
		boolean res = false;
		if (day.length() == 0) {
			res = false;
		}
		else {
			Integer dateKey = getCustomDayKey(currCal);
			if (customDays.containsKey(dateKey)) {
				res = customDays.get(dateKey);
			}
			else if (currCal.get(Calendar.DAY_OF_WEEK) == 1 || currCal.get(Calendar.DAY_OF_WEEK) == 7) {
				// a normal week end day
				res = true;
			}
		}
		return res;
	}
	
	public boolean isStartDate(String value) {
		Calendar startDate = project.getStartDate();
		if (startDate != null && 
				Integer.parseInt(value) == startDate.get(Calendar.DAY_OF_MONTH) && 
				pCal.get(Calendar.MONTH) == startDate.get(Calendar.MONTH) && 
				pCal.get(Calendar.YEAR) == startDate.get(Calendar.YEAR))
			return true;
		return false;
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
  	else if("addHoliday".equals(e.getActionCommand())) {
  		int row = cView.getTable().getSelectedRow();
      int col = cView.getTable().getSelectedColumn();
      try {
      Integer day = (Integer)cView.getTableModel().getValueAt(cView.getTable().convertRowIndexToModel(row), cView.getTable().convertColumnIndexToModel(col));
      if(!isStartDate(day.toString())) 
      	addCustomDay(pCal.get(Calendar.YEAR), pCal.get(Calendar.MONTH), day, true);
      } catch (ClassCastException exception) {}
  	}
  	else if("deleteHoliday".equals(e.getActionCommand())) {
  		int row = cView.getTable().getSelectedRow();
      int col = cView.getTable().getSelectedColumn();
      try {
      Integer day = (Integer)cView.getTableModel().getValueAt(cView.getTable().convertRowIndexToModel(row), cView.getTable().convertColumnIndexToModel(col));
      if(!isStartDate(day.toString()))
      	addCustomDay(pCal.get(Calendar.YEAR), pCal.get(Calendar.MONTH), day, false);
      } catch (ClassCastException exception) {}
  	}
  	else if("setStartDate".equals(e.getActionCommand())) {
  		int row = cView.getTable().getSelectedRow();
      int col = cView.getTable().getSelectedColumn();
      try {
	      Integer day = (Integer)cView.getTableModel().getValueAt(cView.getTable().convertRowIndexToModel(row), cView.getTable().convertColumnIndexToModel(col));
	      Calendar date = Calendar.getInstance();
	      date.set(pCal.get(Calendar.YEAR), pCal.get(Calendar.MONTH), day);
	      if(!isHoliday(day.toString()))
	      	project.setStartDate(date);
      } catch (ClassCastException exception) {}
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
