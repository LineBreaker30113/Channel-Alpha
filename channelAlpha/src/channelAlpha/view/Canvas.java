package channelAlpha.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import channelAlpha.adaptor.ImagePane;
import channelAlpha.adaptor.KeyboardTracker;
import channelAlpha.adaptor.MouseUpdateTracker;
import channelAlpha.lang.Point2i;

public class Canvas extends JPanel implements MouseMotionListener, MouseWheelListener {
	
	
	
	public final float ZOOM_MULTIPLIER = 17.f/16.f;
	
	
	public MouseUpdateTracker mut;
	public KeyboardTracker kbt;
	public ImagePane ip;
	
	

	public void updateCanvas() {
		ip.updatePane();
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
		ip.im.image = new BufferedImage(600, 600, BufferedImage.TYPE_3BYTE_BGR);
		ip.init();
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		ip.paintSpace(g, 0, 0);
		ip.paintImageToPanel(g);
//		Point2i displayLocation = mut.getRelativePosition();
//		g.setColor(Color.RED);
//		g.fillRect(0, displayLocation.y, getWidth(), 1);
//		g.fillRect(displayLocation.x, 0, 1, getHeight());
//		g.setColor(Color.GREEN);
//		g.fillRect(0, (int) ip.getViewYbyLimitY(ip.getLimitYbyViewY(displayLocation.y)), getWidth(), 1);
//		g.fillRect((int) ip.getViewXbyLimitX(ip.getLimitXbyViewX(displayLocation.x)), 0, 1, getHeight());
	}



	@Override
	public void mouseDragged(MouseEvent e) {
		if(mut.left && mut.right) {
			
		} else if(mut.left) {
			Point mcp = e.getPoint();
			ip.drawStroke(mut.leftLastPressPosition.x,
					mut.leftLastPressPosition.y,
					mcp.x, mcp.y, 1);
			mut.leftLastPressPosition = new Point2i(mcp.x, mcp.y);
		} else if(mut.right) {
			Point mcp = e.getPoint();
			ip.drawStroke(mut.rightLastPressPosition.x,
					mut.rightLastPressPosition.y,
					mcp.x, mcp.y, 3);
			mut.rightLastPressPosition = new Point2i(mcp.x, mcp.y);
		}
		
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(mut.left && mut.right) {
			
		} else if(mut.left) {
			ip.brush1size -= e.getWheelRotation()/2.f;
			ip.brush1size = ip.brush1size < 0.f ? -0.1f : ip.brush1size;
		} else if(mut.right) {
			ip.brush3size -= e.getWheelRotation()/2.f;
			ip.brush3size = ip.brush3size < 0.f ? -0.1f : ip.brush3size;
		} else {
			if(e.getWheelRotation() < 0) {
				ip.zoomXat(true, e.getPoint().getX());
				ip.zoomYat(true, e.getPoint().getY());
			} else {
				ip.zoomXat(false, e.getPoint().getX());
				ip.zoomYat(false, e.getPoint().getY());
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
