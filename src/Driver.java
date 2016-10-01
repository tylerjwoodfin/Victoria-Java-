import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Driver 
{
	private static Label 	home_text = null;
	private static Text 	queryField;
	private static String 	query;

	public static void main(String[] args) throws IOException 
	{
		Display.setAppName("Victoria");
		Display display = new Display();
		Font victoriaFont = new Font(display, "Helvetica Neue", 34, SWT.NONE);
		Shell s = new Shell();
		s.setMinimumSize(600, 400);
		s.open();
		createMenu(s);
		createPanels(s, victoriaFont);
		s.setSize(600, 400);
	    
		//Keep at Bottom
		while(!s.isDisposed()) 
		{
			if(!s.getDisplay().readAndDispatch()) 
			{
				s.getDisplay().sleep();
			}
		}
	}
	
	private static void createMenu(final Shell s)
	{
		final Menu menubar = new Menu( s, SWT.BAR );
		
		//Mode Menu
		final MenuItem mode = new MenuItem( menubar, SWT.CASCADE );
		mode.setText( "Modes" );
		final Menu modeMenu = new Menu( s, SWT.DROP_DOWN );
		mode.setMenu( modeMenu );
		
		//Normal
		final MenuItem normalMode = new MenuItem( modeMenu, SWT.PUSH );
		normalMode.setText( "Normal" );
		
		//Night
		final MenuItem nightMode = new MenuItem( modeMenu, SWT.PUSH );
		nightMode.setText( "Night" );
		

		//Public
		final MenuItem publicMode = new MenuItem( modeMenu, SWT.PUSH );
		publicMode.setText( "Public" );
		

		//Driving
		final MenuItem drivingMode = new MenuItem( modeMenu, SWT.PUSH );
		drivingMode.setText( "Driving" );
		
		//Quit
		addMenuItem("Quit", menubar, new Listener()
		{
		    @Override
		    public void handleEvent(Event event)
		    {
		        // Close database connection for example
		    	System.exit(0);
		    }
		}, SWT.ID_QUIT);
		
		//Preferences
		addMenuItem("Preferences...", menubar, new Listener()
		{
		    @Override
		    public void handleEvent(Event event)
		    {
		        System.out.println("Preferences Clicked");
		    }
		}, SWT.ID_PREFERENCES);
		
		//About
		addMenuItem("About Victoria", menubar, new Listener()
		{
		    @Override
		    public void handleEvent(Event event)
		    {
		        System.out.println("About Screen Clicked");
		    }
		}, SWT.ID_ABOUT);
		
		//Add all to Menu
		s.setMenuBar(menubar);

	}

	private static void addMenuItem(String name, Menu parent, Listener listener, int id)
	{
        Menu systemMenu = Display.getDefault().getSystemMenu();

        for (MenuItem systemItem : systemMenu.getItems())
        {
            if (systemItem.getID() == id)
            {
                systemItem.addListener(SWT.Selection, listener);
                return;
            }
        }
	}

	private static void createPanels(final Shell s, Font f) throws IOException
	{
		GridLayout gridLayout = new GridLayout();
		s.setLayout(gridLayout);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		
		//Color and Font
		Device device = Display.getCurrent ();
		Color home_text_color = new Color (device, 54, 54, 54);
		
		//Text Label
		home_text = new Label(s, SWT.CENTER);
		home_text.setText(process_random("HOME"));
		home_text.setForeground(home_text_color);
		home_text.setLayoutData(gridData);
		home_text.setFont(f);
		home_text.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true));
		
		//Query field
		queryField = new Text(s, SWT.BORDER);
		queryField.setLayoutData(gridData);
		queryField.setFocus();
		
		//Query Listener
		queryField.addTraverseListener(new TraverseListener() 
		{
			public void keyTraversed(TraverseEvent e) 
			{
				if (e.detail == SWT.TRAVERSE_RETURN) 
				{
					query = queryField.getText();
					System.out.println("QUERY RECEIVED: " + query);
					queryField.setText("");
				}
			}
		});
	}
	
	private static String process_random(String s) throws IOException
	{
		String file = readFile("resources/Speaking.txt");
		String splitter = "\\[" + s + "\\]";
		String[] fileArray = file.split(splitter);
		String[] fileArray2 = fileArray[1].split("\n");
		
		int r = (new Random()).nextInt(fileArray2.length-1) + 1;
		String result = fileArray2[r];
		
		result = result.replaceAll("    ", "");
		result = result.replaceAll("namename", readFile("Name.vic"));
		result = result.replaceAll("timetime", process_getTimeDesc());
		
		return result;
	}
	
	private static String readFile(String path)
	{
		String file = "ERROR";
		path = "../../Victoria/" + path;
		try {
			file = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	private static String process_getTimeDesc()
	{
		Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        
        int hour = Integer.parseInt(sdf.format(calendar.getTime()));
        
        if(hour < 12)
        	return "morning";
        else if(hour < 18)
        	return "afternoon";
        else
        	return "evening";
	}
	
}