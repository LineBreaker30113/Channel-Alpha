package channelAlpha.lang;


public abstract class LimitedPane2D extends UnlimitedPane2D {
	
	public abstract double getLimitWidth();
	public abstract double getLimitHeight();

	public double getLimitHalfWidth() { return getLimitWidth() / 2.; }
	public double getLimitHalfHeight() { return getLimitHeight() / 2.; }
	

	public double getLimitXbyPaneX(double x) { return x + getLimitHalfWidth(); }
	public double getPaneXbyLimitX(double x) { return x - getLimitHalfWidth(); }

	public double getLimitYbyPaneY(double y) { return getLimitHalfWidth() - y; }
	public double getPaneYbyLimitY(double y) { return getLimitHalfWidth() - y; }
	
	public double getLimitXbyViewX(double x) {
		return getLimitXbyPaneX(super.getPaneXbyViewX(x));
	}
	public double getViewXbyLimitX(double x) {
		return super.getViewXbyPaneX(getPaneXbyLimitX(x)); 
	}

	public double getLimitYbyViewY(double y) {
		return getLimitYbyPaneY(super.getPaneYbyViewY(y)); 
	}
	public double getViewYbyLimitY(double y) {
		return super.getViewYbyPaneY(getPaneYbyLimitY(y));
	}
	
	private static class LPpointer { public LimitedPane2D lp = null; }
	
	public static void main(String[] args) {
		javax.swing.JFrame frame = new javax.swing.JFrame("Unlimited Pane Test");
		LPpointer lppointer = new LPpointer();
		javax.swing.JPanel panel = new javax.swing.JPanel() {
			javax.swing.JPanel self = this;
			LPpointer upp = lppointer;
			@Override public void paintComponent(java.awt.Graphics grap) {
				upp.lp.paintSpace((java.awt.Graphics2D)grap, 0, 0);
				int paneX = (int) upp.lp.getViewXbyPaneX(30);
				int paneY = (int) upp.lp.getViewYbyPaneY(30);
				int paneW = (int) upp.lp.getViewXbyPaneX(60) - paneX;
				int paneH = (int) upp.lp.getViewYbyPaneY(0) - paneY;
				grap.setColor(java.awt.Color.BLUE);
				grap.fillRect(paneX, paneY, paneW, paneH);
				int areaX = (int) upp.lp.getViewXbyLimitX(130);
				int areaY = (int) upp.lp.getViewYbyLimitY(130);
				int areaW = (int) upp.lp.getViewXbyLimitX(160) - areaX;
				int areaH = (int) upp.lp.getViewYbyLimitY(160) - areaY;
				grap.setColor(java.awt.Color.CYAN);
				grap.fillRect(areaX, areaY, areaW, areaH);
			}
		};
		lppointer.lp = new LimitedPane2D() {
			@Override public double getViewWidth() { return panel.getWidth(); }
			@Override public double getViewHeight() { return panel.getHeight(); }
			@Override public double getLimitWidth() { return 600.; }
			@Override public double getLimitHeight() { return 600.; }
		};
		java.awt.event.MouseWheelListener ml = new java.awt.event.MouseWheelListener() {
			@Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
				if(e.getWheelRotation() > 0) {
					lppointer.lp.zoomXat(false, e.getPoint().getX());
					lppointer.lp.zoomYat(false, e.getPoint().getY());
				} 
				else if(e.getWheelRotation() < 0) {
					lppointer.lp.zoomXat(true, e.getPoint().getX());
					lppointer.lp.zoomYat(true, e.getPoint().getY());
				}
			}
		};
		java.awt.event.KeyListener kb = new java.awt.event.KeyListener() {
			@Override public void keyTyped(java.awt.event.KeyEvent e) {}
			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {
				switch(e.getKeyCode()) {
				case java.awt.event.KeyEvent.VK_D: lppointer.lp.centerX += 5.; break;
				case java.awt.event.KeyEvent.VK_A: lppointer.lp.centerX -= 5.; break;
				case java.awt.event.KeyEvent.VK_W: lppointer.lp.centerY += 5.; break;
				case java.awt.event.KeyEvent.VK_S: lppointer.lp.centerY -= 5.; break;
				case java.awt.event.KeyEvent.VK_H: lppointer.lp.zoomXratio *= ZOOM_MULTIPLIER; break;
				case java.awt.event.KeyEvent.VK_K: lppointer.lp.zoomXratio /= ZOOM_MULTIPLIER; break;
				case java.awt.event.KeyEvent.VK_U: lppointer.lp.zoomYratio *= ZOOM_MULTIPLIER; break;
				case java.awt.event.KeyEvent.VK_J: lppointer.lp.zoomYratio /= ZOOM_MULTIPLIER; break;
				case java.awt.event.KeyEvent.VK_T: lppointer.lp.zoomXat(true, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getX() - panel.getLocationOnScreen().x); break;
				case java.awt.event.KeyEvent.VK_Y: lppointer.lp.zoomXat(false, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getX() - panel.getLocationOnScreen().x); break;
				case java.awt.event.KeyEvent.VK_B: lppointer.lp.zoomYat(true, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getY() - panel.getLocationOnScreen().y); break;
				case java.awt.event.KeyEvent.VK_N: lppointer.lp.zoomYat(false, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getY() - panel.getLocationOnScreen().y); break;
				case java.awt.event.KeyEvent.VK_O:
					lppointer.lp.zoomXat(true, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getX() - panel.getLocationOnScreen().x);
					lppointer.lp.zoomYat(true, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getY() - panel.getLocationOnScreen().y);
					break;
				case java.awt.event.KeyEvent.VK_P:
					lppointer.lp.zoomXat(false, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getX() - panel.getLocationOnScreen().x);
					lppointer.lp.zoomYat(false, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getY() - panel.getLocationOnScreen().y);
					break;
				}
			}
			@Override public void keyReleased(java.awt.event.KeyEvent e) { }
			
		};
		panel.setPreferredSize(new java.awt.Dimension(600, 600));
		panel.addMouseWheelListener(ml);
		frame.add(panel); frame.pack();
		frame.addKeyListener(kb);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		java.util.Timer timer = new java.util.Timer();
		java.util.TimerTask tt = new java.util.TimerTask() {
			@Override
			public void run() {
				frame.repaint();
			}
		};
		timer.scheduleAtFixedRate(tt, 0, 66);
	}
//		javax.swing.JFrame frame = new javax.swing.JFrame("Unlimited Pane Test");
//		frame.setSize(600, 600);
//		LPpointer uppointer = new LPpointer();
//		javax.swing.JPanel panel = new javax.swing.JPanel() {
//			LPpointer ulp = uppointer;
//			@Override public void paintComponent(java.awt.Graphics grap) {
//				ulp.lp.paintSpace((java.awt.Graphics2D)grap, 0, 0);
//				int drawX = (int) ulp.lp.getViewXbyLimitX(30);
//				int drawY = (int) ulp.lp.getViewYbyLimitY(30);
//				int drawW = (int) ulp.lp.getViewXbyLimitX(60) - drawX;
//				int drawH = (int) ulp.lp.getViewYbyLimitY(60) - drawY;
//				System.out.println("x:y "+drawX+"|"+drawY+" || "+drawW+"|"+drawH);
//				grap.setColor(java.awt.Color.BLUE);
//				grap.fillRect(drawX, drawY, drawW, drawH);
//			}
//		};
//		uppointer.lp = new LimitedPane2D() {
//			@Override public double getViewWidth() { return panel.getWidth(); }
//			@Override public double getViewHeight() { return panel.getHeight(); }
//			@Override public double getLimitWidth() { return 600.; }
//			@Override public double getLimitHeight() { return 600.; }
//		};
//		java.awt.event.KeyListener kb = new java.awt.event.KeyListener() {
//			@Override public void keyTyped(java.awt.event.KeyEvent e) {}
//			@Override
//			public void keyPressed(java.awt.event.KeyEvent e) {
//				switch(e.getKeyCode()) {
//				case java.awt.event.KeyEvent.VK_D: uppointer.lp.centerX += 5.; break;
//				case java.awt.event.KeyEvent.VK_A: uppointer.lp.centerX -= 5.; break;
//				case java.awt.event.KeyEvent.VK_W: uppointer.lp.centerY += 5.; break;
//				case java.awt.event.KeyEvent.VK_S: uppointer.lp.centerY -= 5.; break;
//				case java.awt.event.KeyEvent.VK_H: uppointer.lp.zoomXratio *= ZOOM_MULTIPLIER; break;
//				case java.awt.event.KeyEvent.VK_K: uppointer.lp.zoomXratio /= ZOOM_MULTIPLIER; break;
//				case java.awt.event.KeyEvent.VK_U: uppointer.lp.zoomYratio *= ZOOM_MULTIPLIER; break;
//				case java.awt.event.KeyEvent.VK_J: uppointer.lp.zoomYratio /= ZOOM_MULTIPLIER; break;
//				}
//			}
//			@Override public void keyReleased(java.awt.event.KeyEvent e) { }
//			
//		};
//		frame.add(panel);
//		frame.addKeyListener(kb);
//		frame.setLocationRelativeTo(null);
//		frame.setVisible(true);
//		java.util.Timer timer = new java.util.Timer();
//		java.util.TimerTask tt = new java.util.TimerTask() {
//			@Override
//			public void run() {
//				frame.repaint();
//			}
//		};
//		timer.scheduleAtFixedRate(tt, 0, 333);
//	}

}
