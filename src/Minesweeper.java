import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
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
	
	private JPanel eastPanel;
	private JLabel minesLeftLabel;
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
		frame.setBounds(100, 100, 500, 400);
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
		createEastPanel();
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
	
	private void createEastPanel() {
		eastPanel = new JPanel();
		eastPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints top = new GridBagConstraints();
		top.gridwidth = GridBagConstraints.REMAINDER;
		top.insets = new Insets(3, 3, 0, 3);
		top.anchor = GridBagConstraints.NORTH;
		top.fill = GridBagConstraints.HORIZONTAL;
		
		GridBagConstraints filler = new GridBagConstraints();
		filler.gridwidth = GridBagConstraints.REMAINDER;
		filler.insets = new Insets(0, 0, 0, 3);
		filler.weighty = 1;
		
		GridBagConstraints bot = new GridBagConstraints();
		bot.gridwidth = GridBagConstraints.REMAINDER;
		bot.insets = new Insets(0, 3, 3, 3);
		bot.anchor = GridBagConstraints.SOUTH;
		bot.fill = GridBagConstraints.HORIZONTAL;
		
		minesLeftLabel = new JLabel("0 Mines", SwingConstants.CENTER);
		resetButton = new JButton("Reset");
		quitButton = new JButton("Quit");
		
		eastPanel.add(minesLeftLabel, top);
		eastPanel.add(Box.createVerticalGlue(), filler);
		eastPanel.add(resetButton, bot);
		eastPanel.add(quitButton, bot);
		
		frame.add(eastPanel, BorderLayout.EAST);
	}
	
	private void createMargins() {
		EmptyBorder margin = new EmptyBorder(3, 3, 3, 3);
		
		JPanel northPanel = new JPanel();
		JPanel westPanel = new JPanel();
		JPanel southPanel = new JPanel();
		
		marginPanels = new JPanel[] { northPanel, westPanel, southPanel };
		
		for (int i = 0; i < marginPanels.length; i++) {
			marginPanels[i].setBorder(margin);
		}
		
		frame.add(northPanel, BorderLayout.NORTH);
		frame.add(westPanel,  BorderLayout.WEST);
		frame.add(southPanel,  BorderLayout.SOUTH);
	}
}
