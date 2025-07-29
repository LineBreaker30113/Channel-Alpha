package channelAlpha.adaptor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.ReentrantLock;

import channelAlpha.Util;
import channelAlpha.lang.Point2i;
import channelAlpha.model.ImageModel;
import channelAlpha.view.Canvas;

public class ImagePane {
	
	
	public ImageModel im;
	public Canvas canvas;
	public ReentrantLock brushLock;

	
	public double zoomX = 1.f, zoomY = 1.f;
	public double imageX = 0., imageY = 0., spaceX = 0., spaceY = 0.;

	public float brush1size = 2, brush3size = 3;
	public Color m1color = Color.black, m3color = Color.white;
	

	public void paintImageToPanel(Graphics2D brush) {
		int iw = im.getWidth(), ih = im.getHeight();
		int dhp = (int) getDisplayXbyImageX((int)0);
		int dvp = (int) getDisplayYbyImageY((int)0);
		int dw = (int)(iw * zoomX), dh = (int)(ih * zoomY);
		brush.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		brush.drawImage(im.image, dhp, dvp, dhp+dw, dvp+dh, 0, 0, im.getWidth(), im.getHeight(), null);
	}
	
	/*
	 * 
		public static interface LBgameWorld2f {
			/** Height is the display's height* public int lbWidth();
			/** Width is the display's width* public int lbHeight();
			/** Zoom is the ratio displayPixels per WorldPoints. * public float lbXzoomRatio();
			/** Zoom is the ratio displayPixels per WorldPoints. * public float lbYzoomRatio();
			/** Just the center of the display * public LBpoint2f lbCenter();
			/** Map point in display to point in world. *
			public default LBpoint2f mapD2W(LBpoint2f p) {
				LBpoint2f result = new LBpoint2f(p.x, lbHeight() - p.y);
				result.increase(lbWidth()*-0.5f, lbHeight()*-0.5f);
				result.amplifie(1.f/lbXzoomRatio(), 1.f/lbYzoomRatio());
				result.increase(lbCenter());
				return result;
			}
			/** Map point in world to point in display. *
			public default LBpoint2f mapW2D(LBpoint2f p) {
				LBpoint2f result = lbCenter().minus().increase(p);
				result.amplifie(lbXzoomRatio(), lbYzoomRatio());
				result.increase(lbWidth()*+0.5f, lbHeight()*+0.5f);
				result.y = lbHeight() - result.y;
				return result;
			}
		}
		
		w, h, cx, cy, zx, zy
		dx, dy, mx, my
		
		mx = dx-w/2)/zx)+cx
		dx = mx-cx)*zx)+w/2
		
		
	 * */

	
//	public double getImageXbyDisplayX(double horrizontalP) {
//		return (double) ((horrizontalP + (canvas.getWidth()/2.*(zoomHorrizontal-1.)))
//				/ zoomHorrizontal - horrizontalCenter);
//	}
//	public double getDisplayXbyImageX(double horrizontalP) {
//		return (double) ((horrizontalP * zoomHorrizontal) -
//				(canvas.getWidth()/2.f*(zoomHorrizontal-1.)));
//	}
	
	public double getSpaceXbyDisplayX(double horrizontalP) {
		return (double) ((horrizontalP - canvas.getWidth()/2.) / zoomX + spaceX);
	}
	public double getDisplayXbySpaceX(double horrizontalP) {
		return (double) ((horrizontalP - spaceX) * zoomX + canvas.getWidth()/2.);
	}

	public double getSpaceYbyDisplayY(double horrizontalP) {
		return (double) ((-horrizontalP + canvas.getHeight()/2.) / zoomX + spaceY);
	}
	public double getDisplayYbySpaceY(double horrizontalP) {
		return (double) (-((horrizontalP - spaceY) * zoomX - canvas.getHeight()/2.));
	}
	
	public double getImageXbyDisplayX(double horrizontalP) {
		return (double) (((horrizontalP - canvas.getWidth()/2.) / zoomX - imageY) + im.getWidth()/2.);
	}
	public double getDisplayXbyImageX(double horrizontalP) {
		return (double) ((horrizontalP - im.getWidth()/2.) * zoomX + canvas.getWidth()/2.f);
	}

	public double getImageYbyDisplayY(double verticalP) {
		return (double) (((verticalP - canvas.getHeight()/2.) / zoomY + im.getHeight()/2. - imageX));
	}
	public double getDisplayYbyImageY(double verticalP) {
		return (double) ((verticalP - im.getHeight()/2.) * zoomY + canvas.getHeight()/2.f);
	}

	public void updatePane() {
		float moveSpeed = 5.0f;
		if (canvas.kbt.isDAdown()) { imageX -= moveSpeed / zoomY; }
		if (canvas.kbt.isUAdown()) { imageX += moveSpeed / zoomY; }
		if (canvas.kbt.isRAdown()) { imageY -= moveSpeed / zoomX; }
		if (canvas.kbt.isLAdown()) { imageY += moveSpeed / zoomX; }
		if (!canvas.mut.mouseInside) { return; }
		if (!(canvas.mut.left || canvas.mut.right)) { return; }
		double mhp = 0, mvp = 0;
		{
			Point2i p = canvas.mut.getRelativePosition();
			mhp = getImageXbyDisplayX(p.x);
			mvp = getImageYbyDisplayY(p.y);
		}
		if (mhp < 0 || mvp < 0 || mhp >= im.getWidth() || mvp >= im.getHeight()) {
			return;
		}
		if (canvas.mut.left && canvas.mut.right) {

		} else if (canvas.mut.left) {
			drawSquare(mhp, mvp, m1color, brush1size);
		} else if (canvas.mut.right) {
			drawSquare(mhp, mvp, m3color, brush3size);
		}
	}
	
	public void drawSquare(double hor, double ver, Color color, float brushSize) {
		if((brushSize) == -0.1f) { return; }
		brushLock.lock();
		im.brush.setColor(color);
		im.drawSquare(hor, ver, brushSize);
		brushLock.unlock();
	}

	public void drawStroke(int beginH, int beginV, int endH, int endV, int buttonIndex) {
		if((buttonIndex == 1 ? brush1size : brush3size) == -0.1f) { return; }
		brushLock.lock();
		double beginPH = getImageXbyDisplayX(beginH), beginPV = getImageYbyDisplayY(beginV),
				endPH = getImageXbyDisplayX(endH), endPV = getImageYbyDisplayY(endV);
		int vectorH = (int) (endPH - beginPH), vectorV = (int) (endPV - beginPV);
		im.drawStroke((int) beginPH, (int) beginPV, vectorH, vectorV,
				buttonIndex == 1 ? m1color : m3color, buttonIndex == 1 ? brush1size : brush3size);
		brushLock.unlock();
	}
	
	

	public ImagePane(Canvas canvas) {
		this.canvas = canvas;
		im = new ImageModel();
		im.pane = this;
		brushLock = new ReentrantLock();
	}
	
	public void init() {
		im.image = new BufferedImage(600, 600, BufferedImage.TYPE_3BYTE_BGR);
		im.init();
	}
	
	public void init(int width, int height) {
		im.image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		im.init();
	}


}
