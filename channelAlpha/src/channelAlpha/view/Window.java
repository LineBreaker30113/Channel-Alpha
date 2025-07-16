package channelAlpha.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
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

import channelAlpha.adaptor.Canvas;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 700);
		setLocationRelativeTo(null);
		setLayout(null);
		getContentPane().setBackground(Color.black);
		setVisible(true);
		
		canvas.init();

		chooseColor1 = new JButton(); chooseColor3 = new JButton();
		chooseColor1.setFocusable(false); chooseColor3.setFocusable(false);
		chooseColor1.setBounds(30, 230, 70, 70); chooseColor3.setBounds(100, 230, 70, 70);
		chooseColor1.setBackground(canvas.im.m1color); chooseColor3.setBackground(canvas.im.m3color);
		chooseColor1.addActionListener(event -> {
			Color candidate = JColorChooser.showDialog(this, "Choose Primary Color", canvas.im.m1color);
			if(candidate == null) { return; }
			canvas.im.m1color = candidate;
			chooseColor1.setBackground(canvas.im.m1color);
		});
		chooseColor3.addActionListener(event -> {
			 Color candidate = JColorChooser.showDialog(this, "Choose Secondary Color", canvas.im.m3color);
			 if(candidate == null) { return; }
			 canvas.im.m3color = candidate;
			chooseColor3.setBackground(canvas.im.m3color);
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
					ImageIO.write(canvas.im.image, "png", saveDialog.getSelectedFile());
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
					canvas.im.image = ImageIO.read(loadDialog.getSelectedFile());
					canvas.im.rasterOFimage = canvas.im.image.getRaster();
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

					BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_3BYTE_BGR);
					Graphics2D g = newImage.createGraphics();
					g.setBackground(Color.white);
					g.clearRect(0, 0, newWidth, newHeight);

					// Eski resmi merkezden yeni resme çizme
					int x = (newWidth - canvas.im.image.getWidth()) / 2;
					int y = (newHeight - canvas.im.image.getHeight()) / 2;
					if (x < 0)
						x = 0;
					if (y < 0)
						y = 0;

					g.drawImage(canvas.im.image, x, y, null);
					g.dispose();

					// Yeni resmi atama
					canvas.im.image = newImage;
					canvas.im.rasterOFimage = canvas.im.image.getRaster();

					// Zoom ve center değerlerini sıfırlama
					canvas.im.zoomHorrizontal = 1.0f;
					canvas.im.zoomVertical = 1.0f;
					canvas.im.horrizontalC = 0;
					canvas.im.verticalC = 0;

					JOptionPane.showMessageDialog(this, "Image resized successfully!", "Success",
							JOptionPane.INFORMATION_MESSAGE);

				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this, "Please enter valid numbers!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		super.add(resizeButton);
		
		{
			Graphics2D cg = (Graphics2D) canvas.im.image.getGraphics();
			cg.setBackground(Color.white);
			cg.clearRect(0, 0, canvas.im.image.getWidth(), canvas.im.image.getHeight());
		}
	}

//	public Window(String title, GraphicsConfiguration gc) {
//		super(title, gc);
//		// TODO Auto-generated constructor stub
//	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
