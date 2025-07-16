package channelAlpha.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import channelAlpha.adaptor.Canvas;
import channelAlpha.lang.Point2i;

public class ImageModel {
	public int width = 600, height = 600;
	public Color m1color = Color.black, m3color = Color.white;
	public float zoomHorrizontal = 1.f, zoomVertical = 1.f;
	public double verticalC = 0, horrizontalC = 0;
	public float brush1size = 2, brush3size = 3;
	
	public BufferedImage image;
	public WritableRaster rasterOFimage;
	public Canvas canvas;
	
	public void init() {
		rasterOFimage = image.getRaster();
	}
	
	public int getImageXbyDisplayX(int horrizontalP) {
		return (int) ((horrizontalP + (rasterOFimage.getWidth()/2.f*(zoomHorrizontal-1.f)))
				/ zoomHorrizontal - horrizontalC);
	}
	public int getImageYbyDisplayY(int verticalP) {
		return (int) ((verticalP + (rasterOFimage.getHeight()/2.f*(zoomVertical-1.f)))
				/ zoomVertical - verticalC);
	}
	public int getDisplayXbyImageX(int horrizontalP) {
		return (int) ((horrizontalP * zoomHorrizontal) -
				(rasterOFimage.getWidth()/2.f*(zoomHorrizontal-1.f)));
//		return (int) ((horrizontalP + (rasterOFimage.getWidth()/2.f*(zoomHorrizontal-1.f)))
//				/ zoomHorrizontal);
	}
	public int getDisplayYbyImageY(int verticalP) {
		return (int) ((verticalP * zoomVertical) -
				(rasterOFimage.getWidth()/2.f*(zoomVertical-1.f)));
	}
	
	public void updateCanvas() {
		float moveSpeed = 5.0f; // Hareket hızını artırmak için bu değeri ekledim
		if (canvas.kbt.isDAdown()) { verticalC -= moveSpeed / zoomVertical; }
		if (canvas.kbt.isDAdown()) { verticalC += moveSpeed / zoomVertical; }
		if (canvas.kbt.isDAdown()) { horrizontalC -= moveSpeed / zoomHorrizontal; }
		if (canvas.kbt.isDAdown()) { horrizontalC += moveSpeed / zoomHorrizontal; }
		if (!canvas.mut.mouseInside) { return; }
		if (!(canvas.mut.left || canvas.mut.right)) { return; }
		int mhp = 0, mvp = 0;
		{
			Point2i p = canvas.mut.getRelativePosition();
			mhp = getImageXbyDisplayX(p.x);
			mvp = getImageYbyDisplayY(p.y);
		}
		if (mhp < 0 || mvp < 0 || mhp >= image.getWidth() || mvp >= image.getHeight()) {
			return;
		}
		if (canvas.mut.left && canvas.mut.right) {

		} else if (canvas.mut.left) {
			drawSquare(mhp, mvp, 1);
		} else if (canvas.mut.right) {
			drawSquare(mhp, mvp, 3);
		}
	}
	
	public void drawStroke(int beginH, int beginV, int endH, int endV, int buttonIndex) {
		int vectorH = endH - beginH, vectorV = endV - beginV;
		int timer = Math.abs(Math.max(vectorV, beginV));
		for(int ct = 0; ct != timer; ct++) {
			drawSquare(beginH + vectorH*ct/timer, beginV + vectorV*ct/timer, buttonIndex);
		}
	}
	public void drawSquare(int hor, int ver, int buttonIndex) {
		if((buttonIndex == 1 ? brush1size : brush3size) == -0.1f) { return; }
		int bsize = (int) (buttonIndex == 1 ? brush1size : brush3size);
		int[] data = new int[3];
		if(buttonIndex == 1) {
			data = new int[] { m1color.getRed(), m1color.getGreen(), m1color.getBlue() };
		} else {
			data = new int[] { m3color.getRed(), m3color.getGreen(), m3color.getBlue() };
		}
		if(bsize == 0.f) {
			if(hor < 0 || hor >= image.getWidth()) { return; }
			if(ver < 0 || ver >= image.getHeight()) { return; }
			rasterOFimage.setPixel(hor, ver, data); return;
		}
		for(int hi = -bsize; hi != bsize; hi++) {
			if(hor+hi < 0 || hor+hi >= image.getWidth()) { continue; }
			for(int vi = -bsize; vi != bsize; vi++) {
				if(ver+vi < 0 || ver+vi >= image.getHeight()) { continue; }
				rasterOFimage.setPixel(hor+hi, ver+vi, data);
			}
		}
	}
}
