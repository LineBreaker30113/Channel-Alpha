package abiy.ChannelAlpha;

import java.util.TimerTask;

import javax.swing.SwingUtilities;

import abiy.ChannelAlpha.view.Canvas;
import abiy.ChannelAlpha.view.Window;

/**
 * Hello world!
 */
public class App {

	public static Window window;
	public static Canvas canvas;
	public static TimerTask updater;
	public static java.util.Timer updatingTimer = new java.util.Timer();

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			canvas = new Canvas();
			window = new Window(canvas, "Channel ALPHA v0.0 (test build)");
			window.addKeyListener(canvas.kbt);
			canvas.setBounds(180, 50, 600, 600);
			window.add(canvas);
			
		});
		updater = new TimerTask() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(() -> {
					canvas.updateCanvas();
					window.repaint();
				});
			}
		};
		updatingTimer.scheduleAtFixedRate(updater, 0, 52);
	}

}
