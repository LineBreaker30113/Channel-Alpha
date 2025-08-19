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
    
    private static final Logger LOGGER = Logger.getLogger(App.class.getName()); // Logger for application events
    private static final int UPDATE_INTERVAL_MS = 52; // ~19 FPS - Timer interval for canvas updates
    
    private Window window; // Main application window
    private Canvas canvas; // Canvas for drawing and image manipulation
    private Timer updateTimer; // Timer for regular canvas updates
    private boolean isRunning = false; // Flag to track application running state

    /**
     * Main entry point for the application
     */
    public static void main(String[] args) {
        try {
            App app = new App(); // Create new application instance
            app.start(); // Start the application
        } catch (Exception e) { // Catch any startup errors
            LOGGER.log(Level.SEVERE, "Failed to start application", e); // Log the error
            System.err.println("Failed to start application: " + e.getMessage()); // Print error to console
            System.exit(1); // Exit with error code
        }
    }
    
    /**
     * Starts the application
     */
    public void start() {
        SwingUtilities.invokeLater(() -> { // Ensure UI creation happens on EDT
            try {
                initializeUI(); // Set up the user interface
                startUpdateTimer(); // Start the update timer
                isRunning = true; // Mark application as running
                LOGGER.info("Application started successfully"); // Log successful startup
            } catch (Exception e) { // Catch any UI initialization errors
                LOGGER.log(Level.SEVERE, "Failed to initialize UI", e); // Log the error
                throw new RuntimeException("Failed to initialize UI", e); // Rethrow as runtime exception
            }
        });
    }
    
    /**
     * Initializes the user interface
     */
    private void initializeUI() {
        canvas = new Canvas(); // Create new canvas for drawing
        window = new Window(canvas, "Channel ALPHA v0.0 (test build)"); // Create main window with title
        
        // Set up canvas bounds and add to window
        canvas.setBounds(180, 50, 600, 600); // Position and size the canvas within the window
        window.add(canvas); // Add canvas to the window
        
        // Add key listener to window
        window.addKeyListener(canvas.getKeyboardTracker()); // Connect keyboard input to canvas
    }
    
    /**
     * Starts the update timer for canvas updates
     */
    private void startUpdateTimer() {
        updateTimer = new Timer("CanvasUpdateTimer", true); // Create daemon timer for canvas updates
        updateTimer.scheduleAtFixedRate(new TimerTask() { // Schedule recurring task
            @Override
            public void run() {
                if (isRunning && canvas != null && window != null) { // Check if application is still running
                    SwingUtilities.invokeLater(() -> { // Ensure updates happen on EDT
                        try {
                            canvas.updateCanvas(); // Update canvas state (mouse, keyboard, drawing)
                            window.repaint(); // Trigger window repaint
                        } catch (Exception e) { // Catch any update errors
                            LOGGER.log(Level.WARNING, "Error during canvas update", e); // Log warning
                        }
                    });
                }
            }
        }, 0, UPDATE_INTERVAL_MS); // Start immediately, repeat every 52ms
    }
    
    /**
     * Stops the application
     */
    public void stop() {
        isRunning = false; // Mark application as stopped
        if (updateTimer != null) { // Check if timer exists
            updateTimer.cancel(); // Cancel the update timer
            updateTimer = null; // Clear timer reference
        }
        if (window != null) { // Check if window exists
            window.dispose(); // Close and dispose the window
        }
        LOGGER.info("Application stopped"); // Log application stop
    }
    
    /**
     * Gets the main window
     */
    public Window getWindow() {
        return window; // Return the main window
    }
    
    /**
     * Gets the canvas
     */
    public Canvas getCanvas() {
        return canvas; // Return the canvas
    }
    
    /**
     * Checks if the application is running
     */
    public boolean isRunning() {
        return isRunning; // Return running state
    }
}
