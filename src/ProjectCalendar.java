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
	
	public Calendar getNextWorkDay(Calendar date, int offset) {
		Calendar workDay = Calendar.getInstance();
		workDay.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		// compute next date 
		while(offset > 0 || isHoliday(((Integer)workDay.get(Calendar.DAY_OF_MONTH)).toString(), workDay)) {
		  workDay.add(Calendar.DAY_OF_MONTH, 1);
			if(!isHoliday(((Integer)workDay.get(Calendar.DAY_OF_MONTH)).toString(), workDay))
				offset--;
		}
		return workDay;
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
	
	public boolean isHoliday(String day, Calendar refCal) {
		Calendar currCal = Calendar.getInstance();
		if(refCal == null)
			currCal.set(pCal.get(Calendar.YEAR), pCal.get(Calendar.MONTH), Integer.parseInt(day));
		else
			currCal.set(refCal.get(Calendar.YEAR), refCal.get(Calendar.MONTH), Integer.parseInt(day));
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
	
	public boolean isEndDate(String value) {
		Calendar endDate = project.getEndDate();
		if (endDate != null && 
				Integer.parseInt(value) == endDate.get(Calendar.DAY_OF_MONTH) && 
				pCal.get(Calendar.MONTH) == endDate.get(Calendar.MONTH) && 
				pCal.get(Calendar.YEAR) == endDate.get(Calendar.YEAR))
			return true;
		return false;
	}
	
	public void printDays() {
		// clear table
		cView.getTableModel().setRowCount(0);
		pCal.set(Calendar.DAY_OF_MONTH, 1);
		// set label
		Integer year = pCal.get(Calendar.YEAR);
		cView.getLblMonth().setText(pCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) +
				"   " + year.toString());
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
	
	public Calendar calculateEndDate(Calendar startDate, int length) {
		Calendar endDate = null;
		if (startDate != null) {
			endDate = Calendar.getInstance();
			endDate.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
			while(length > 0) {
			  endDate.add(Calendar.DAY_OF_MONTH, 1);
				if(!isHoliday(((Integer)endDate.get(Calendar.DAY_OF_MONTH)).toString(), endDate))
					length--;
			}
		}
		return endDate;
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
      	if(!isStartDate(day.toString())) {
      		addCustomDay(pCal.get(Calendar.YEAR), pCal.get(Calendar.MONTH), day, true);
      		project.calculateEndDate();
      	}
      } catch (ClassCastException excep) {}
      catch (ArrayIndexOutOfBoundsException excep) {}
  	}
  	else if("deleteHoliday".equals(e.getActionCommand())) {
  		int row = cView.getTable().getSelectedRow();
      int col = cView.getTable().getSelectedColumn();
      try {
      	Integer day = (Integer)cView.getTableModel().getValueAt(cView.getTable().convertRowIndexToModel(row), cView.getTable().convertColumnIndexToModel(col));
      	if(!isStartDate(day.toString())) {
      		addCustomDay(pCal.get(Calendar.YEAR), pCal.get(Calendar.MONTH), day, false);
      		project.calculateEndDate();
      	}
      } catch (ClassCastException excep) {}
      catch (ArrayIndexOutOfBoundsException excep) {}
  	}
  	else if("setStartDate".equals(e.getActionCommand())) {
  		int row = cView.getTable().getSelectedRow();
      int col = cView.getTable().getSelectedColumn();
      try {
	      Integer day = (Integer)cView.getTableModel().getValueAt(cView.getTable().convertRowIndexToModel(row), cView.getTable().convertColumnIndexToModel(col));
	      Calendar date = Calendar.getInstance();
	      date.set(pCal.get(Calendar.YEAR), pCal.get(Calendar.MONTH), day);
	      if(!isHoliday(day.toString(), null)) {
	      	project.setStartDate(date);
	      	//System.out.println("End date: " + project.calculateEndDate().getTime());
	      }
      } catch (ClassCastException excep) {}
      catch (ArrayIndexOutOfBoundsException excep) {}
	  }
  	else if("showStart".equals(e.getActionCommand())) {
  		Calendar sDate = project.getStartDate();
  		if(sDate.get(Calendar.MONTH) == pCal.get(Calendar.MONTH) && sDate.get(Calendar.YEAR) == pCal.get(Calendar.YEAR)) {
  			return;
  		}
  		else if(pCal.get(Calendar.YEAR) < sDate.get(Calendar.YEAR) || (pCal.get(Calendar.YEAR) <= sDate.get(Calendar.YEAR) && pCal.get(Calendar.MONTH) < sDate.get(Calendar.MONTH))) {
  			do {
  	  		pCal.set(Calendar.MONTH, (pCal.get(Calendar.MONTH)+1)%12);
  	  		if(pCal.get(Calendar.MONTH) == 0 )
  	  			pCal.set(Calendar.YEAR, pCal.get(Calendar.YEAR)+1);
  			} while (sDate.get(Calendar.MONTH) != pCal.get(Calendar.MONTH) || sDate.get(Calendar.YEAR) != pCal.get(Calendar.YEAR));
  		}
  		else {
  			do {
  	  		pCal.set(Calendar.MONTH, (pCal.get(Calendar.MONTH)+11)%12);
  	  		if(pCal.get(Calendar.MONTH) == 11 )
  	  			pCal.set(Calendar.YEAR, pCal.get(Calendar.YEAR)-1);
  			} while (sDate.get(Calendar.MONTH) != pCal.get(Calendar.MONTH) || sDate.get(Calendar.YEAR) != pCal.get(Calendar.YEAR));
  		}
  	}
  	project.calculateEndDate();
  	project.setActivityDates();
  	project.printActivities();
  	printDays();
  }
  
  public void openCalendarWindow() {
  	if (this.cView == null)
  		this.cView = new CalendarView(this);
  	else {
  		cView.openWindow();
  		//pCal = Calendar.getInstance(); // reset to current date
  	}
  	project.calculateEndDate();
  	printDays();
  }

}
