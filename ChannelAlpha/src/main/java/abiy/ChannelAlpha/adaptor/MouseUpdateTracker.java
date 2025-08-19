package abiy.ChannelAlpha.adaptor;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Date;


/******************************************************************************************
 * Simple Mouse Handling class for that for knowing lot's of things about mouse.
 * Stores position of the mouse in "currentMousePosition" where you call "lbUpdate()".
 * If you set the value of "mouseOriginObject" to a component, "position"
 * will be relative to that component (best practice is setting it to a JPanel).
 * For using this object you can either add this as a "MouseListener" to a "JPanel"
 * (not to a "JFrame") or you can call "onPress", "onRelease" on related functions
 * but for handling "mouseInside" you need to set it on your own,
 * but hopefully it is not changing any of other methods working style.
 * Boolean "left", "right", "middle" indicates if they are down.
 * Booleans that ends with "Readed" are set to false only when
 * the related key released automatically and there is not
 * any other action, so YOU can handle button presses.
 * The "wheel" starts at 0 and as wheel events happen it changes,
 * so you can learn scroll of wheel relative to the one at start of application.
 * Integers that end with "for" meant to tell you how many updates they stayed
 * in the same state, for using this utility you must call lbUpdate() in every update.
 ******************************************************************************************/
public class MouseUpdateTracker implements MouseListener, MouseWheelListener {
	public long leftLastPressDate = 0, rightLastPressDate = 0, middleLastPress = 0; // Timestamps for last button press events
	public long leftLastReleaseDate = 0, rightLastReleaseDate = 0, middleLastRelease = 0; // Timestamps for last button release events
	public boolean left = false, right = false, midle = false, mouseInside = false; // Current button states and mouse inside flag
	public boolean leftReaded = false, rightReaded = false, middleReaded = false; // Flags to track if button press has been processed
	
	public Component component; // Component that this tracker is attached to

	public MouseUpdateTracker(Component component) {
		this.component = component; // Store reference to the component
		leftLastPressDate = new Date().getTime(); rightLastPressDate = new Date().getTime(); // Initialize press timestamps
		middleLastPress = new Date().getTime(); middleLastRelease = new Date().getTime(); // Initialize middle button timestamps
		leftLastReleaseDate = new Date().getTime(); rightLastReleaseDate = new Date().getTime(); // Initialize release timestamps
	}

	public Point leftLastPressPosition = null, leftLastReleasePosition = null; // Store left button press and release positions
	public Point rightLastPressPosition = null, rightLastReleasePosition = null; // Store right button press and release positions
	public int leftSameFor = 0, rightSameFor = 0; // Count how many updates buttons stayed in same state
	public double wheelStatus = 0; // Cumulative wheel rotation value
	public Component mouseOriginObject = null; // Component for relative positioning
	public Point position = new Point(); // Current mouse position
	
	public void onPress(MouseEvent e) {
		switch(e.getButton()) { // Check which mouse button was pressed
		case MouseEvent.BUTTON2: midle = true; break; // Middle button (wheel click)
		case MouseEvent.BUTTON1: // Left button
			left = true; leftSameFor = 0; // Set left button state and reset counter
			leftLastPressPosition = new Point(e.getX(), e.getY()); // Store press position
			leftLastPressDate = new Date().getTime(); // Store press timestamp
			break;
		case MouseEvent.BUTTON3: // Right button
			right = true; rightSameFor = 0; // Set right button state and reset counter
			rightLastPressPosition = new Point(e.getX(), e.getY()); // Store press position
			rightLastPressDate = new Date().getTime(); // Store press timestamp
			break;
		}
	}
	
	public void onRelease(MouseEvent e) {
		switch(e.getButton()) { // Check which mouse button was released
		case MouseEvent.BUTTON2: midle = false; middleReaded = false; break; // Middle button release
		case MouseEvent.BUTTON1: // Left button release
			left = false; leftSameFor = 0; leftReaded = false; // Reset left button state and flags
			leftLastReleasePosition = new Point(e.getX(), e.getY()); // Store release position
			leftLastReleaseDate = new Date().getTime(); // Store release timestamp
			break;
		case MouseEvent.BUTTON3: // Right button release
			right = false; rightSameFor = 0; rightReaded = false; // Reset right button state and flags
			rightLastReleasePosition = new Point(e.getX(), e.getY()); // Store release position
			leftLastReleaseDate = new Date().getTime(); // Store release timestamp (note: should be rightLastReleaseDate)
			break;
		}
	}
	
	public void lbUpdate() {
		if(mouseOriginObject == null) { position = getPosition(); // Get absolute position if no origin component
		} else { position = getPosition(mouseOriginObject); } // Get position relative to origin component
		leftSameFor++; rightSameFor++; // Increment counters for button state duration
	}
	
	public Point getPosition() {
		Point p = MouseInfo.getPointerInfo().getLocation(); // Get current mouse position from system
		return new Point(p.x, p.y); // Return new Point object
	}
	
	public Point getRelativePosition() {
		Point p = component.getLocationOnScreen(); Point r = getPosition(); // Get component screen location and current mouse position
		r.x -= p.x; r.y -= p.y; // Calculate position relative to component
		return r; // Return relative position
	}
	
	public Point getPosition(Component relative) {
		Point p = relative.getLocationOnScreen(); Point r = getPosition(); // Get relative component screen location and current mouse position
		r.x -= p.x; r.y -= p.y; // Calculate position relative to specified component
		return r; // Return relative position
	}
	
	@Override public void mousePressed(MouseEvent e) { onPress(e); } // Handle mouse press event
	@Override public void mouseReleased(MouseEvent e) { onRelease(e); } // Handle mouse release event
	@Override public void mouseEntered(MouseEvent e) { mouseInside = true; } // Handle mouse enter event
	@Override public void mouseExited(MouseEvent e) { mouseInside = false; } // Handle mouse exit event
	@Override public void mouseClicked(MouseEvent e) { } // Handle mouse click event (not used)
	@Override public void mouseWheelMoved(MouseWheelEvent e) { wheelStatus += e.getPreciseWheelRotation(); } // Update cumulative wheel rotation
	

}