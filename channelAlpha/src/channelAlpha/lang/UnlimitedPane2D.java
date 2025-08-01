package channelAlpha.lang;

public abstract class UnlimitedPane2D {
	
	public final static float ZOOM_MULTIPLIER = 17.f / 16.f, ZOOM_DIVIDER = 16.f / 17.f;

	public double zoomXratio = 1., zoomYratio = 1.;
	public double centerX = 0., centerY = 0.;
	
	public void paintSpace(java.awt.Graphics2D g, int top, int left) {
		java.awt.Color gc = g.getColor();
		g.setColor(java.awt.Color.LIGHT_GRAY);
		g.fillRect(left, top, (int) getViewWidth(), (int) getViewHeight());
		g.setColor(java.awt.Color.RED);
		int mpw = 1 + (int) ((getHalfViewWidth() - ((int)getHalfViewWidth())) * 2.1);
		int mph = 1 + (int) ((getHalfViewHeight() - ((int)getHalfViewHeight())) * 2.1);
		g.fillRect(top + ((int) getHalfViewWidth()), left + ((int)getHalfViewHeight()), mpw, mph);
		g.setColor(java.awt.Color.BLUE);
		g.fillRect(top + ((int) getViewXbyPaneX(0)), left + ((int)getViewYbyPaneY(0)), mpw, mph);
		g.setColor(gc);
	}
	
	
	public abstract double getViewWidth();
	public abstract double getViewHeight();

	public double getHalfViewWidth() { return getViewWidth() / 2; }
	public double getHalfViewHeight() { return getViewHeight() / 2; }
	

	
	public double getPaneXbyViewX(double x) {
		return (double) ((x - getHalfViewWidth()) / zoomXratio + centerX);
	}
	public double getViewXbyPaneX(double x) {
		return (double) ((x - centerX) * zoomXratio + getHalfViewWidth());
	}

	public double getPaneYbyViewY(double y) {
		return (double) ((-y + getHalfViewHeight()) / zoomYratio + centerY);
	}
	public double getViewYbyPaneY(double y) {
		return (double) (-((y - centerY) * zoomYratio - getHalfViewHeight()));
	}
	
	public void zoomXat(boolean in, double viewLoaction) {
		double former = getPaneXbyViewX(viewLoaction);
		zoomXratio *= in ? ZOOM_MULTIPLIER : ZOOM_DIVIDER;
		double recent = getPaneXbyViewX(viewLoaction);
		centerX -= recent - former;
	}
	public void zoomYat(boolean in, double viewLoaction) {
		double former = getPaneYbyViewY(viewLoaction);
		zoomYratio *= in ? ZOOM_MULTIPLIER : ZOOM_DIVIDER;
		double recent = getPaneYbyViewY(viewLoaction);
		centerY -= recent - former;
	}
	
	
	
	private static class UPpointer { public UnlimitedPane2D up = null; }
	
	public static void main(String[] args) {
		javax.swing.JFrame frame = new javax.swing.JFrame("Unlimited Pane Test");
		frame.setSize(600, 600);
		UPpointer uppointer = new UPpointer();
		javax.swing.JPanel panel = new javax.swing.JPanel() {
			javax.swing.JPanel self = this;
			UPpointer upp = uppointer;
			@Override public void paintComponent(java.awt.Graphics grap) {
				upp.up.paintSpace((java.awt.Graphics2D)grap, 0, 0);
				int drawX = (int) upp.up.getViewXbyPaneX(30);
				int drawY = (int) upp.up.getViewYbyPaneY(30);
				int drawW = (int) upp.up.getViewXbyPaneX(60) - drawX;
				int drawH = (int) upp.up.getViewYbyPaneY(0) - drawY;
				grap.setColor(java.awt.Color.BLUE);
				grap.fillRect(drawX, drawY, drawW, drawH);
			}
		};
		uppointer.up = new UnlimitedPane2D() {
			@Override public double getViewWidth() { return panel.getWidth(); }
			@Override public double getViewHeight() { return panel.getHeight(); }
		};
		java.awt.event.MouseWheelListener ml = new java.awt.event.MouseWheelListener() {
			@Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
				if(e.getWheelRotation() > 0) {
					uppointer.up.zoomXat(false, e.getPoint().getX());
					uppointer.up.zoomYat(false, e.getPoint().getY());
				} 
				else if(e.getWheelRotation() < 0) {
					uppointer.up.zoomXat(true, e.getPoint().getX());
					uppointer.up.zoomYat(true, e.getPoint().getY());
				}
			}
		};
		java.awt.event.KeyListener kb = new java.awt.event.KeyListener() {
			@Override public void keyTyped(java.awt.event.KeyEvent e) {}
			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {
				switch(e.getKeyCode()) {
				case java.awt.event.KeyEvent.VK_D: uppointer.up.centerX += 5.; break;
				case java.awt.event.KeyEvent.VK_A: uppointer.up.centerX -= 5.; break;
				case java.awt.event.KeyEvent.VK_W: uppointer.up.centerY += 5.; break;
				case java.awt.event.KeyEvent.VK_S: uppointer.up.centerY -= 5.; break;
				case java.awt.event.KeyEvent.VK_H: uppointer.up.zoomXratio *= ZOOM_MULTIPLIER; break;
				case java.awt.event.KeyEvent.VK_K: uppointer.up.zoomXratio /= ZOOM_MULTIPLIER; break;
				case java.awt.event.KeyEvent.VK_U: uppointer.up.zoomYratio *= ZOOM_MULTIPLIER; break;
				case java.awt.event.KeyEvent.VK_J: uppointer.up.zoomYratio /= ZOOM_MULTIPLIER; break;
				case java.awt.event.KeyEvent.VK_T: uppointer.up.zoomXat(true, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getX() - panel.getLocationOnScreen().x); break;
				case java.awt.event.KeyEvent.VK_Y: uppointer.up.zoomXat(false, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getX() - panel.getLocationOnScreen().x); break;
				case java.awt.event.KeyEvent.VK_B: uppointer.up.zoomYat(true, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getY() - panel.getLocationOnScreen().y); break;
				case java.awt.event.KeyEvent.VK_N: uppointer.up.zoomYat(false, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getY() - panel.getLocationOnScreen().y); break;
				case java.awt.event.KeyEvent.VK_O:
					uppointer.up.zoomXat(true, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getX() - panel.getLocationOnScreen().x);
					uppointer.up.zoomYat(true, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getY() - panel.getLocationOnScreen().y);
					break;
				case java.awt.event.KeyEvent.VK_P:
					uppointer.up.zoomXat(false, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getX() - panel.getLocationOnScreen().x);
					uppointer.up.zoomYat(false, 
						java.awt.MouseInfo.getPointerInfo().getLocation().getY() - panel.getLocationOnScreen().y);
					break;
				}
			}
			@Override public void keyReleased(java.awt.event.KeyEvent e) { }
			
		};
		panel.addMouseWheelListener(ml);
		frame.add(panel);
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

}
