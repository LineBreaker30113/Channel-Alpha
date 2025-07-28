package channelAlpha.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import channelAlpha.adaptor.ImagePane;

public class ImageModel {
	
	public BufferedImage image;
	public WritableRaster raster;
	public Graphics2D brush;
	public ImagePane pane;
	
	public int getWidth() { return raster.getWidth(); }
	public int getHeight() { return raster.getHeight(); }
	
	public void init() {
		raster = image.getRaster();
		brush = image.createGraphics();
		brush.setBackground(Color.white);
		brush.clearRect(0, 0, getWidth(), getHeight());
	}
	
	
	public void drawStroke(int beginH, int beginV, int vectorH, int vectorV, Color color, float brushSize) {
		double timer = Math.max(Math.abs(vectorV), Math.abs(vectorH));
		brush.setColor(color);
		System.out.println(color);
//		int 
		for(double ct = 0; ct < timer; ct++) {
//			System.out.println(ct + ": " + (beginH + vectorH*ct/timer) + "|" + (beginV + vectorV*ct/timer));
			drawSquare((int) (beginH + vectorH*ct/timer), (int) (beginV + vectorV*ct/timer),  brushSize);
		}
//		System.out.println("dS end!");
	}
	public void drawSquare(int hor, int ver, float brushSize) {
		int bsize = (int) (brushSize + 0.5f);
//		System.out.println("ds: " + hor + "|" + ver + "  " + brushSize);
		brush.fillRect(hor - bsize, ver - bsize, (int) (brushSize * 2), (int) (brushSize * 2));
	}
	
	public void free() {
		brush.dispose();
		pane = null;
		image = null;
		raster = null;
	}
	
}
