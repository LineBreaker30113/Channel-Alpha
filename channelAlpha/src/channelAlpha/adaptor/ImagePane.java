package channelAlpha.adaptor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.ReentrantLock;

import channelAlpha.lang.Point2i;
import channelAlpha.model.ImageModel;
import channelAlpha.view.Canvas;

public class ImagePane {
	
	
	public ImageModel im;
	public Canvas canvas;
	public ReentrantLock brushLock;

	
	public float zoomHorrizontal = 1.f, zoomVertical = 1.f;
	public double verticalC = 0, horrizontalC = 0;

	public float brush1size = 2, brush3size = 3;
	public Color m1color = Color.black, m3color = Color.white;
	

	public void paintImageToPanel(Graphics2D brush) {
		int iw = im.getWidth(), ih = im.getHeight();
		int dhp = getDisplayXbyImageX((int)0);
		int dvp = getDisplayYbyImageY((int)0);
		int dw = (int)(iw * zoomHorrizontal * zoomHorrizontal), dh = (int)(ih * zoomVertical);
		brush.clearRect(0, 0, im.getWidth(), im.getHeight());
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

	
	public int getImageXbyDisplayX(int horrizontalP) {
		return (int) ((horrizontalP + (im.getWidth()/2.f*(zoomHorrizontal-1.f)))
				/ zoomHorrizontal - horrizontalC);
	}
	public int getImageYbyDisplayY(int verticalP) {
		return (int) ((verticalP + (im.getHeight()/2.f*(zoomVertical-1.f)))
				/ zoomVertical - verticalC);
	}
	public int getDisplayXbyImageX(int horrizontalP) {
		return (int) ((horrizontalP * zoomHorrizontal) -
				(im.rasterOFimage.getWidth()/2.f*(zoomHorrizontal-1.f)));
	}
	public int getDisplayYbyImageY(int verticalP) {
		return (int) ((verticalP * zoomVertical) -
				(im.rasterOFimage.getHeight()/2.f*(zoomVertical-1.f)));
	}

	public void updatePane() {
		float moveSpeed = 5.0f; // Hareket hızını artırmak için bu değeri ekledim
		if (canvas.kbt.isDAdown()) { verticalC -= moveSpeed / zoomVertical; }
		if (canvas.kbt.isUAdown()) { verticalC += moveSpeed / zoomVertical; }
		if (canvas.kbt.isRAdown()) { horrizontalC -= moveSpeed / zoomHorrizontal; }
		if (canvas.kbt.isLAdown()) { horrizontalC += moveSpeed / zoomHorrizontal; }
		if (!canvas.mut.mouseInside) { return; }
		if (!(canvas.mut.left || canvas.mut.right)) { return; }
		int mhp = 0, mvp = 0;
		{
			Point2i p = canvas.mut.getRelativePosition();
//			mhp = getImageXbyDisplayX(p.x);
//			mvp = getImageYbyDisplayY(p.y);
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
	
	public void drawSquare(int hor, int ver, Color color, float brushSize) {
		if((brushSize) == -0.1f) { return; }
		brushLock.lock();
		int[] data = new int[3];
		data = new int[] { m1color.getRed(), m1color.getGreen(), m1color.getBlue() };
		im.drawSquare(getImageXbyDisplayX(hor), getImageYbyDisplayY(ver), brushSize);
		brushLock.unlock();
	}

	public void drawStroke(int beginH, int beginV, int endH, int endV, int buttonIndex) {
		if((buttonIndex == 1 ? brush1size : brush3size) == -0.1f) { return; }
		brushLock.lock();
		int beginPH = getImageXbyDisplayX(beginH), beginPV = getImageYbyDisplayY(beginV),
				endPH = getImageXbyDisplayX(endH), endPV = getImageYbyDisplayY(endV);
		int vectorH = endPH - beginPH, vectorV = endPV - beginPV;
		im.drawStroke(beginPH, beginPV, vectorH, vectorV,
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


}
