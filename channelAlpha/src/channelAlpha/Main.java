package channelAlpha;

import java.util.TimerTask;

import channelAlpha.view.Canvas;
import channelAlpha.view.Window;

public class Main {

	public static Window window;
	public static Canvas canvas;
	public static TimerTask updater;
	public static java.util.Timer updatingTimer = new java.util.Timer();
	
	public static void main(String[] args) {
		canvas = new Canvas();
		window = new Window(canvas, "Channel ALPHA v0.0 (test build)");
		window.addKeyListener(canvas.kbt);
		canvas.setSize(600, 600);
		canvas.setLocation(180, 50);
		window.add(canvas);
		updater = new TimerTask() {
			@Override
			public void run() {
				canvas.updateCanvas();
				window.repaint();
			}
		};
		updatingTimer.scheduleAtFixedRate(updater, 0, 112);
		
	}
	
}
