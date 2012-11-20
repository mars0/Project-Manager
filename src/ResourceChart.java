import java.awt.*;
import javax.swing.*;

public class ResourceChart extends JPanel {
  private int[] dailyResources;
  private String title;

  public ResourceChart(int[] val, String t) {
	  this.dailyResources = val;
	  this.title = t;
  }
  
  public void paintComponent(Graphics graphics) {
	  super.paintComponent(graphics);
	  if (dailyResources == null || dailyResources.length == 0)
	  	return;
	  
	  double mindailyResources = 0;
	  double maxdailyResources = 0;
	  
	  for (int i = 0; i < dailyResources.length; i++) {
	  	if (mindailyResources > dailyResources[i])
	  			mindailyResources = dailyResources[i];
	  	if (maxdailyResources < dailyResources[i])
	  			maxdailyResources = dailyResources[i];
	  }
	  Dimension dim = getSize();
	  int clientWidth = dim.width;
	  int clientHeight = dim.height;
	  int barWidth = clientWidth / dailyResources.length;
	  
	  Font titleFont = new Font("Book Antiqua", Font.BOLD, 15);
	  FontMetrics titleFontMetrics = graphics.getFontMetrics(titleFont);
	  Font labelFont = new Font("Book Antiqua", Font.PLAIN, 10);
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
	  for (int j = 0; j < dailyResources.length; j++) {
	  	int dailyResourcesP = j * barWidth + 1;
	  	int dailyResourcesQ = top;
	  	int height = (int) (dailyResources[j] * scale);
	  	if (dailyResources[j] >= 0)
	  		dailyResourcesQ += (int) ((maxdailyResources - dailyResources[j]) * scale);
	  	else {
	  		dailyResourcesQ += (int) (maxdailyResources * scale);
	  		height = -height;
	  	}
	  	graphics.setColor(Color.blue);
		  graphics.fillRect(dailyResourcesP, dailyResourcesQ, barWidth - 2, height);
		  graphics.setColor(Color.black);
		  graphics.drawRect(dailyResourcesP, dailyResourcesQ, barWidth - 2, height);
		  
		  int labelWidth = labelFontMetrics.stringWidth(((Integer)j).toString());
		  p = j * barWidth + (barWidth - labelWidth) / 2;
		  graphics.drawString(((Integer)j).toString(), p, q);
	  }
	}
}