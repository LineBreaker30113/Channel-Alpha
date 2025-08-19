package abiy.ChannelAlpha.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import abiy.ChannelAlpha.adaptor.ImagePane;

public class ImageModel {
	
	public BufferedImage image; // The actual image data stored as a buffered image
	public WritableRaster raster; // Raster data for pixel-level manipulation
	public Graphics2D brush; // Graphics context for drawing operations
	public ImagePane pane; // Reference back to the image pane that owns this model
	
	public int getWidth() { return raster.getWidth(); } // Get image width from raster
	public int getHeight() { return raster.getHeight(); } // Get image height from raster
	
	public void init() {
		raster = image.getRaster(); // Get raster data from the buffered image
		brush = image.createGraphics(); // Create graphics context for drawing
		brush.setBackground(Color.white); // Set background color to white
		brush.clearRect(0, 0, getWidth(), getHeight()); // Clear the entire image with background color
	}
	
	
	public void drawStroke(Point2D begin, Point2D end, Color color, float brushSize) {
		brush.setColor(color); brush.setStroke(new BasicStroke(brushSize * 2.f)); // Set color and stroke width
		brush.drawLine((int) begin.getX(), (int) begin.getY(), (int) end.getX(), (int) end.getY()); // Draw line between two points
	}
	public void drawSquare(double hor, double ver, float brushSize) {
		double bsize = brushSize - 0.5; // Calculate brush size offset for centering
		brush.fillRect((int) (hor - bsize), (int) (ver - bsize), (int) (brushSize*2), (int) (brushSize*2)); // Fill rectangle centered at point
	}
	
	public void free() {
		brush.dispose(); // Dispose of graphics context to free resources
		pane = null; // Clear reference to image pane
		image = null; // Clear reference to image
		raster = null; // Clear reference to raster
	}
	
}
