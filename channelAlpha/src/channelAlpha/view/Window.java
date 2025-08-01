package channelAlpha.view;

import java.awt.Color;
import java.awt.HeadlessException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Window extends JFrame {

//	public Window() throws HeadlessException {
//		// TODO Auto-generated constructor stub
//	}
//
//	public Window(GraphicsConfiguration gc) {
//		super(gc);
//		// TODO Auto-generated constructor stub
//	}

	
	public Canvas canvas;

	public JFileChooser saveDialog, loadDialog;
	public JButton chooseColor1, chooseColor3, saveButton, resizeButton, loadButton;


	public Window(Canvas canvas, String title) throws HeadlessException {
		super(title);
		this.canvas = canvas;
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setSize(800, 700);
		super.setLocationRelativeTo(null);
		super.setLayout(null);
		super.getContentPane().setBackground(Color.black);
		super.setVisible(true);
		super.addKeyListener(canvas.kbt);
		
		canvas.init();

		chooseColor1 = new JButton(); chooseColor3 = new JButton();
		chooseColor1.setFocusable(false); chooseColor3.setFocusable(false);
		chooseColor1.setBounds(30, 230, 70, 70); chooseColor3.setBounds(100, 230, 70, 70);
		chooseColor1.setBackground(canvas.ip.m1color); chooseColor3.setBackground(canvas.ip.m3color);
		chooseColor1.addActionListener(event -> {
			Color candidate = JColorChooser.showDialog(this, "Choose Primary Color", canvas.ip.m1color);
			if(candidate == null) { return; }
			canvas.ip.m1color = candidate;
			chooseColor1.setBackground(canvas.ip.m1color);
		});
		chooseColor3.addActionListener(event -> {
			 Color candidate = JColorChooser.showDialog(this, "Choose Secondary Color", canvas.ip.m3color);
			 if(candidate == null) { return; }
			 canvas.ip.m3color = candidate;
			chooseColor3.setBackground(canvas.ip.m3color);
		});
		this.add(chooseColor1); this.add(chooseColor3);

		saveButton = new JButton("save"); saveButton.setFocusable(false);
		saveButton.setBounds(60, 30, 100, 60);
		saveButton.setBackground(Color.CYAN);
		saveDialog = new JFileChooser();
		saveDialog.setDialogTitle("Only PNG Supported");
		saveDialog.setDialogType(JFileChooser.SAVE_DIALOG);
//		saveDialog.setFileFilter(new FileFilter() {
//			@Override
//			public boolean accept(File f) {
//				char[] pathname.
//				return false;
//			}
//			@Override
//			public String getDescription() {
//				return "custom";
//			}
//			
//		});;
		saveButton.addActionListener(event -> {
			if(saveDialog.showDialog(this, "save") == JFileChooser.APPROVE_OPTION) {
				try {
					ImageIO.write(canvas.ip.im.image, "png", saveDialog.getSelectedFile());
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, "Image failed to be saved!");
					e.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(this, "Image will not be saved!");
			}
		});
		loadButton = new JButton("load"); loadButton.setFocusable(false);
		loadButton.setBounds(60, 90, 100, 60);
		loadButton.setBackground(Color.YELLOW);
		loadDialog = new JFileChooser();
		loadDialog.setDialogType(JFileChooser.FILES_ONLY);
		loadButton.addActionListener(event -> {
			if(loadDialog.showDialog(this, "load") == JFileChooser.APPROVE_OPTION) {
				try {
					canvas.ip.im.image = ImageIO.read(loadDialog.getSelectedFile());
					canvas.ip.im.raster = canvas.ip.im.image.getRaster();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, "Image failed to be loaded!");
					e.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(this, "Image will not be loaded!");
			}
		});
		this.add(saveButton); this.add(loadButton);
		resizeButton = new JButton("Resize");
		resizeButton.setFocusable(false);
		resizeButton.setBounds(60, 150, 100, 60);
		resizeButton.setBackground(Color.GREEN);
		resizeButton.addActionListener(event -> {
			JPanel panel = new JPanel();
			panel.setLayout(new java.awt.GridLayout(2, 2));

			JLabel widthLabel = new JLabel("Width: ");
			JLabel heightLabel = new JLabel("Height: ");
			JTextField widthField = new JTextField(10);
			JTextField heightField = new JTextField(10);

			panel.add(widthLabel);
			panel.add(widthField);
			panel.add(heightLabel);
			panel.add(heightField);

			int result = JOptionPane.showConfirmDialog(this, panel, "Enter new dimensions",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (result == JOptionPane.OK_OPTION) {
				try {
					int newWidth = Integer.parseInt(widthField.getText());
					int newHeight = Integer.parseInt(heightField.getText());

					if (newWidth <= 0 || newHeight <= 0) {
						JOptionPane.showMessageDialog(this, "Width and height must be positive numbers!", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					canvas.ip.resize(newWidth, newHeight);

					JOptionPane.showMessageDialog(this, "Image resized successfully!", "Success",
							JOptionPane.INFORMATION_MESSAGE);

				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this, "Please enter valid numbers!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		super.add(resizeButton);
	}

//	public Window(String title, GraphicsConfiguration gc) {
//		super(title, gc);
//		// TODO Auto-generated constructor stub
//	}

}
