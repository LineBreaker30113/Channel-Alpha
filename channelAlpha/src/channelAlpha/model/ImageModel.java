package channelAlpha.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import channelAlpha.adaptor.ImagePane;

public class ImageModel {
	
	public BufferedImage image;
	public WritableRaster rasterOFimage;
	public Graphics2D brush;
	public ImagePane pane;
	
	public int getWidth() { return rasterOFimage.getWidth(); }
	public int getHeight() { return rasterOFimage.getHeight(); }
	
	public void init() {
		rasterOFimage = image.getRaster();
		brush = image.createGraphics();
	}
	
	
	public void drawStroke(int beginH, int beginV, int vectorH, int vectorV, Color color, float brushSize) {
		int timer = Math.abs(Math.max(vectorV, beginV));
		brush.setColor(color);
		for(int ct = 0; ct < timer; ct++) {
			System.out.println(ct + ": " + (beginH + vectorH*ct/timer) + "|" + (beginV + vectorV*ct/timer));
			drawSquare(beginH + vectorH*ct/timer, beginV + vectorV*ct/timer,  brushSize);
		}
	}
	public void drawSquare(int hor, int ver, float brushSize) {
		int bsize = (int) (brushSize + 0.5f);
		brush.fillRect(hor - bsize, ver - bsize, (int) (brushSize * 2), (int) (brushSize * 2));
	}
}
