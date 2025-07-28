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

	
	public void paintImageToPanel(Graphics2D brush) {
		int iw = ip.im.getWidth(), ih = ip.im.getHeight();
		int dhp = ip.getDisplayXbyImageX((int)ip.horrizontalC);
		int dvp = ip.getDisplayYbyImageY((int)ip.verticalC);
		int dw = (int)(iw * ip.zoomHorrizontal), dh = (int)(ih * ip.zoomVertical);
		brush.clearRect(0, 0, getWidth(), getHeight());
		brush.drawImage(ip.im.image, dhp, dvp, dhp+dw, dvp+dh, 0, 0, ip.im.image.getWidth(), ip.im.image.getHeight(), this);
	}
	

	public void resize(int newWidth, int newHeight) {

		BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = newImage.createGraphics();
		g.setBackground(Color.white);
		g.clearRect(0, 0, newWidth, newHeight);

		int x = (newWidth - ip.im.getWidth()) / 2;
		int y = (newHeight - ip.im.getHeight()) / 2;
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;

		g.drawImage(ip.im.image, x, y, null);
		g.dispose();

		ip.im.free();
		ip.im.image = newImage;
		ip.im.init();

		// Zoom ve center değerlerini sıfırlama
//		ip.zoomHorrizontal = 1.0f;
//		ip.zoomVertical = 1.0f;
//		ip.horrizontalC = 0;
//		ip.verticalC = 0;
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
		paintImageToPanel(g);
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
				ip.zoomHorrizontal *= ZOOM_MULTIPLIER;
				ip.zoomVertical *= ZOOM_MULTIPLIER;
			} else {
				ip.zoomHorrizontal /= ZOOM_MULTIPLIER;
				ip.zoomVertical /= ZOOM_MULTIPLIER;
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
