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
		for(double ct = 0; ct < timer; ct++) {
			drawSquare((int) (beginH + vectorH*ct/timer), (int) (beginV + vectorV*ct/timer),  brushSize);
		}
	}
	public void drawSquare(double hor, double ver, float brushSize) {
		double bsize = brushSize - 0.5;
		brush.fillRect((int) (hor - bsize), (int) (ver - bsize), (int) (brushSize*2), (int) (brushSize*2));
	}
	
	public void free() {
		brush.dispose();
		pane = null;
		image = null;
		raster = null;
	}
	
}
