package abiy.ChannelAlpha.lang;

public abstract class UnlimitedPane2D {
	
	public final static float ZOOM_MULTIPLIER = 17.f / 16.f, ZOOM_DIVIDER = 16.f / 17.f; // Constants for smooth zooming

	public double zoomXratio = 1., zoomYratio = 1.; // Current zoom ratios for X and Y axes
	public double centerX = 0., centerY = 0.; // Center position of the pane
	
	public void paintSpace(java.awt.Graphics2D g, int top, int left) {
		java.awt.Color gc = g.getColor(); // Store original graphics color
		g.setColor(java.awt.Color.LIGHT_GRAY); // Set background color to light gray
		g.fillRect(left, top, (int) getViewWidth(), (int) getViewHeight()); // Fill background rectangle
		g.setColor(java.awt.Color.RED); // Set color to red for center marker
		int mpw = 1 + (int) ((getHalfViewWidth() - ((int)getHalfViewWidth())) * 2.1); // Calculate marker width
		int mph = 1 + (int) ((getHalfViewHeight() - ((int)getHalfViewHeight())) * 2.1); // Calculate marker height
		g.fillRect(top + ((int) getHalfViewWidth()), left + ((int)getHalfViewHeight()), mpw, mph); // Draw center marker
		g.setColor(java.awt.Color.BLUE); // Set color to blue for pane origin marker
		g.fillRect(top + ((int) getViewXbyPaneX(0)), left + ((int)getViewYbyPaneY(0)), mpw, mph); // Draw pane origin marker
		g.setColor(gc); // Restore original graphics color
	}
	
	
	public abstract double getViewWidth(); // Abstract method to get view width
	public abstract double getViewHeight(); // Abstract method to get view height

	public double getHalfViewWidth() { return getViewWidth() / 2; } // Get half of view width
	public double getHalfViewHeight() { return getViewHeight() / 2; } // Get half of view height
	

	
	public double getPaneXbyViewX(double x) {
		return (double) ((x - getHalfViewWidth()) / zoomXratio + centerX); // Convert view X coordinate to pane X coordinate
	}
	public double getViewXbyPaneX(double x) {
		return (double) ((x - centerX) * zoomXratio + getHalfViewWidth()); // Convert pane X coordinate to view X coordinate
	}

	public double getPaneYbyViewY(double y) {
		return (double) ((-y + getHalfViewHeight()) / zoomYratio + centerY); // Convert view Y coordinate to pane Y coordinate
	}
	public double getViewYbyPaneY(double y) {
		return (double) (-((y - centerY) * zoomYratio - getHalfViewHeight())); // Convert pane Y coordinate to view Y coordinate
	}
	
	public void zoomXat(boolean in, double viewLoaction) {
		double former = getPaneXbyViewX(viewLoaction); // Get pane X coordinate before zoom
		zoomXratio *= in ? ZOOM_MULTIPLIER : ZOOM_DIVIDER; // Apply zoom factor
		double recent = getPaneXbyViewX(viewLoaction); // Get pane X coordinate after zoom
		centerX -= recent - former; // Adjust center to keep zoom point fixed
	}
	public void zoomYat(boolean in, double viewLoaction) {
		double former = getPaneYbyViewY(viewLoaction); // Get pane Y coordinate before zoom
		zoomYratio *= in ? ZOOM_MULTIPLIER : ZOOM_DIVIDER; // Apply zoom factor
		double recent = getPaneYbyViewY(viewLoaction); // Get pane Y coordinate after zoom
		centerY -= recent - former; // Adjust center to keep zoom point fixed
	}
	
	
	
	private static class UPpointer { public UnlimitedPane2D up = null; } // Helper class to hold reference for testing
	
	public static void main(String[] args) {
		javax.swing.JFrame frame = new javax.swing.JFrame("Unlimited Pane Test"); // Create test frame
		frame.setSize(600, 600); // Set frame size
		UPpointer uppointer = new UPpointer(); // Create pointer to hold pane reference
		javax.swing.JPanel panel = new javax.swing.JPanel() { // Create test panel
			javax.swing.JPanel self = this; // Reference to self
			UPpointer upp = uppointer; // Reference to pane pointer
			@Override public void paintComponent(java.awt.Graphics grap) {
				upp.up.paintSpace((java.awt.Graphics2D)grap, 0, 0); // Paint the pane space
				int drawX = (int) upp.up.getViewXbyPaneX(30); // Get view X coordinate for pane position 30
				int drawY = (int) upp.up.getViewYbyPaneY(30); // Get view Y coordinate for pane position 30
				int drawW = (int) upp.up.getViewXbyPaneX(60) - drawX; // Calculate drawing width
				int drawH = (int) upp.up.getViewYbyPaneY(0) - drawY; // Calculate drawing height
				grap.setColor(java.awt.Color.BLUE); // Set color to blue
				grap.fillRect(drawX, drawY, drawW, drawH); // Draw blue rectangle
			}
		};
		uppointer.up = new UnlimitedPane2D() { // Create anonymous implementation for testing
			@Override public double getViewWidth() { return panel.getWidth(); } // Return panel width as view width
			@Override public double getViewHeight() { return panel.getHeight(); } // Return panel height as view height
		};
		java.awt.event.MouseWheelListener ml = new java.awt.event.MouseWheelListener() { // Create mouse wheel listener
			@Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
				if(e.getWheelRotation() > 0) { // If scrolling down (zoom out)
					uppointer.up.zoomXat(false, e.getPoint().getX()); // Zoom out X at mouse X position
					uppointer.up.zoomYat(false, e.getPoint().getY()); // Zoom out Y at mouse Y position
				} 
				else if(e.getWheelRotation() < 0) { // If scrolling up (zoom in)
					uppointer.up.zoomXat(true, e.getPoint().getX()); // Zoom in X at mouse X position
					uppointer.up.zoomYat(true, e.getPoint().getY()); // Zoom in Y at mouse Y position
				}
			}
		};
		java.awt.event.KeyListener kb = new java.awt.event.KeyListener() { // Create keyboard listener
			@Override public void keyTyped(java.awt.event.KeyEvent e) {} // Not used
			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {
				switch(e.getKeyCode()) { // Handle different key presses
				case java.awt.event.KeyEvent.VK_D: uppointer.up.centerX += 5.; break; // Move right with D key
				case java.awt.event.KeyEvent.VK_A: uppointer.up.centerX -= 5.; break; // Move left with A key
				case java.awt.event.KeyEvent.VK_W: uppointer.up.centerY += 5.; break; // Move up with W key
				case java.awt.event.KeyEvent.VK_S: uppointer.up.centerY -= 5.; break; // Move down with S key
				case java.awt.event.KeyEvent.VK_H: uppointer.up.zoomXratio *= ZOOM_MULTIPLIER; break; // Zoom in X with H key
				case java.awt.event.KeyEvent.VK_K: uppointer.up.zoomXratio /= ZOOM_MULTIPLIER; break; // Zoom out X with K key
				case java.awt.event.KeyEvent.VK_U: uppointer.up.zoomYratio *= ZOOM_MULTIPLIER; break; // Zoom in Y with U key
				case java.awt.event.KeyEvent.VK_J: uppointer.up.zoomYratio /= ZOOM_MULTIPLIER; break; // Zoom out Y with J key
				}
			}
			@Override public void keyReleased(java.awt.event.KeyEvent e) { } // Not used
			
		};
		panel.setPreferredSize(new java.awt.Dimension(600, 600)); // Set panel preferred size
		panel.addMouseWheelListener(ml); // Add mouse wheel listener to panel
		frame.add(panel); frame.pack(); // Add panel to frame and pack
		frame.addKeyListener(kb); // Add keyboard listener to frame
		frame.setLocationRelativeTo(null); // Center frame on screen
		frame.setVisible(true); // Make frame visible
		java.util.Timer timer = new java.util.Timer(); // Create timer for repainting
		java.util.TimerTask tt = new java.util.TimerTask() { // Create timer task
			@Override
			public void run() {
				frame.repaint(); // Repaint frame every timer tick
			}
		};
		timer.scheduleAtFixedRate(tt, 0, 66); // Schedule timer to run every 66ms (~15 FPS)
	}

}
