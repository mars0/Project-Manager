import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileManager {
	private ProjectManager projectManager;
  private File file;
  
  public FileManager(ProjectManager pManager) {
  	this.projectManager = pManager;
  	this.file = null;
  }
	
  public boolean openFileChooserDialog(boolean saveFile) {
  	boolean success = false;
    JFileChooser fileopen = new JFileChooser();
    FileFilter filter = new FileNameExtensionFilter("*.projm files", ".projm");
    fileopen.addChoosableFileFilter(filter);

    int ret;
    if (saveFile) ret = fileopen.showDialog(null, "Save file");
    else ret = fileopen.showDialog(null, "Open file");

    if (ret == JFileChooser.APPROVE_OPTION) {
    	if (saveFile) {
    		String filename = fileopen.getSelectedFile().toString() + ".projm";
    		this.file = new File(filename);
    	}
    	else {
    		this.file = fileopen.getSelectedFile();
    	}
      System.out.println(file);
      success = true;
    }
    return success;
  }
  
	/*private void serialize(Project project) {
    try {
      ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
      os.writeObject(project.getActivities()); 
      os.close();
    }
    catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }
    catch (IOException e1) {
      e1.printStackTrace();
    }
  }
	
	private boolean deserialize() {
    ObjectInputStream is;
    boolean success = false;
    try {
      is = new ObjectInputStream(new FileInputStream(file));
      projectManager.setProject( new Project( (List<Activity>)is.readObject(), projectManager.getView() ));
      is.close();
      success = true;
    }
    catch (FileNotFoundException e1) {
      e1.printStackTrace();
      success = true;
    }
    catch (IOException e1) {
      e1.printStackTrace();
      success = true;
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace();
      success = true;
    }
    return success;
  }
  
  public void save(Project project) {
  	boolean success = true;
  	if (this.file == null) {
  		success = openFileChooserDialog(true);
  	}
  	if (success)
  		serialize(projectManager.getProject());
  }
  
  public void open() {
  	if (openFileChooserDialog(false))
  		deserialize();
  }*/
}
