import java.awt.*;
import javax.swing.*;

public class ResourceChart extends JPanel {
  private int[][] dailyResources;
  private int[] resLimit;
  private String title;
  private Color[] colors;

  public ResourceChart(int[][] val, int[] resLimit, String t) {
	  this.dailyResources = val;
	  this.resLimit = resLimit;
	  this.title = t;
	  this.colors = new Color[]{Color.blue, Color.green, Color.magenta, 
	  		Color.yellow, Color.cyan, Color.orange, Color.gray, Color.darkGray};
  }
  
  public void paintComponent(Graphics graphics) {
	  super.paintComponent(graphics);
	  if (dailyResources == null || dailyResources.length == 0)
	  	return;
	  
	  double mindailyResources = 0;
	  double maxdailyResources = 0;
	  
	  for (int i=0; i < dailyResources.length; i++) {
	  	for (int j=0; j < dailyResources[i].length; j++) {
		  	if (mindailyResources > dailyResources[i][j])
		  			mindailyResources = dailyResources[i][j];
		  	if (maxdailyResources < dailyResources[i][j])
		  			maxdailyResources = dailyResources[i][j];
	  	}
	  }
	  Dimension dim = getSize();
	  int clientWidth = dim.width;
	  int clientHeight = dim.height;
	  int dayWidth = clientWidth / dailyResources.length;
	  int barWidth = dayWidth / dailyResources[0].length;
	  		
	  Font titleFont = new Font("Book Antiqua", Font.BOLD, 15);
	  FontMetrics titleFontMetrics = graphics.getFontMetrics(titleFont);
	  Font labelFont = new Font("Book Antiqua", Font.BOLD, 15);
	  FontMetrics labelFontMetrics = graphics.getFontMetrics(labelFont);
	  
	  int titleWidth = titleFontMetrics.stringWidth(title);
	  int q = titleFontMetrics.getAscent();
	  int p = (clientWidth - titleWidth) / 2;
	  
	  graphics.setFont(titleFont);
	  graphics.drawString(title, p, q);
	  
	  int top = titleFontMetrics.getHeight();
	  int bottom = labelFontMetrics.getHeight();
	  
	  if (maxdailyResources == mindailyResources)
	  	return;
	  
	  double scale = (clientHeight - top - bottom) / (maxdailyResources - mindailyResources);
	  q = clientHeight - labelFontMetrics.getDescent();
	  graphics.setFont(labelFont);
	  for (int i=0; i < dailyResources.length; i++) {
	  	for (int j=0; j < dailyResources[i].length; j++) {
		  	int dailyResourcesP = (i * dailyResources[i].length + j) * barWidth + 1;
		  	int dailyResourcesQ = top;
		  	int height = (int) (dailyResources[i][j] * scale);
		  	if (dailyResources[i][j] >= 0)
		  		dailyResourcesQ += (int) ((maxdailyResources - dailyResources[i][j]) * scale);
		  	else {
		  		dailyResourcesQ += (int) (maxdailyResources * scale);
		  		height = -height;
		  	}
		  	for (int r=0; r<dailyResources[i][j]; r++) {
		  		if ((dailyResources[i][j]-r) <= resLimit[j])
		  			graphics.setColor(colors[j%colors.length]);
		  		else
		  			graphics.setColor(Color.red);
		  		graphics.fillRect(dailyResourcesP, dailyResourcesQ + (int)(r*scale), barWidth - 2, (int)scale);
		  		graphics.setColor(Color.black);
		  		graphics.drawRect(dailyResourcesP, dailyResourcesQ + (int)(r*scale), barWidth - 2, (int)scale);
		  	}
	  	}
		  int labelWidth = labelFontMetrics.stringWidth(((Integer)i).toString());
		  p = i * dayWidth + (dayWidth - labelWidth) / 2;
		  graphics.drawString(" "+((Integer)(i+1)).toString()+" ", p, q);
	  }
	}
}