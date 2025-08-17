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
	public long leftLastPressDate = 0, rightLastPressDate = 0, middleLastPress = 0;
	public long leftLastReleaseDate = 0, rightLastReleaseDate = 0, middleLastRelease = 0;
	public boolean left = false, right = false, midle = false, mouseInside = false;
	public boolean leftReaded = false, rightReaded = false, middleReaded = false;
	
	public Component component;

	public MouseUpdateTracker(Component component) {
		this.component = component;
		leftLastPressDate = new Date().getTime(); rightLastPressDate = new Date().getTime();
		middleLastPress = new Date().getTime(); middleLastRelease = new Date().getTime();
		leftLastReleaseDate = new Date().getTime(); rightLastReleaseDate = new Date().getTime();
	}

	public Point leftLastPressPosition = null, leftLastReleasePosition = null;
	public Point rightLastPressPosition = null, rightLastReleasePosition = null;
	public int leftSameFor = 0, rightSameFor = 0;
	public double wheelStatus = 0;
	public Component mouseOriginObject = null;
	public Point position = new Point();
	public void onPress(MouseEvent e) {
		switch(e.getButton()) {
		case MouseEvent.BUTTON2: midle = true; break;
		case MouseEvent.BUTTON1:
			left = true; leftSameFor = 0;
			leftLastPressPosition = new Point(e.getX(), e.getY());
			leftLastPressDate = new Date().getTime();
			break;
		case MouseEvent.BUTTON3:
			right = true; rightSameFor = 0;
			rightLastPressPosition = new Point(e.getX(), e.getY());
			rightLastPressDate = new Date().getTime();
			break;
		}
	}
	public void onRelease(MouseEvent e) {
		switch(e.getButton()) {
		case MouseEvent.BUTTON2: midle = false; middleReaded = false; break;
		case MouseEvent.BUTTON1:
			left = false; leftSameFor = 0; leftReaded = false;
			leftLastReleasePosition = new Point(e.getX(), e.getY());
			leftLastReleaseDate = new Date().getTime();
			break;
		case MouseEvent.BUTTON3:
			right = false; rightSameFor = 0; rightReaded = false;
			rightLastReleasePosition = new Point(e.getX(), e.getY());
			leftLastReleaseDate = new Date().getTime();
			break;
		}
	}
	public void lbUpdate() {
		if(mouseOriginObject == null) { position = getPosition();
		} else { position = getPosition(mouseOriginObject); }
		leftSameFor++; rightSameFor++;
	}
	public Point getPosition() {
		Point p = MouseInfo.getPointerInfo().getLocation();
		return new Point(p.x, p.y);
	}
	public Point getRelativePosition() {
		Point p = component.getLocationOnScreen(); Point r = getPosition();
		r.x -= p.x; r.y -= p.y;
		return r;
	}
	public Point getPosition(Component relative) {
		Point p = relative.getLocationOnScreen(); Point r = getPosition();
		r.x -= p.x; r.y -= p.y;
		return r;
	}
	@Override public void mousePressed(MouseEvent e) { onPress(e); }
	@Override public void mouseReleased(MouseEvent e) { onRelease(e); }
	@Override public void mouseEntered(MouseEvent e) { mouseInside = true; }
	@Override public void mouseExited(MouseEvent e) { mouseInside = false; }
	@Override public void mouseClicked(MouseEvent e) { }
	@Override public void mouseWheelMoved(MouseWheelEvent e) { wheelStatus += e.getPreciseWheelRotation(); }
	

}