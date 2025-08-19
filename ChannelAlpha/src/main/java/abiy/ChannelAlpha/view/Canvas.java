package abiy.ChannelAlpha.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import abiy.ChannelAlpha.adaptor.ImagePane;
import abiy.ChannelAlpha.adaptor.KeyboardTracker;
import abiy.ChannelAlpha.adaptor.MouseUpdateTracker;

public class Canvas extends JPanel implements MouseMotionListener, MouseWheelListener {
	
	
	
	public final float ZOOM_MULTIPLIER = 17.f/16.f; // Zoom multiplier constant for smooth zooming
	
	
	public MouseUpdateTracker mut; // Mouse update tracker for handling mouse events
	public KeyboardTracker kbt; // Keyboard tracker for handling keyboard input
	public ImagePane ip; // Image pane that manages the image display
	
	private Point leftLastPressPosition = null, rightLastPressPosition = null; // Store last mouse press positions for drawing strokes

	public void updateCanvas() {
		ip.updatePane(); // Update the image pane (handle keyboard movement)
		if (!(mut.left || mut.right)) { // If neither mouse button is pressed
			leftLastPressPosition = null; // Reset left button position
			rightLastPressPosition = null; // Reset right button position
		}
		Point mouseCurrentLocation = mut.getRelativePosition(); // Get current mouse position
		Point2D.Double mp = ip.mapDisplay2Image(mouseCurrentLocation); // Convert display coordinates to image coordinates
		if (!ip.isWithin(mp)) { // Check if mouse is within image bounds
			return; // Exit if mouse is outside image
		}
		if (mut.left && mut.right) { // If both mouse buttons are pressed
			// Both buttons pressed - could implement special behavior here
		} else if (mut.left) { // If only left mouse button is pressed
			if(leftLastPressPosition != null) { // If we have a previous left button position
				Point2D lastP = ip.mapDisplay2Image(leftLastPressPosition); // Convert last position to image coordinates
				ip.drawStroke(lastP, mp, 1); // Draw stroke from last position to current position
			}
			leftLastPressPosition = mouseCurrentLocation; // Update left button position
			rightLastPressPosition = null; // Reset right button position
			ip.drawSquare(mp, 1); // Draw square at current position
		} else if (mut.right) { // If only right mouse button is pressed
			System.out.println("releasing left"); // Debug print (should be "right button")
			if(rightLastPressPosition != null) { // If we have a previous right button position
				Point2D lastP = ip.mapDisplay2Image(rightLastPressPosition); // Convert last position to image coordinates
				ip.drawStroke(lastP, mp, 3); // Draw stroke from last position to current position
			}
			rightLastPressPosition = mouseCurrentLocation; // Update right button position
			leftLastPressPosition = null; // Reset left button position
			ip.drawSquare(mp, 3); // Draw square at current position
		}
	}
	

	public Canvas() {
		ip = new ImagePane(this); // Create new image pane
		mut = new MouseUpdateTracker(this); // Create new mouse update tracker
		kbt = new KeyboardTracker(); // Create new keyboard tracker
		super.addMouseListener(mut); // Add mouse listener for mouse events
		super.addMouseMotionListener(this); // Add mouse motion listener for drag events
		super.addMouseWheelListener(mut); // Add mouse wheel listener for zoom
		super.addMouseWheelListener(this); // Add mouse wheel listener for brush size changes
	}
	
	
	public void init() {
		super.setSize(600, 600); // Set canvas size to 600x600 pixels
		ip.init(); // Initialize the image pane
	}
	
	/**
	 * Gets the ImagePane associated with this canvas
	 */
	public ImagePane getImagePane() {
		return ip; // Return the image pane
	}
	
	/**
	 * Gets the KeyboardTracker associated with this canvas
	 */
	public KeyboardTracker getKeyboardTracker() {
		return kbt; // Return the keyboard tracker
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics); // Call parent paint method
		Graphics2D g = (Graphics2D) graphics; // Cast to Graphics2D for advanced drawing
		g.setBackground(Color.LIGHT_GRAY); // Set background color
		g.clearRect(0, 0, getWidth(), getHeight()); // Clear the entire canvas
		ip.paintImageToPanel(g); // Paint the image onto the canvas
	}



	@Override
	public void mouseDragged(MouseEvent e) {
		if(mut.left && mut.right) { // If both mouse buttons are pressed
			// Both buttons pressed - could implement special behavior here
		} else if(mut.left) { // If only left mouse button is pressed
			Point mcp = e.getPoint(); // Get current mouse point
			mut.leftLastPressPosition = new Point(mcp.x, mcp.y); // Update left button position in mouse tracker
		} else if(mut.right) { // If only right mouse button is pressed
			Point mcp = e.getPoint(); // Get current mouse point
			mut.rightLastPressPosition = new Point(mcp.x, mcp.y); // Update right button position in mouse tracker
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(mut.left && mut.right) { // If both mouse buttons are pressed
			// Both buttons pressed - could implement special behavior here
		} else if(mut.left) { // If only left mouse button is pressed
			ip.brush1size -= e.getWheelRotation()/2.f; // Adjust left brush size based on wheel rotation
			ip.brush1size = ip.brush1size < 0.f ? 0.1f : ip.brush1size; // Ensure minimum brush size
		} else if(mut.right) { // If only right mouse button is pressed
			ip.brush3size -= e.getWheelRotation()/2.f; // Adjust right brush size based on wheel rotation
			ip.brush3size = ip.brush3size < 0.f ? 0.1f : ip.brush3size; // Ensure minimum brush size
		} else { // If no mouse buttons are pressed
			ip.scale(e.getWheelRotation() < 0, e.getPoint()); // Zoom in/out based on wheel rotation
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Update mouse position for tracking purposes
		// This method is required by the MouseMotionListener interface
	}

}
