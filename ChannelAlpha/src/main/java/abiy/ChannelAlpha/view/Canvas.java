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
	
	
	
	public final float ZOOM_MULTIPLIER = 17.f/16.f;
	
	
	public MouseUpdateTracker mut;
	public KeyboardTracker kbt;
	public ImagePane ip;
	
	private Point leftLastPressPosition = null, rightLastPressPosition = null;

	public void updateCanvas() {
		ip.updatePane();
		if (!(mut.left || mut.right)) {
			leftLastPressPosition = null;
			rightLastPressPosition = null;
		}
		Point mouseCurrentLocation = mut.getRelativePosition();
		Point2D.Double mp = ip.mapDisplay2Image(mouseCurrentLocation);
		if (!ip.isWithin(mp)) {
			return;
		}
		if (mut.left && mut.right) {

		} else if (mut.left) {
			if(leftLastPressPosition != null) {
				Point2D lastP = ip.mapDisplay2Image(leftLastPressPosition);
				ip.drawStroke(lastP, mp, 1);
			}
			leftLastPressPosition = mouseCurrentLocation;
			rightLastPressPosition = null;
			ip.drawSquare(mp, 1);
		} else if (mut.right) {
			System.out.println("releasing left");
			if(rightLastPressPosition != null) {
				Point2D lastP = ip.mapDisplay2Image(rightLastPressPosition);
				ip.drawStroke(lastP, mp, 3);
			}
			rightLastPressPosition = mouseCurrentLocation;
			leftLastPressPosition = null;
			ip.drawSquare(mp, 3);
		}
	}
	

	public Canvas() {
		ip = new ImagePane(this);
		mut = new MouseUpdateTracker(this);
		kbt = new KeyboardTracker();
		super.addMouseListener(mut);
		super.addMouseMotionListener(this);
		super.addMouseWheelListener(mut);
		super.addMouseWheelListener(this);
	}
	
	
	public void init() {
		super.setSize(600, 600);
		ip.init();
	}
	
	/**
	 * Gets the ImagePane associated with this canvas
	 */
	public ImagePane getImagePane() {
		return ip;
	}
	
	/**
	 * Gets the KeyboardTracker associated with this canvas
	 */
	public KeyboardTracker getKeyboardTracker() {
		return kbt;
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setBackground(Color.LIGHT_GRAY);
		g.clearRect(0, 0, getWidth(), getHeight());
		ip.paintImageToPanel(g);
	}



	@Override
	public void mouseDragged(MouseEvent e) {
		if(mut.left && mut.right) {
			// Both buttons pressed - could implement special behavior here
		} else if(mut.left) {
			Point mcp = e.getPoint();
			mut.leftLastPressPosition = new Point(mcp.x, mcp.y);
		} else if(mut.right) {
			Point mcp = e.getPoint();
			mut.rightLastPressPosition = new Point(mcp.x, mcp.y);
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(mut.left && mut.right) {
			// Both buttons pressed - could implement special behavior here
		} else if(mut.left) {
			ip.brush1size -= e.getWheelRotation()/2.f;
			ip.brush1size = ip.brush1size < 0.f ? 0.1f : ip.brush1size;
		} else if(mut.right) {
			ip.brush3size -= e.getWheelRotation()/2.f;
			ip.brush3size = ip.brush3size < 0.f ? 0.1f : ip.brush3size;
		} else {
			ip.scale(e.getWheelRotation() < 0, e.getPoint());
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Update mouse position for tracking purposes
		// This method is required by the MouseMotionListener interface
	}

}
