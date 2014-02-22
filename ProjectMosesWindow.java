import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//Class for Window Events
class ProjectMosesWindow extends WindowAdapter
{ 
	ProjectMoses a;
	
	public ProjectMosesWindow(ProjectMoses f)
	{ 
		a=f;
	}
	
	public void windowClosing(WindowEvent e)
	{ 
		a.flag=true;
		a.t=null;
		System.exit(0);
	}
}
