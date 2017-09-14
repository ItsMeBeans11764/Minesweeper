import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

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
	
	private int easyGridLength;
	private int mediumGridLength;
	private int hardGridLength;
	private int customGridLength;
	
	private int easyNumberOfMines;
	private int mediumNumberOfMines;
	private int hardNumberOfMines;
	private int customNumberOfMines;
	
	private Map<JButton, Integer> isTileMine; 
	private Map<JButton, Boolean> isTileVisited;
	private int numberOfTiles;
	private int numberOfMines;
	private int numberOfFreeTilesLeft;
	private int difficultyLevel;
	
	private LinkedList<JButton> queue;
	
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
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
		
		easyGridLength = 10;
		mediumGridLength = 25;
		hardGridLength = 50;
		
		easyNumberOfMines = 10;			// 10% mine
		mediumNumberOfMines = 125;		// 20% mine
		hardNumberOfMines = 875;		// 35% mine
		
		
		isTileMine = new HashMap<>();
		isTileVisited = new HashMap<>();
		numberOfMines = easyNumberOfMines;
		difficultyLevel = 1;
		
		queue = new LinkedList<>();
		
		
		// Create initial grid.
		createCenterPanel(easyGridLength, easyGridLength);
		createEastPanel();
		createMargins();
		
		generateMines(numberOfMines);
	}
	
	private void createCenterPanel(int rows, int cols) {
		centerPanel = new JPanel(new GridLayout(rows, cols));
		tileset = new JButton[rows][cols];

		numberOfTiles = rows * cols;
		numberOfFreeTilesLeft = numberOfTiles - numberOfMines;
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				tileset[i][j] = new JButton("-");
				tileset[i][j].setMargin(new Insets(0, 0, 0, 0));
				tileset[i][j].setFocusPainted(false);
				tileset[i][j].setActionCommand(i + " " + j);
				
				// when button on main grid is clicked
				tileset[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JButton button = (JButton)e.getSource();
						button.setEnabled(false);
						button.setText(isTileMine.get(button).toString());
						
						if (Integer.parseInt(button.getText()) < 0) {
							minesLeftLabel.setText("You lost.");
							revealAllMines();
						} else if (Integer.parseInt(button.getText()) == 0) {
							revealAdjacentTiles(button, e.getActionCommand());
						} else if (Integer.parseInt(button.getText()) > 0) {
							revealSafeTile(button);
						}
						
						if (numberOfFreeTilesLeft == 0) {
							minesLeftLabel.setText("You win!");
						}
					}
				});

				isTileMine.put(tileset[i][j], 0);
				isTileVisited.put(tileset[i][j], false);
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
		
		minesLeftLabel = new JLabel(numberOfMines + " Mines", SwingConstants.CENTER);
		
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		
		quitButton = new JButton("Quit");
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit();
			}
		});
		
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

	private void generateMines(int mines) {
		Random random = new Random();
		JButton selectedTile;
		int xMine, yMine, xMine2, yMine2;
		int xLength = tileset[0].length;
		int yLength = tileset.length;
		
		for (int i = 0; i < mines; i++) {
			do {
				xMine = random.nextInt(xLength);
				yMine = random.nextInt(yLength);
				
				selectedTile = tileset[xMine][yMine];
			} while (isTileMine.get(selectedTile) < 0);
			
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					if (x == 0 && y == 0) {
						isTileMine.put(selectedTile, -1);
						System.out.println("Mine at: " + xMine + yMine);
					}
					else {
						xMine2 = xMine + x;
						yMine2 = yMine + y;
						
						if (xMine2 >= 0 && xMine2 < xLength && yMine2 >= 0 && yMine2 < yLength &&
							isTileMine.get(tileset[xMine2][yMine2]) != -1) {
							isTileMine.put(tileset[xMine+x][yMine+y], isTileMine.get(tileset[xMine+x][yMine+y])+1);
						}
					}
				}
			}
		}
	}
	
	private void updateMinesLeft() {
		minesLeftLabel.setText(numberOfMines + " Mines");
	}
	
	private void revealAdjacentTiles(JButton tile, String indexes) {
		if (isTileVisited.get(tile)) {
			return;
		}
		
		String[] dimensions = indexes.split(" ");
		int row, col, xLength, yLength;
		
		row = Integer.parseInt(dimensions[0]);
		col = Integer.parseInt(dimensions[1]);
		xLength = tileset[0].length;
		yLength = tileset.length;
		
		queue.add(tile);
		
		while (!queue.isEmpty()) {
			JButton currentTile = queue.remove();
			revealSafeTile(currentTile);
			isTileVisited.put(currentTile, true);
			
			if (row < 0 || row >= xLength || col < 0 || col >= yLength) {
				return;
			} else {
				if (isTileMine.get(currentTile) == 0) {
					// check surroundings
					if (row - 1 >= 0) {
						revealAdjacentTiles(tileset[row-1][col], row-1, col);
					}
					if (row + 1 < xLength) {
						revealAdjacentTiles(tileset[row+1][col], row+1, col);
					}
					if (col - 1 >= 0) {
						revealAdjacentTiles(tileset[row][col-1], row, col-1);
					}
					if (col + 1 < yLength) {
						revealAdjacentTiles(tileset[row][col+1], row, col+1);
					}
				}
			}
		}
	}
	
	// overloaded helper function
	private void revealAdjacentTiles(JButton tile, int row, int col) {
		revealAdjacentTiles(tile, row + " " + col);
	}
	
	private void revealSafeTile(JButton tile) {
		tile.setEnabled(false);
		tile.setText(isTileMine.get(tile).toString());
		numberOfFreeTilesLeft--;
	}
	
	private void reset() {
		
		switch (difficultyLevel) {
			case 1:
				numberOfMines = easyNumberOfMines;
				break;
			case 2:
				numberOfMines = mediumNumberOfMines;
				break;
			case 3:
				numberOfMines = hardNumberOfMines;
				break;
			case 4:
				numberOfMines = customNumberOfMines;
				break;
		}
		
		numberOfFreeTilesLeft = numberOfTiles - numberOfMines;
		
		updateMinesLeft();
		
		for (JButton tile : isTileMine.keySet()) {
			isTileMine.put(tile, 0);
			
			// reset UI of tile
			tile.setText("-");
			tile.setEnabled(true);
			tile.setBackground(null);
		}
		
		for (JButton tile: isTileVisited.keySet()) {
			isTileVisited.put(tile, false);
		}
		
		generateMines(numberOfMines);
	}
	
	private void quit() {
		frame.dispose();
	}
	
	private void revealAllMines() {
		for (JButton tile : isTileMine.keySet()) {
			tile.setEnabled(false);
			tile.setText(isTileMine.get(tile).toString());
			if (isTileMine.get(tile) == -1) {
				tile.setBackground(Color.RED);
			}
		}
	}
}
