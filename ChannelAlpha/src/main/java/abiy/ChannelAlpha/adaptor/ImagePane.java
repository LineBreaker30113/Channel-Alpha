package abiy.ChannelAlpha.adaptor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.concurrent.locks.ReentrantLock;

import abiy.ChannelAlpha.model.ImageModel;
import abiy.ChannelAlpha.view.Canvas;

public class ImagePane { //  extends channelAlpha.lang.LimitedPane2D
	
	/** Constants for zooming effect, the optimal solutions for least artifacts. */
	public final static double ZOOM_MULTIPLIER = Math.E / 2., ZOOM_DIVIDER = 1./ZOOM_MULTIPLIER;
	/** Constants for zooming effect, the optimal solutions for least artifacts. */
	public final static byte ZOOM_MAX_INDEX = 13, ZOOM_MIN_INDEX = -3;
	
	/** Zoom ratios for x and y, ironically not used scaling AffineTransform but rater in debugging and moving. */
	public double zoomXratio = 1., zoomYratio = 1.;
	/** The power of the "ZOOM_MULTIPLIER" thats equal to current zoom level. */
	public byte zoomXindex = 0, zoomYindex = 0, zoomMax = 0;
	/** A single Image for data, later on we may make this hold more images. */
	public ImageModel im;
	/** The GUI element associated with. Later we may make this canvas have more than 1 ImagePanes. */
	public Canvas canvas;
	/** The Lock for the brush of the "im". All of the locking and unlocking should be done in this class for ease of development. */
	public ReentrantLock brushLock;


	/** The brush sizes for left and right clicks. */
	public float brush1size = 2.f, brush3size = 3.f;
	/** The brush colors for left and right clicks. */
	public Color m1color = Color.black, m3color = Color.white;
	
	/**
	 * Gets the primary color (left mouse button)
	 */
	public Color getPrimaryColor() {
		return m1color;
	}
	
	/**
	 * Sets the primary color (left mouse button)
	 */
	public void setPrimaryColor(Color color) {
		this.m1color = color;
	}
	
	/**
	 * Gets the secondary color (right mouse button)
	 */
	public Color getSecondaryColor() {
		return m3color;
	}
	
	/**
	 * Sets the secondary color (right mouse button)
	 */
	public void setSecondaryColor(Color color) {
		this.m3color = color;
	}
	
	/**
	 * Gets the current image
	 */
	public BufferedImage getImage() {
		return im.image;
	}
	
	/**
	 * Gets the current image dimensions
	 */
	public java.awt.Dimension getImageSize() {
		if (im.image != null) {
			return new java.awt.Dimension(im.image.getWidth(), im.image.getHeight());
		}
		return new java.awt.Dimension(0, 0);
	}
	
	/**
	 * Loads an image from a file
	 */
	public void loadImage(java.io.File file) throws IOException {
		im.image = ImageIO.read(file);
		if (im.image != null) {
			im.raster = im.image.getRaster();
		} else {
			throw new IOException("Failed to read image file");
		}
	}

	/** Since ImageModel should bee separate from "view" we had to add this function, later we may expand it's functionality. */
	public void paintImageToPanel(Graphics2D brush) {
//		AffineTransform bTransform = brush.getTransform();
//		AffineTransform rtransform = new AffineTransform(bTransform);
//		rtransform.concatenate(transform);
//		brush.setTransform(rtransform);
//		brush.drawImage(im.image, 0, 0, canvas);
//		brush.setTransform(bTransform);
//		Object renderingAAkey = brush.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
//		brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		brush.drawImage(im.image, transform, canvas);
//		brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING, renderingAAkey);
	}
	
	public void scale(boolean in, Point location) {
		boolean scalingX = true, scalingY = true;
		if(in) {
			scalingX = zoomXindex != zoomMax;
			zoomXindex += scalingX ? 1 : 0;
			scalingY = zoomYindex != zoomMax;
			zoomYindex += scalingY ? 1 : 0;
		} else {
			scalingX = zoomXindex != ZOOM_MIN_INDEX;
			zoomXindex -= scalingX ? 1 : 0;
			scalingY = zoomYindex != ZOOM_MIN_INDEX;
			zoomYindex -= scalingY ? 1 : 0;
		}
		double scaler = in ? ZOOM_MULTIPLIER : ZOOM_DIVIDER;
		Point2D originalPointInImageSpace = new Point2D.Double();
		try {
			transform.inverseTransform(location, originalPointInImageSpace);
		} catch (NoninvertibleTransformException ex) {
			ex.printStackTrace(); // Should not happen in a correctly initialized transform
			return;
		}
		transform.scale(scalingX ? scaler : 1., scalingY ? scaler : 1.);
		Point2D newPointInImageSpace = new Point2D.Double();
		try {
			transform.inverseTransform(location, newPointInImageSpace);
		} catch (NoninvertibleTransformException ex) {
			ex.printStackTrace(); // Should not happen in a correctly initialized transform
			return;
		}
		transform.translate(
				newPointInImageSpace.getX() -
				originalPointInImageSpace.getX(),
				newPointInImageSpace.getY() -
				originalPointInImageSpace.getY());
		zoomXratio *= scalingX ? scaler : 1.;
		zoomYratio *= scalingY ? scaler : 1.;
	}
	
	private AffineTransform transform;

	public void updatePane() {
		float moveSpeed = 5.0f;
		if (canvas.kbt.isDAdown()) { transform.translate(0., -moveSpeed / zoomYratio); }
		if (canvas.kbt.isUAdown()) { transform.translate(0., +moveSpeed / zoomYratio); }
		if (canvas.kbt.isRAdown()) { transform.translate(-moveSpeed / zoomXratio, 0.); }
		if (canvas.kbt.isLAdown()) { transform.translate(+moveSpeed / zoomXratio, 0.); }
		if (!canvas.mut.mouseInside) { return; }
	}
	
	public Point2D.Double mapDisplay2Image(Point displayLocation) {
		Point2D.Double result = new Point2D.Double();
		try {
			transform.inverseTransform(displayLocation, result);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean isWithin(Point2D.Double point) {
		return point.x >= 0 && point.y >= 0 && point.x < im.getWidth() && point.y < im.getHeight();
	}
	
	public void drawSquare(Point2D point, int buttonIndex) {
		float brushSize = buttonIndex == 1 ? brush1size : brush3size;
		Color color = buttonIndex == 1 ? m1color : m3color;
		if(brushSize <= 0.1f) { return; }
		
		brushLock.lock();
		try {
			im.brush.setColor(color);
			im.drawSquare(point.getX(), point.getY(), brushSize);
		} finally {
			brushLock.unlock();
		}
	}

	public void drawStroke(Point2D begin, Point2D end, int buttonIndex) {
		float brushSize = buttonIndex == 1 ? brush1size : brush3size;
		if(brushSize <= 0.1f) { return; }
		
		brushLock.lock();
		try {
			im.drawStroke(begin, end, buttonIndex == 1 ? m1color : m3color, brushSize);
		} finally {
			brushLock.unlock();
		}
	}

	public void resize(int newWidth, int newHeight) {
		if (newWidth <= 0 || newHeight <= 0) {
			throw new IllegalArgumentException("Width and height must be positive");
		}
		
		BufferedImage formerImage = im.image;
		im.free();
		init(newWidth, newHeight);

		if (formerImage != null) {
			int x = Math.max(0, (newWidth - formerImage.getWidth()) / 2);
			int y = Math.max(0, (newHeight - formerImage.getHeight()) / 2);
			
			im.brush.drawImage(formerImage, x, y, null);
		}
	}
	

	public ImagePane(Canvas canvas) {
		this.canvas = canvas;
		im = new ImageModel();
		im.pane = this;
		brushLock = new ReentrantLock();
	}
	
	public void init() {
		init(600, 600);
	}
	
	public void init(int width, int height) {
		im.image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		im.init();
		transform = new AffineTransform();
		Rectangle cbounds = canvas.getBounds();
		transform.translate(cbounds.getMinX(), cbounds.getMinY());
		transform.scale(
				((double) canvas.getWidth()) / ((double) width),
				(((double) canvas.getHeight()) / ((double) height))
				);
		zoomMax = (byte) (ZOOM_MAX_INDEX - Math.max(0, Math.log(Math.min(
				((double) canvas.getWidth()) / ((double) width),
				((double) canvas.getWidth()) / ((double) width)))));
	}

//	@Override public double getViewWidth() { return canvas.getWidth(); }
//	@Override public double getViewHeight() { return canvas.getHeight(); }
//	@Override public double getLimitWidth() { return im.getWidth(); }
//	@Override public double getLimitHeight() { return im.getHeight(); }


}
