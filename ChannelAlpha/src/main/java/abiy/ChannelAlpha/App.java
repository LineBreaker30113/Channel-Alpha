package abiy.ChannelAlpha;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.swing.SwingUtilities;

import abiy.ChannelAlpha.view.Canvas;
import abiy.ChannelAlpha.view.Window;

/**
 * Main application class for Channel Alpha
 */
public class App {
    
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private static final int UPDATE_INTERVAL_MS = 52; // ~19 FPS
    
    private Window window;
    private Canvas canvas;
    private Timer updateTimer;
    private boolean isRunning = false;

    /**
     * Main entry point for the application
     */
    public static void main(String[] args) {
        try {
            App app = new App();
            app.start();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to start application", e);
            System.err.println("Failed to start application: " + e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Starts the application
     */
    public void start() {
        SwingUtilities.invokeLater(() -> {
            try {
                initializeUI();
                startUpdateTimer();
                isRunning = true;
                LOGGER.info("Application started successfully");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to initialize UI", e);
                throw new RuntimeException("Failed to initialize UI", e);
            }
        });
    }
    
    /**
     * Initializes the user interface
     */
    private void initializeUI() {
        canvas = new Canvas();
        window = new Window(canvas, "Channel ALPHA v0.0 (test build)");
        
        // Set up canvas bounds and add to window
        canvas.setBounds(180, 50, 600, 600);
        window.add(canvas);
        
        // Add key listener to window
        window.addKeyListener(canvas.getKeyboardTracker());
    }
    
    /**
     * Starts the update timer for canvas updates
     */
    private void startUpdateTimer() {
        updateTimer = new Timer("CanvasUpdateTimer", true);
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isRunning && canvas != null && window != null) {
                    SwingUtilities.invokeLater(() -> {
                        try {
                            canvas.updateCanvas();
                            window.repaint();
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Error during canvas update", e);
                        }
                    });
                }
            }
        }, 0, UPDATE_INTERVAL_MS);
    }
    
    /**
     * Stops the application
     */
    public void stop() {
        isRunning = false;
        if (updateTimer != null) {
            updateTimer.cancel();
            updateTimer = null;
        }
        if (window != null) {
            window.dispose();
        }
        LOGGER.info("Application stopped");
    }
    
    /**
     * Gets the main window
     */
    public Window getWindow() {
        return window;
    }
    
    /**
     * Gets the canvas
     */
    public Canvas getCanvas() {
        return canvas;
    }
    
    /**
     * Checks if the application is running
     */
    public boolean isRunning() {
        return isRunning;
    }
}
