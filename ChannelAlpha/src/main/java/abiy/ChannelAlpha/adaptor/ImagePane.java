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
	public double zoomXratio = 1., zoomYratio = 1.; // Current zoom ratios for X and Y axes
	/** The power of the "ZOOM_MULTIPLIER" thats equal to current zoom level. */
	public byte zoomXindex = 0, zoomYindex = 0, zoomMax = 0; // Zoom level indices and maximum zoom
	/** A single Image for data, later on we may make this hold more images. */
	public ImageModel im; // Image model containing the actual image data
	/** The GUI element associated with. Later we may make this canvas have more than 1 ImagePanes. */
	public Canvas canvas; // Reference to the canvas that displays this image pane
	/** The Lock for the brush of the "im". All of the locking and unlocking should be done in this class for ease of development. */
	public ReentrantLock brushLock; // Lock for thread-safe brush operations


	/** The brush sizes for left and right clicks. */
	public float brush1size = 2.f, brush3size = 3.f; // Brush sizes for left (1) and right (3) mouse buttons
	/** The brush colors for left and right clicks. */
	public Color m1color = Color.black, m3color = Color.white; // Colors for left (1) and right (3) mouse buttons
	
	/**
	 * Gets the primary color (left mouse button)
	 */
	public Color getPrimaryColor() {
		return m1color; // Return the primary color used for left mouse button
	}
	
	/**
	 * Sets the primary color (left mouse button)
	 */
	public void setPrimaryColor(Color color) {
		this.m1color = color; // Set the primary color for left mouse button
	}
	
	/**
	 * Gets the secondary color (right mouse button)
	 */
	public Color getSecondaryColor() {
		return m3color; // Return the secondary color used for right mouse button
	}
	
	/**
	 * Sets the secondary color (right mouse button)
	 */
	public void setSecondaryColor(Color color) {
		this.m3color = color; // Set the secondary color for right mouse button
	}
	
	/**
	 * Gets the current image
	 */
	public BufferedImage getImage() {
		return im.image; // Return the current buffered image
	}
	
	/**
	 * Gets the current image dimensions
	 */
	public java.awt.Dimension getImageSize() {
		if (im.image != null) { // Check if image exists
			return new java.awt.Dimension(im.image.getWidth(), im.image.getHeight()); // Return image dimensions
		}
		return new java.awt.Dimension(0, 0); // Return zero dimensions if no image
	}
	
	/**
	 * Loads an image from a file
	 */
	public void loadImage(java.io.File file) throws IOException {
		im.image = ImageIO.read(file); // Read image from file using ImageIO
		if (im.image != null) { // Check if image was successfully loaded
			im.raster = im.image.getRaster(); // Get the raster data from the loaded image
		} else {
			throw new IOException("Failed to read image file"); // Throw exception if loading failed
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
		brush.drawImage(im.image, transform, canvas); // Draw the image using the current transform
//		brush.setRenderingHint(RenderingHints.KEY_ANTIALIASING, renderingAAkey);
	}
	
	public void scale(boolean in, Point location) {
		boolean scalingX = true, scalingY = true; // Flags to determine if X and Y should be scaled
		if(in) { // If zooming in
			scalingX = zoomXindex != zoomMax; // Scale X if not at maximum zoom
			zoomXindex += scalingX ? 1 : 0; // Increment X zoom index if scaling
			scalingY = zoomYindex != zoomMax; // Scale Y if not at maximum zoom
			zoomYindex += scalingY ? 1 : 0; // Increment Y zoom index if scaling
		} else { // If zooming out
			scalingX = zoomXindex != ZOOM_MIN_INDEX; // Scale X if not at minimum zoom
			zoomXindex -= scalingX ? 1 : 0; // Decrement X zoom index if scaling
			scalingY = zoomYindex != ZOOM_MIN_INDEX; // Scale Y if not at minimum zoom
			zoomYindex -= scalingY ? 1 : 0; // Decrement Y zoom index if scaling
		}
		double scaler = in ? ZOOM_MULTIPLIER : ZOOM_DIVIDER; // Determine scale factor based on zoom direction
		Point2D originalPointInImageSpace = new Point2D.Double(); // Point to store original image coordinates
		try {
			transform.inverseTransform(location, originalPointInImageSpace); // Convert display coordinates to image coordinates
		} catch (NoninvertibleTransformException ex) {
			ex.printStackTrace(); // Should not happen in a correctly initialized transform
			return; // Exit if transform is not invertible
		}
		transform.scale(scalingX ? scaler : 1., scalingY ? scaler : 1.); // Apply scaling transformation
		Point2D newPointInImageSpace = new Point2D.Double(); // Point to store new image coordinates
		try {
			transform.inverseTransform(location, newPointInImageSpace); // Convert display coordinates to new image coordinates
		} catch (NoninvertibleTransformException ex) {
			ex.printStackTrace(); // Should not happen in a correctly initialized transform
			return; // Exit if transform is not invertible
		}
		transform.translate( // Translate to keep the zoom centered on the mouse location
				newPointInImageSpace.getX() -
				originalPointInImageSpace.getX(),
				newPointInImageSpace.getY() -
				originalPointInImageSpace.getY());
		zoomXratio *= scalingX ? scaler : 1.; // Update X zoom ratio if scaling
		zoomYratio *= scalingY ? scaler : 1.; // Update Y zoom ratio if scaling
	}
	
	private AffineTransform transform; // Transformation matrix for zoom and pan operations

	public void updatePane() {
		float moveSpeed = 5.0f; // Speed of movement when using arrow keys
		if (canvas.kbt.isDAdown()) { transform.translate(0., -moveSpeed / zoomYratio); } // Move down if down arrow pressed
		if (canvas.kbt.isUAdown()) { transform.translate(0., +moveSpeed / zoomYratio); } // Move up if up arrow pressed
		if (canvas.kbt.isRAdown()) { transform.translate(-moveSpeed / zoomXratio, 0.); } // Move right if right arrow pressed
		if (canvas.kbt.isLAdown()) { transform.translate(+moveSpeed / zoomXratio, 0.); } // Move left if left arrow pressed
		if (!canvas.mut.mouseInside) { return; } // Exit if mouse is not inside the canvas
	}
	
	public Point2D.Double mapDisplay2Image(Point displayLocation) {
		Point2D.Double result = new Point2D.Double(); // Create result point
		try {
			transform.inverseTransform(displayLocation, result); // Transform display coordinates to image coordinates
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace(); // Print stack trace if transform fails
		}
		return result; // Return the transformed coordinates
	}
	
	public boolean isWithin(Point2D.Double point) {
		return point.x >= 0 && point.y >= 0 && point.x < im.getWidth() && point.y < im.getHeight(); // Check if point is within image bounds
	}
	
	public void drawSquare(Point2D point, int buttonIndex) {
		float brushSize = buttonIndex == 1 ? brush1size : brush3size; // Get brush size based on button index
		Color color = buttonIndex == 1 ? m1color : m3color; // Get color based on button index
		if(brushSize <= 0.1f) { return; } // Exit if brush size is too small
		
		brushLock.lock(); // Acquire lock for thread safety
		try {
			im.brush.setColor(color); // Set the brush color
			im.drawSquare(point.getX(), point.getY(), brushSize); // Draw a square at the specified point
		} finally {
			brushLock.unlock(); // Always release the lock
		}
	}

	public void drawStroke(Point2D begin, Point2D end, int buttonIndex) {
		float brushSize = buttonIndex == 1 ? brush1size : brush3size; // Get brush size based on button index
		if(brushSize <= 0.1f) { return; } // Exit if brush size is too small
		
		brushLock.lock(); // Acquire lock for thread safety
		try {
			im.drawStroke(begin, end, buttonIndex == 1 ? m1color : m3color, brushSize); // Draw stroke between two points
		} finally {
			brushLock.unlock(); // Always release the lock
		}
	}

	public void resize(int newWidth, int newHeight) {
		if (newWidth <= 0 || newHeight <= 0) { // Validate input dimensions
			throw new IllegalArgumentException("Width and height must be positive"); // Throw exception for invalid dimensions
		}
		
		BufferedImage formerImage = im.image; // Store reference to current image
		im.free(); // Free current image resources
		init(newWidth, newHeight); // Initialize new image with new dimensions

		if (formerImage != null) { // Check if there was a previous image
			int x = Math.max(0, (newWidth - formerImage.getWidth()) / 2); // Calculate X offset to center the image
			int y = Math.max(0, (newHeight - formerImage.getHeight()) / 2); // Calculate Y offset to center the image
			
			im.brush.drawImage(formerImage, x, y, null); // Draw the old image centered in the new image
		}
	}
	

	public ImagePane(Canvas canvas) {
		this.canvas = canvas; // Store reference to the canvas
		im = new ImageModel(); // Create new image model
		im.pane = this; // Set this pane as the owner of the image model
		brushLock = new ReentrantLock(); // Initialize the brush lock for thread safety
	}
	
	public void init() {
		init(600, 600); // Initialize with default 600x600 dimensions
	}
	
	public void init(int width, int height) {
		im.image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR); // Create new buffered image with specified dimensions
		im.init(); // Initialize the image model
		transform = new AffineTransform(); // Create new transformation matrix
		Rectangle cbounds = canvas.getBounds(); // Get canvas bounds
		transform.translate(cbounds.getMinX(), cbounds.getMinY()); // Translate to canvas position
		transform.scale( // Scale to fit canvas
				((double) canvas.getWidth()) / ((double) width),
				(((double) canvas.getHeight()) / ((double) height))
				);
		zoomMax = (byte) (ZOOM_MAX_INDEX - Math.max(0, Math.log(Math.min( // Calculate maximum zoom level
				((double) canvas.getWidth()) / ((double) width),
				((double) canvas.getWidth()) / ((double) width)))));
	}

//	@Override public double getViewWidth() { return canvas.getWidth(); }
//	@Override public double getViewHeight() { return canvas.getHeight(); }
//	@Override public double getLimitWidth() { return im.getWidth(); }
//	@Override public double getLimitHeight() { return im.getHeight(); }


}
