package channelAlpha.adaptor;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import channelAlpha.lang.Point2i;
import channelAlpha.model.Constants;
import channelAlpha.model.ImageModel;
import channelAlpha.view.KeyboardTracker;
import channelAlpha.view.MouseUpdateTracker;

public class Canvas extends JPanel implements MouseMotionListener, MouseWheelListener {
	
	
	public MouseUpdateTracker mut;
	public KeyboardTracker kbt;
	public ImageModel im;
	

	public void paintImageToPanel(Graphics2D brush) {
		int iw = im.image.getWidth(), ih = im.image.getHeight();
		int dhp = im.getDisplayXbyImageX((int)im.horrizontalC);
		int dvp = im.getDisplayYbyImageY((int)im.verticalC);
		int dw = (int)(iw * im.zoomHorrizontal), dh = (int)(ih * im.zoomVertical);
		brush.clearRect(0, 0, getWidth(), getHeight());
		brush.drawImage(im.image, dhp, dvp, dhp+dw, dvp+dh, 0, 0, im.image.getWidth(), im.image.getHeight(), this);
	}
	
	

	public Canvas() {
		im = new ImageModel();
		mut = new MouseUpdateTracker(this);
		kbt = new KeyboardTracker();
		im.canvas = this;
		super.addMouseListener(mut);
		super.addMouseMotionListener(this);
		super.addMouseWheelListener(mut);
	}
	
	public void init() {
		im.image = new BufferedImage(600, 600, BufferedImage.TYPE_3BYTE_BGR);
		im.init();
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
			Point2i cp = mut.getRelativePosition();
			Point mcp = e.getPoint();
			int mchp  = im.getImageXbyDisplayX(mcp.x);
			int mcvp  = im.getImageYbyDisplayY(mcp.y);
			int mlhp  = im.getImageXbyDisplayX(mut.leftLastPressPosition.x);
			int mlvp  = im.getImageYbyDisplayY(mut.leftLastPressPosition.y);
			im.drawStroke(mlhp, mlvp, mchp, mcvp, 1);
			mut.leftLastPressPosition = new Point2i(mcp.x, mcp.y);
		} else if(mut.right) {
			Point2i cp = mut.getRelativePosition();
			Point mcp = e.getPoint();
			int mchp  = im.getImageXbyDisplayX(mcp.x);
			int mcvp  = im.getImageYbyDisplayY(mcp.y);
			int mlhp  = im.getImageXbyDisplayX(mut.rightLastPressPosition.x);
			int mlvp  = im.getImageYbyDisplayY(mut.rightLastPressPosition.y);
			im.drawStroke(mlhp, mlvp, mchp, mcvp, 3);
			mut.rightLastPressPosition = new Point2i(mcp.x, mcp.y);
		}
		
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(mut.left && mut.right) {
			
		} else if(mut.left) {
			im.brush1size -= e.getWheelRotation()/2.f;
			im.brush1size = im.brush1size < 0.f ? -0.1f : im.brush1size;
		} else if(mut.right) {
			im.brush3size -= e.getWheelRotation()/2.f;
			im.brush3size = im.brush3size < 0.f ? -0.1f : im.brush3size;
		} else {
			if(e.getWheelRotation() < 0) {
				im.zoomHorrizontal *= Constants.zoomer;
				im.zoomVertical *= Constants.zoomer;
			} else {
				im.zoomHorrizontal /= Constants.zoomer;
				im.zoomVertical /= Constants.zoomer;
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
