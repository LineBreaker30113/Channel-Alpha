package abiy.ChannelAlpha.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import abiy.ChannelAlpha.adaptor.ImagePane;

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
	
	
	public void drawStroke(Point2D begin, Point2D end, Color color, float brushSize) {
		brush.setColor(color); brush.setStroke(new BasicStroke(brushSize * 2.f));
		brush.drawLine((int) begin.getX(), (int) begin.getY(), (int) end.getX(), (int) end.getY());
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
