import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class Minesweeper {

	private JFrame frame;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem newMenuItem;
	private JMenu difficultyMenu;
	private JMenuItem easyMenuItem;			// 10 * 10 tiles
	private JMenuItem mediumMenuItem;		// 25 * 25 tiles.
	private JMenuItem hardMenuItem;			// 100 * 100 tiles.
	private JMenuItem customMenuItem;
	private JMenuItem exitMenuItem;
	private JMenu helpMenu;
	private JMenuItem instructionMenuItem;
	private JMenuItem aboutMenuItem;

	private JPanel centerPanel;
	private JButton[][] tileset;
	
	private JPanel southPanel;
	private JButton resetButton;
	private JButton quitButton;
	
	private JPanel[] marginPanels;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Minesweeper window = new Minesweeper();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Minesweeper() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Create window frame.
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Create menu bar.
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		// Create 'file' menu for the menu bar.
		fileMenu = new JMenu("File");
		newMenuItem = new JMenuItem("New");
		difficultyMenu = new JMenu("Difficulty");
		easyMenuItem = new JMenuItem("Easy");
		mediumMenuItem = new JMenuItem("Medium");
		hardMenuItem = new JMenuItem("Hard");
		customMenuItem = new JMenuItem("Custom");
		exitMenuItem = new JMenuItem("Exit");
		
		difficultyMenu.add(easyMenuItem);
		difficultyMenu.add(mediumMenuItem);
		difficultyMenu.add(hardMenuItem);
		difficultyMenu.add(customMenuItem);
		
		menuBar.add(fileMenu);
		fileMenu.add(newMenuItem);
		fileMenu.add(difficultyMenu);
		fileMenu.add(exitMenuItem);
		// Create 'help' menu for the menu bar.
		helpMenu = new JMenu("Help");
		instructionMenuItem = new JMenuItem("How to Play");
		aboutMenuItem = new JMenuItem("About");
		
		menuBar.add(helpMenu);
		helpMenu.add(instructionMenuItem);
		helpMenu.add(aboutMenuItem);
		
		// Create initial grid.
		createCenterPanel(10, 10);
		createSouthPanel();
		createMargins();
	}
	
	private void createCenterPanel(int rows, int cols) {
		centerPanel = new JPanel(new GridLayout(rows, cols));
		tileset = new JButton[rows][cols];
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				tileset[i][j] = new JButton("-");
				centerPanel.add(tileset[i][j]);
			}
		}
		
		frame.add(centerPanel, BorderLayout.CENTER);
	}
	
	private void createSouthPanel() {
		southPanel = new JPanel();
		
		resetButton = new JButton("Reset");
		quitButton = new JButton("Quit");
		
		southPanel.add(resetButton);
		southPanel.add(quitButton);
		
		frame.add(southPanel, BorderLayout.SOUTH);
	}
	
	private void createMargins() {
		EmptyBorder margin = new EmptyBorder(3, 3, 3, 3);
		
		JPanel northPanel = new JPanel();
		JPanel westPanel = new JPanel();
		JPanel eastPanel = new JPanel();
		
		marginPanels = new JPanel[] { northPanel, westPanel, eastPanel };
		
		for (int i = 0; i < marginPanels.length; i++) {
			marginPanels[i].setBorder(margin);
		}
		
		frame.add(northPanel, BorderLayout.NORTH);
		frame.add(westPanel,  BorderLayout.WEST);
		frame.add(eastPanel,  BorderLayout.EAST);
	}
}
