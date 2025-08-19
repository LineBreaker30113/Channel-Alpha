package abiy.ChannelAlpha.lang;


public abstract class LimitedPane2D extends UnlimitedPane2D {
	
	public abstract double getLimitWidth(); // Abstract method to get the limited pane width
	public abstract double getLimitHeight(); // Abstract method to get the limited pane height

	public double getLimitHalfWidth() { return getLimitWidth() / 2.; } // Get half of the limited pane width
	public double getLimitHalfHeight() { return getLimitHeight() / 2.; } // Get half of the limited pane height
	

	public double getLimitXbyPaneX(double x) { return x + getLimitHalfWidth(); } // Convert pane X coordinate to limit X coordinate
	public double getPaneXbyLimitX(double x) { return x - getLimitHalfWidth(); } // Convert limit X coordinate to pane X coordinate

	public double getLimitYbyPaneY(double y) { return getLimitHalfWidth() - y; } // Convert pane Y coordinate to limit Y coordinate
	public double getPaneYbyLimitY(double y) { return getLimitHalfWidth() - y; } // Convert limit Y coordinate to pane Y coordinate
	
	public double getLimitXbyViewX(double x) {
		return getLimitXbyPaneX(super.getPaneXbyViewX(x)); // Convert view X coordinate to limit X coordinate through pane
	}
	public double getViewXbyLimitX(double x) {
		return super.getViewXbyPaneX(getPaneXbyLimitX(x)); // Convert limit X coordinate to view X coordinate through pane
	}

	public double getLimitYbyViewY(double y) {
		return getLimitYbyPaneY(super.getPaneYbyViewY(y)); // Convert view Y coordinate to limit Y coordinate through pane
	}
	public double getViewYbyLimitY(double y) {
		return super.getPaneYbyViewY(getPaneYbyLimitY(y)); // Convert limit Y coordinate to view Y coordinate through pane
	}
	
	private static class LPpointer { public LimitedPane2D lp = null; } // Helper class to hold reference for testing
	
	public static void main(String[] args) {
		javax.swing.JFrame frame = new javax.swing.JFrame("Unlimited Pane Test"); // Create test frame
		LPpointer lppointer = new LPpointer(); // Create pointer to hold pane reference
		javax.swing.JPanel panel = new javax.swing.JPanel() { // Create test panel
			javax.swing.JPanel self = this; // Reference to self
			LPpointer upp = lppointer; // Reference to pane pointer
			@Override public void paintComponent(java.awt.Graphics grap) {
				upp.lp.paintSpace((java.awt.Graphics2D)grap, 0, 0); // Paint the pane space
				int paneX = (int) upp.lp.getViewXbyPaneX(30); // Get pane X coordinate for view position 30
				int paneY = (int) upp.lp.getViewYbyPaneY(30); // Get pane Y coordinate for view position 30
				int paneW = (int) upp.lp.getViewXbyPaneX(60) - paneX; // Calculate pane width
				int paneH = (int) upp.lp.getViewYbyPaneY(0) - paneY; // Calculate pane height
				grap.setColor(java.awt.Color.BLUE); // Set color to blue
				grap.fillRect(paneX, paneY, paneW, paneH); // Draw blue rectangle for pane area
				int areaX = (int) upp.lp.getViewXbyLimitX(130); // Get limit area X coordinate
				int areaY = (int) upp.lp.getViewYbyLimitY(130); // Get limit area Y coordinate
				int areaW = (int) upp.lp.getViewXbyLimitX(160) - areaX; // Calculate limit area width
				int areaH = (int) upp.lp.getViewYbyLimitY(160) - areaY; // Calculate limit area height
				grap.setColor(java.awt.Color.CYAN); // Set color to cyan
				grap.fillRect(areaX, areaY, areaW, areaH); // Draw cyan rectangle for limit area
			}
		};
		lppointer.lp = new LimitedPane2D() { // Create anonymous implementation for testing
			@Override public double getViewWidth() { return panel.getWidth(); } // Return panel width as view width
			@Override public double getViewHeight() { return panel.getHeight(); } // Return panel height as view height
			@Override public double getLimitWidth() { return 600.; } // Return 600 as limit width
			@Override public double getLimitHeight() { return 600.; } // Return 600 as limit height
		};
		java.awt.event.MouseWheelListener ml = new java.awt.event.MouseWheelListener() { // Create mouse wheel listener
			@Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
				if(e.getWheelRotation() > 0) { // If scrolling down (zoom out)
					lppointer.lp.zoomXat(false, e.getPoint().getX()); // Zoom out X at mouse X position
					lppointer.lp.zoomYat(false, e.getPoint().getY()); // Zoom out Y at mouse Y position
				} 
				else if(e.getWheelRotation() < 0) { // If scrolling up (zoom in)
					lppointer.lp.zoomXat(true, e.getPoint().getX()); // Zoom in X at mouse X position
					lppointer.lp.zoomYat(true, e.getPoint().getY()); // Zoom in Y at mouse Y position
				}
			}
		};
		java.awt.event.KeyListener kb = new java.awt.event.KeyListener() { // Create keyboard listener
			@Override public void keyTyped(java.awt.event.KeyEvent e) {} // Not used
			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {
				switch(e.getKeyCode()) { // Handle different key presses
				case java.awt.event.KeyEvent.VK_D: lppointer.lp.centerX += 5.; break; // Move right with D key
				case java.awt.event.KeyEvent.VK_A: lppointer.lp.centerX -= 5.; break; // Move left with A key
				case java.awt.event.KeyEvent.VK_W: lppointer.lp.centerY += 5.; break; // Move up with W key
				case java.awt.event.KeyEvent.VK_S: lppointer.lp.centerY -= 5.; break; // Move down with S key
				case java.awt.event.KeyEvent.VK_H: lppointer.lp.zoomXratio *= ZOOM_MULTIPLIER; break; // Zoom in X with H key
				case java.awt.event.KeyEvent.VK_K: lppointer.lp.zoomXratio /= ZOOM_MULTIPLIER; break; // Zoom out X with K key
				case java.awt.event.KeyEvent.VK_U: lppointer.lp.zoomYratio *= ZOOM_MULTIPLIER; break; // Zoom in Y with U key
				case java.awt.event.KeyEvent.VK_J: lppointer.lp.zoomYratio /= ZOOM_MULTIPLIER; break; // Zoom out Y with J key
				case java.awt.event.KeyEvent.VK_T: lppointer.lp.zoomXat(true, // Zoom in X at mouse position with T key
						java.awt.MouseInfo.getPointerInfo().getLocation().getX() - panel.getLocationOnScreen().x); break;
				case java.awt.event.KeyEvent.VK_Y: lppointer.lp.zoomXat(false, // Zoom out X at mouse position with Y key
						java.awt.MouseInfo.getPointerInfo().getLocation().getX() - panel.getLocationOnScreen().x); break;
				case java.awt.event.KeyEvent.VK_B: lppointer.lp.zoomYat(true, // Zoom in Y at mouse position with B key
						java.awt.MouseInfo.getPointerInfo().getLocation().getY() - panel.getLocationOnScreen().y); break;
				case java.awt.event.KeyEvent.VK_N: lppointer.lp.zoomYat(false, // Zoom out Y at mouse position with N key
						java.awt.MouseInfo.getPointerInfo().getLocation().getY() - panel.getLocationOnScreen().y); break;
				case java.awt.event.KeyEvent.VK_O: // Zoom in both X and Y at mouse position with O key
					lppointer.lp.zoomXat(true, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getX() - panel.getLocationOnScreen().x);
					lppointer.lp.zoomYat(true, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getY() - panel.getLocationOnScreen().y); break;
				case java.awt.event.KeyEvent.VK_P: // Zoom out both X and Y at mouse position with P key
					lppointer.lp.zoomXat(false, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getX() - panel.getLocationOnScreen().x);
					lppointer.lp.zoomYat(false, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getY() - panel.getLocationOnScreen().y); break;
				case java.awt.event.KeyEvent.VK_R: // Reset zoom ratios to 1.0 with R key
					lppointer.lp.zoomXratio = 1.; lppointer.lp.zoomYratio = 1.; break;
				case java.awt.event.KeyEvent.VK_SPACE: // Reset center position to (0,0) with spacebar
					lppointer.lp.centerX = 0.; lppointer.lp.centerY = 0.; break;
				}
			}
			@Override public void keyReleased(java.awt.event.KeyEvent e) {} // Not used
		};
		panel.addMouseWheelListener(ml); // Add mouse wheel listener to panel
		panel.addKeyListener(kb); // Add keyboard listener to panel
		panel.setFocusable(true); // Make panel focusable for keyboard input
		frame.add(panel); // Add panel to frame
		frame.setSize(800, 600); // Set frame size
		frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE); // Set close operation
		frame.setVisible(true); // Make frame visible
	}
}
