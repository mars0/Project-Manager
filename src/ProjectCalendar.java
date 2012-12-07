import java.util.*;
import java.awt.event.*;

public class ProjectCalendar implements ActionListener{
	private static Calendar pCal;
	private Calendar startDate;
	private Calendar endDate;
	private CalendarView cView;
	private Project project;
	private Activity subject;
	private Map<Integer,Boolean> customDays;
	
	public ProjectCalendar(Project p) {
		this.project = p;
		pCal = Calendar.getInstance();
		customDays = new HashMap<Integer,Boolean>();
		this.startDate = p.getStartDate();
		this.endDate = p.getEndDate();
	}
	
	private boolean isEqual(Calendar c1, Calendar c2) {
		if(c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
			return true;
		}
		return false;
	}
	
	private void saveOldStarts(Activity a, Map<String,Integer> starts) {
		starts.put(a.getName(), a.getStartDay());
		// recursive call
		for (int i=0; i<a.getSuccessors().size(); i++) {
			if (!(starts.containsKey(a.getSuccessors().get(i).getName()))) {
				saveOldStarts(a.getSuccessors().get(i), starts);
			}
		}
	}
	
	public void revertChanges(Map<String,Integer> starts) {
		for (Map.Entry<String,Integer> entry : starts.entrySet()) {
			project.getActivityByName(entry.getKey()).setStartDay(entry.getValue());
		}
		project.setActivityDates();
		project.calculateEndDate();
		project.printActivities();
  	updateDates();
  	printDays();
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
	
	public int getOffset(Calendar from, Calendar to) {
		int offset = 0;
		if(isEqual(to, from)) {
			return offset;
		}
		else if(to.compareTo(from) < 0) {
			// to is before from
			while (!isEqual(to, from)) {
				to.add(Calendar.DAY_OF_MONTH, 1);
				if (!isHoliday(to))
					offset--;
			}
		}
		else {
			// to is after from
			while (!isEqual(to, from)) {
				to.add(Calendar.DAY_OF_MONTH, -1);
				if (!isHoliday(to))
					offset++;
			}
		}
		return offset;
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
	
	private void updateDates() {
  	this.startDate = null;
		this.endDate = null;
  	
		if (subject.getName().equals("START")) {
  		// we are considering the whole project
  		this.startDate = project.getStartDate();
  		this.endDate = project.getEndDate();
  	}
  	else if (!subject.getName().equals("END")) {
  		this.startDate = subject.getStartDate();
  		this.endDate = subject.getEndDate();
  	}
	}
	
	public boolean isHoliday(Calendar cal) {
		boolean res = false;
		Integer dateKey = getCustomDayKey(cal);
		if (customDays.containsKey(dateKey)) {
			res = customDays.get(dateKey);
		}
		else if (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7) {
			res = true;
		}
		return res;
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
		if (startDate != null && 
				Integer.parseInt(value) == startDate.get(Calendar.DAY_OF_MONTH) && 
				pCal.get(Calendar.MONTH) == startDate.get(Calendar.MONTH) && 
				pCal.get(Calendar.YEAR) == startDate.get(Calendar.YEAR))
			return true;
		return false;
	}
	
	public boolean isEndDate(String value) {
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
      		//project.calculateEndDate();
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
      		//project.calculateEndDate();
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
	      	if ("START".equals(subject.getName()))
	      		project.setStartDate(date);
	      	else {
	      		int newStart = subject.getStartDay() + getOffset(subject.getStartDate(), date);
	      		if (newStart >= subject.getLowerBound() && newStart != subject.getStartDay()) {
	      			//int oldStart = subject.getStartDay();
	      			Map<String,Integer> oldStarts = new HashMap<String,Integer>();
	      			saveOldStarts(subject, oldStarts);
	      			int oldDuration = project.getLength();
	      			//project.getView().printDebugln("1 Duration: " + project.getLength());
	      			subject.recursivlySetStartDay(newStart);
	      			project.setActivityDates();
	      			project.calculateEndDate();
	      			//project.getView().printDebugln("2 Duration: " + project.getLength());
	      			new ChangeDateDialog(this, project.getLength()-oldDuration, true, oldStarts);
	      		}
	      	}
	      }
      } catch (ClassCastException excep) {}
      catch (ArrayIndexOutOfBoundsException excep) {}
	  }
  	else if("showStart".equals(e.getActionCommand())) {
  		if(startDate.get(Calendar.MONTH) == pCal.get(Calendar.MONTH) && startDate.get(Calendar.YEAR) == pCal.get(Calendar.YEAR)) {
  			return;
  		}
  		else if(pCal.get(Calendar.YEAR) < startDate.get(Calendar.YEAR) || (pCal.get(Calendar.YEAR) <= startDate.get(Calendar.YEAR) && pCal.get(Calendar.MONTH) < startDate.get(Calendar.MONTH))) {
  			do {
  	  		pCal.set(Calendar.MONTH, (pCal.get(Calendar.MONTH)+1)%12);
  	  		if(pCal.get(Calendar.MONTH) == 0 )
  	  			pCal.set(Calendar.YEAR, pCal.get(Calendar.YEAR)+1);
  			} while (startDate.get(Calendar.MONTH) != pCal.get(Calendar.MONTH) || startDate.get(Calendar.YEAR) != pCal.get(Calendar.YEAR));
  		}
  		else {
  			do {
  	  		pCal.set(Calendar.MONTH, (pCal.get(Calendar.MONTH)+11)%12);
  	  		if(pCal.get(Calendar.MONTH) == 11 )
  	  			pCal.set(Calendar.YEAR, pCal.get(Calendar.YEAR)-1);
  			} while (startDate.get(Calendar.MONTH) != pCal.get(Calendar.MONTH) || startDate.get(Calendar.YEAR) != pCal.get(Calendar.YEAR));
  		}
  	}
  	project.calculateEndDate();
  	project.setActivityDates();
  	project.printActivities();
  	updateDates();
  	printDays();
  }
  
  public void openCalendarWindow(Activity a) {
  	this.subject = a;
  	updateDates();
  	
  	if (this.cView == null)
  		this.cView = new CalendarView(this);
  	else {
  		cView.openWindow();
  		pCal = Calendar.getInstance(); // reset to current date
  	}
  	project.calculateEndDate();
  	printDays();
  }

}
