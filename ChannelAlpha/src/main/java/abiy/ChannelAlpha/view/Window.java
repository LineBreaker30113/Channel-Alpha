package abiy.ChannelAlpha.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Main application window for Channel Alpha
 */
public class Window extends JFrame {
    
    private static final Logger LOGGER = Logger.getLogger(Window.class.getName());
    
    // Constants for UI layout
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 700;
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 60;
    private static final int COLOR_BUTTON_SIZE = 70;
    private static final int LEFT_MARGIN = 30;
    private static final int TOP_MARGIN = 30;
    private static final int BUTTON_SPACING = 60;
    
    // UI Components
    private final Canvas canvas;
    private final JFileChooser saveDialog;
    private final JFileChooser loadDialog;
    private final JButton chooseColor1;
    private final JButton chooseColor3;
    private final JButton saveButton;
    private final JButton resizeButton;
    private final JButton loadButton;

    /**
     * Creates a new Window with the specified canvas and title
     * 
     * @param canvas the canvas to display
     * @param title the window title
     * @throws HeadlessException if running in headless environment
     */
    public Window(Canvas canvas, String title) throws HeadlessException {
        super(title);
        this.canvas = canvas;
        
        // Initialize file dialogs
        this.saveDialog = createSaveDialog();
        this.loadDialog = createLoadDialog();
        
        // Initialize UI components
        this.chooseColor1 = createColorButton("Primary Color", canvas.getImagePane().getPrimaryColor());
        this.chooseColor3 = createColorButton("Secondary Color", canvas.getImagePane().getSecondaryColor());
        this.saveButton = createSaveButton();
        this.loadButton = createLoadButton();
        this.resizeButton = createResizeButton();
        
        setupWindow();
        setupLayout();
        addKeyListener(canvas.getKeyboardTracker());
        
        canvas.init();
    }
    
    /**
     * Sets up the main window properties
     */
    private void setupWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.BLACK);
        setVisible(true);
    }
    
    /**
     * Sets up the layout of UI components
     */
    private void setupLayout() {
        // Position color buttons
        chooseColor1.setBounds(LEFT_MARGIN, TOP_MARGIN + 200, COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE);
        chooseColor3.setBounds(LEFT_MARGIN + 70, TOP_MARGIN + 200, COLOR_BUTTON_SIZE, COLOR_BUTTON_SIZE);
        
        // Position action buttons
        saveButton.setBounds(LEFT_MARGIN + 30, TOP_MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT);
        loadButton.setBounds(LEFT_MARGIN + 30, TOP_MARGIN + BUTTON_SPACING, BUTTON_WIDTH, BUTTON_HEIGHT);
        resizeButton.setBounds(LEFT_MARGIN + 30, TOP_MARGIN + BUTTON_SPACING * 2, BUTTON_WIDTH, BUTTON_HEIGHT);
        
        // Add components to window
        add(chooseColor1);
        add(chooseColor3);
        add(saveButton);
        add(loadButton);
        add(resizeButton);
    }
    
    /**
     * Creates a color selection button
     */
    private JButton createColorButton(String colorName, Color initialColor) {
        JButton button = new JButton();
        button.setFocusable(false);
        button.setBackground(initialColor);
        button.setToolTipText("Click to choose " + colorName.toLowerCase());
        
        button.addActionListener(event -> {
            Color newColor = JColorChooser.showDialog(this, "Choose " + colorName, initialColor);
            if (newColor != null) {
                if (colorName.equals("Primary Color")) {
                    canvas.getImagePane().setPrimaryColor(newColor);
                } else {
                    canvas.getImagePane().setSecondaryColor(newColor);
                }
                button.setBackground(newColor);
            }
        });
        
        return button;
    }
    
    /**
     * Creates the save button with file dialog functionality
     */
    private JButton createSaveButton() {
        JButton button = new JButton("Save");
        button.setFocusable(false);
        button.setBackground(Color.CYAN);
        button.setToolTipText("Save the current image as PNG");
        
        button.addActionListener(event -> handleSaveAction());
        
        return button;
    }
    
    /**
     * Creates the load button with file dialog functionality
     */
    private JButton createLoadButton() {
        JButton button = new JButton("Load");
        button.setFocusable(false);
        button.setBackground(Color.YELLOW);
        button.setToolTipText("Load an image file");
        
        button.addActionListener(event -> handleLoadAction());
        
        return button;
    }
    
    /**
     * Creates the resize button with dimension input dialog
     */
    private JButton createResizeButton() {
        JButton button = new JButton("Resize");
        button.setFocusable(false);
        button.setBackground(Color.GREEN);
        button.setToolTipText("Resize the current image");
        
        button.addActionListener(event -> handleResizeAction());
        
        return button;
    }
    
    /**
     * Creates the save file dialog
     */
    private JFileChooser createSaveDialog() {
        JFileChooser dialog = new JFileChooser();
        dialog.setDialogTitle("Save Image (PNG format)");
        dialog.setDialogType(JFileChooser.SAVE_DIALOG);
        dialog.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
        return dialog;
    }
    
    /**
     * Creates the load file dialog
     */
    private JFileChooser createLoadDialog() {
        JFileChooser dialog = new JFileChooser();
        dialog.setDialogTitle("Load Image");
        dialog.setDialogType(JFileChooser.OPEN_DIALOG);
        dialog.setFileFilter(new FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg", "gif", "bmp"));
        return dialog;
    }
    
    /**
     * Handles the save action
     */
    private void handleSaveAction() {
        if (saveDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = saveDialog.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".png")) {
                    filePath += ".png";
                }
                ImageIO.write(canvas.getImagePane().getImage(), "png", new java.io.File(filePath));
                JOptionPane.showMessageDialog(this, "Image saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to save image", e);
                JOptionPane.showMessageDialog(this, "Failed to save image: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Handles the load action
     */
    private void handleLoadAction() {
        if (loadDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                canvas.getImagePane().loadImage(loadDialog.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Image loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to load image", e);
                JOptionPane.showMessageDialog(this, "Failed to load image: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Handles the resize action
     */
    private void handleResizeAction() {
        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridLayout(2, 2, 5, 5));
        
        JLabel widthLabel = new JLabel("Width:");
        JLabel heightLabel = new JLabel("Height:");
        JTextField widthField = new JTextField(10);
        JTextField heightField = new JTextField(10);
        
        // Set current dimensions as default values
        Dimension currentSize = canvas.getImagePane().getImageSize();
        widthField.setText(String.valueOf(currentSize.width));
        heightField.setText(String.valueOf(currentSize.height));
        
        panel.add(widthLabel);
        panel.add(widthField);
        panel.add(heightLabel);
        panel.add(heightField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Enter new dimensions",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                int newWidth = Integer.parseInt(widthField.getText().trim());
                int newHeight = Integer.parseInt(heightField.getText().trim());
                
                if (newWidth <= 0 || newHeight <= 0) {
                    JOptionPane.showMessageDialog(this, "Width and height must be positive numbers!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (newWidth > 10000 || newHeight > 10000) {
                    JOptionPane.showMessageDialog(this, "Dimensions too large! Maximum is 10000x10000", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                canvas.getImagePane().resize(newWidth, newHeight);
                JOptionPane.showMessageDialog(this, "Image resized successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Gets the canvas associated with this window
     */
    public Canvas getCanvas() {
        return canvas;
    }
}
