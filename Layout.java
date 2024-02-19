
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Layout class contains the main UI for connect 4 game.
 * @author Kanwardeep Kaur
 *
 */
public class Layout extends JFrame{

	public static void main(String[] args) {
		new Layout();
	}
	/**
	 * Game board where each element indicates the state of circle
	 * empty circle = 0
	 * circle filled by player 1 = 1
	 * circle filled by player 2 = 2
	 */
	private int[][] circle; 
	/**
	 * Number of rows on game board
	 */
	private int rows = 6; 
	/**
	 * Number of columns on game board
	 */
	private int col = 7; 
	/**
	 * The current player in game
	 * player 1 = 1, player 2 = 2
	 */
	private int currentPlayer = 1;
	/**
	 * Size of the circles
	 */
	private int size = 45; 
	/**
	 * Gap between circles
	 */
	private int gap = 10; 
	
	/**
	 * Constructs Layout frame
	 * initializes UI components and displays frame
	 */
	public Layout() {	
		
		setTitle("CONNECT 4");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		components();
		
		pack();
		setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
	}
	
	/**
	 * Constructs the main panel containing game components.
	 */
	private void components() {
        JPanel mainPanel = new JPanel(new BorderLayout());//divides screen
        mainPanel.setPreferredSize(new Dimension(800,500));
        
        //Horizontal JSplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);//initial location divider
        splitPane.setEnabled(false);//can't be resized
        
        splitPane.add(createLeftPanel());
        splitPane.add(createRightPanel());
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        
        add(mainPanel, BorderLayout.CENTER);
        
        setJMenuBar(createMenuBar());
    }
	
	/**
	 * Creates left panel containing game board circles and "CONNECT 4" heading.
	 * @return left The left panel containing the game board. 
	 */
	private JPanel createLeftPanel() {
		JPanel left = new JPanel(new BorderLayout()); // Use BorderLayout for left panel
	    left.setBackground(new Color(211, 211, 211));//light gray, RGB components
		JLabel label = new JLabel("CONNECT 4", SwingConstants.CENTER); // Create a label
	    label.setFont(new Font("Arial", Font.BOLD, 50));
	    label.setPreferredSize(new Dimension(450, 50)); // Set label size
	    left.add(label, BorderLayout.NORTH); // Add label to the top of the panel

	    
	    circle = new int[rows][col];
	    
		JPanel circlesPanel = new JPanel() {
			
			//method in Graphics class to paint components
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);//to ensure the component is painted correctly
                setBackground(new Color(211, 211, 211));//light gray color
//              
                
                
                /*
                 * Calculating the starting x-position(horizontal centering) for drawing circles by subtracting
                 * the total width of circles and gap from panel width then dividing b 2.
                 */
                int xStart = (getWidth() - (size * col + gap * (col - 1))) / 2; // Calculate starting x position
                /*
                 * Calculating the starting y-position(vertical centering) for drawing the circle by subtracting
                 * the total height of circles and gap from panel height then dividing b 2.
                 */
                int yStart = (getHeight() - (size * rows + gap * (rows - 1))) / 2; // Calculate starting y position
                //draws circles row by row
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < col; j++) {
                        int x = xStart + j * (size + gap);
                        int y = yStart + i * (size + gap);
                        
                       
                        if (circle[i][j] == 1) {
                            g.setColor(new Color(51, 67, 75)); // navy color for player 1
                        }else if (circle[i][j] == 2) {
                            g.setColor(new Color(110, 112, 117)); // dark grey color for player 2
                        }else {
                            g.setColor(Color.WHITE); // Set default color for circles
                        }
                        g.fillOval(x, y, size, size); // Draw circle
                    }
                }
            }
        };
		
        circlesPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {//handles mouse clicks in circlesPanel
                
                int xStart = (circlesPanel.getWidth() - (size * 7 + gap * (7 - 1))) / 2; // Calculate the starting x-position 
                int col = (e.getX() - xStart) / (size + gap);
                int row = getAvailableRow(col); // Find the available row in the column

                if (row != -1) { // If column is not full
                    circle[row][col] = currentPlayer; // Place the player's piece
                    changePlayer(); // Switch to the next player
                    circlesPanel.repaint(); // Repaint the panel to reflect the changes
                }
            }
        });
        
        left.add(circlesPanel, BorderLayout.CENTER); // Add circles panel to the center of the left panel
        return left;
	}
	
	/**
	 * Switches the current player to the other player to take turns.
	 * If the current player is 1, it changes to player 2, and vice versa.
	 */
	private void changePlayer() {
		if (currentPlayer == 1) {
	        currentPlayer = 2;
	    } else {
	        currentPlayer = 1;
	    }
	}
	
	/**
	 * Finds the available row in the specified column where a game piece can be placed.
	 * 
	 * @param col The column index where the availability of the row is to be checked.
	 * @return The index of the available row in the specified column, or -1 if the column is full.
	 */
	private int getAvailableRow(int col) {
	    for (int i = 5; i >= 0; i--) {
	        if (circle[i][col] == 0) {
	            return i;
	        }
	    }
	    return -1; // Column is full
	}
	
	/**
	 * Creates the right panel containing the game information panel and the chat panel.
	 * @return rightPanel The JPanel containing the game information panel and the chat panel.
	 */
	private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new GridLayout(2, 1));
        rightPanel.add(createGameInfoPanel());
        rightPanel.add(createChatPanel());
        return rightPanel;
    }
	
	/**
	 * Creates game info panel, the upper side on the right
	 * @return gameInfo The game info panel.
	 */
	private JPanel createGameInfoPanel() {
		 //use boxlayout
        JPanel gameInfo = new JPanel(new BorderLayout());
        gameInfo.setBackground(new Color(40,50,60)); // navy
        
        JPanel heading = new JPanel();
        heading.setBackground(new Color(40,50,60)); // navy
        
        JLabel info = new JLabel("GAME INFO",SwingConstants.CENTER);//aligning in center
        info.setFont(new Font("Calibri", Font.BOLD, 18));//editing label's text
        info.setForeground(Color.WHITE);
        heading.add(info);
        gameInfo.add(heading,BorderLayout.NORTH);
        
        JPanel players = new JPanel(); 
        players.setBackground(new Color(40,50,60)); // navy
        players.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5)); // center alignment, no horizontal gap, and 20 pixels vertical gap
   

        JLabel moveInfoLabel = new JLabel("Click on a column to make a move.",SwingConstants.CENTER);
        moveInfoLabel.setFont(new Font("Calibri",Font.ITALIC,14));
        moveInfoLabel.setForeground(Color.WHITE);
        players.add(moveInfoLabel);
        //player 1
        JLabel player1 = new JLabel("Player 1: Kanwar",SwingConstants.CENTER);
        player1.setFont(new Font("Calibri",Font.ITALIC,14));
        player1.setForeground(Color.WHITE);
        players.add(player1);
        //player 2
        JLabel player2 = new JLabel("Player 2: Tanvi",SwingConstants.CENTER);
        player2.setFont(new Font("Calibri",Font.ITALIC,14));
        player2.setForeground(Color.WHITE);
        players.add(player2);
        
        JLabel timer = new JLabel("Timer: 35 sec",SwingConstants.CENTER);
        timer.setFont(new Font("Calibri",Font.ITALIC,14));
        timer.setForeground(Color.WHITE);
        players.add(timer);
        
        JLabel gameTimer = new JLabel("Game Timer: 1 min 3 sec",SwingConstants.CENTER);
        gameTimer.setFont(new Font("Calibri",Font.ITALIC,14));
        gameTimer.setForeground(Color.WHITE);
        players.add(gameTimer);
        
        
        //moves 
        
        JPanel playerMovesPanel = new JPanel(new BorderLayout());
        playerMovesPanel.setBackground(new Color(40, 50, 60)); // navy

        // Create and configure the heading label
        JLabel movesHeading = new JLabel("MOVES", SwingConstants.CENTER);
        movesHeading.setFont(new Font("Calibri", Font.BOLD, 16));
        movesHeading.setForeground(Color.WHITE);
        playerMovesPanel.add(movesHeading, BorderLayout.NORTH);

        //text pane for player moves
        JTextPane movesTextPane = new JTextPane();
        movesTextPane.setBackground(new Color(40, 50, 60)); // navy
        movesTextPane.setFont(new Font("Calibri", Font.ITALIC, 14));
        movesTextPane.setForeground(Color.WHITE);
        movesTextPane.setEditable(false);

        
        // Set the text for player moves
        String movesText = "Player 1 played column 1.\nPlayer 2 played column 2.";
        movesTextPane.setText(movesText);

        // Add the text pane to the panel with a scroll pane
        JScrollPane scrollPane = new JScrollPane(movesTextPane);
        scrollPane.setPreferredSize(new Dimension(200, 130)); // Set preferred size for the scroll pane
        playerMovesPanel.add(scrollPane, BorderLayout.CENTER);
        // Set preferred size for the panel
        playerMovesPanel.setPreferredSize(new Dimension(200, 130));

        
        gameInfo.add(playerMovesPanel, BorderLayout.SOUTH);   
        
        
        
        gameInfo.add(players, BorderLayout.CENTER);
        

        return gameInfo;
	}
	
	/**
	 * Creates chat history panel, the lower side on the right
	 * @return chat The chat panel.
	 */
	private JPanel createChatPanel() {
		JPanel chat = new JPanel(new BorderLayout());
        chat.setBackground(new Color(40,50,60));
        //label
        
        // Panel for label
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBackground(new Color(40, 50, 60));
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(Color.WHITE); // Set color to white
        labelPanel.add(separator, BorderLayout.NORTH);
        //labelPanel.setPreferredSize(new Dimension(100, 30)); // Set preferred size for the label area
        JLabel chatLabel = new JLabel("CHAT HISTORY", SwingConstants.CENTER);
        chatLabel.setFont(new Font("Calibri", Font.BOLD, 18));
        chatLabel.setForeground(Color.WHITE);
        labelPanel.add(chatLabel, BorderLayout.CENTER);
        chat.add(labelPanel, BorderLayout.NORTH);

        // Text area for chat history
        JTextPane chatHistory = new JTextPane();
        chatHistory.setEditable(false);
        chatHistory.setFont(new Font("Calibri", Font.BOLD, 18));
        chatHistory.setBackground(new Color(40, 50, 60));
        chatHistory.setForeground(Color.WHITE);
        chatHistory.setOpaque(true);
        
        //scroll pane        
        JScrollPane scroll = new JScrollPane(chatHistory);
        chat.add(scroll,BorderLayout.CENTER);
        chat.add(createBottomPanel(),BorderLayout.SOUTH);
        // Add messages to the chat with different alignments
        addMessage(chatHistory, "Hello!", true); // Right side
        addMessage(chatHistory, "Hi there!", false); // Left side
        
        /*scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Ensure vertical scrollbar is always visible
    	   scroll.setBorder(null); // Remove border to ensure a cleaner appearance
    	*/
        return chat;
	}
	
	/**
	 * Adds a message to the chat history pane with specified alignment.
	 * 
	 * @param chatHistory The JTextPane representing the chat history.
	 * @param message The message to be added to the chat history.
	 * @param alignRight A boolean indicating whether to align the message to the right (true) or left (false).
	 */
	//aligns the text left and right
	private void addMessage(JTextPane chatHistory, String message, boolean alignRight) {
        StyledDocument doc = chatHistory.getStyledDocument();
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, Color.WHITE);
        StyleConstants.setFontFamily(set, "Calibri");
        StyleConstants.setFontSize(set, 16);

        int length = doc.getLength();
        try {
            doc.insertString(length, message + "\n", set);
            StyleConstants.setAlignment(set, alignRight ? StyleConstants.ALIGN_RIGHT : StyleConstants.ALIGN_LEFT);
            doc.setParagraphAttributes(length, 1, set, false);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * Creates a panel containing an input field and a send button for the bottom section.
	 * @return bottom The JPanel containing the input field and send button.
	 */
	private JPanel createBottomPanel() {
		 	JPanel bottom = new JPanel(new BorderLayout());
	        JTextField input = new JTextField();
	        JButton send = new JButton("SEND");
	        bottom.add(input,BorderLayout.CENTER);
	        bottom.add(send,BorderLayout.EAST);
	        return bottom;
	}
	
	/**
	 * Creates the menu bar.
	 * @return top The menu bar.
	 */
	private JMenuBar createMenuBar() {
		 //menu bar
    	JMenuBar top = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu game = new JMenu("Game");
		JMenu network = new JMenu("Network");
		JMenu language = new JMenu("Language");
		JMenu help = new JMenu("Help");
		
		//use mnemonic (alt + f for file)
		file.setMnemonic('F');
		
		
		
		top.add(file);
		top.add(game);
		top.add(network);
		top.add(language);
		top.add(help);
		
		//File sub-menu
		JMenuItem newItem = new JMenuItem("New");
		newItem.setMnemonic('N');
		JMenuItem loadItem = new JMenuItem("Load");
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.setMnemonic('S');
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('X');
		
		
		file.add(newItem);
		file.addSeparator();
		file.add(loadItem);
		file.addSeparator();
		file.add(saveItem);
		file.addSeparator();
		file.add(exitItem);
		
		//Game sub-menu
		JMenuItem sound = new JMenuItem("Sound");
		JMenuItem color = new JMenuItem("Color");
		game.add(sound);
		game.addSeparator();
		game.add(color);
		
		//Network sub-menu
		JMenuItem host = new JMenuItem("Host");
		JMenuItem connect = new JMenuItem("Connect");
		JMenuItem disconnect = new JMenuItem("Disconnect");
		network.add(host);
		network.addSeparator();
		network.add(connect);
		network.addSeparator();
		network.add(disconnect);
		
		JMenuItem english = new JMenuItem("English");
		JMenuItem french = new JMenuItem("French");
		language.add(english);
		language.addSeparator();
		language.add(french);
		
		JMenuItem rules = new JMenuItem("Rules");
		JMenuItem about = new JMenuItem("About");
		help.add(rules);
		help.addSeparator();
		help.add(about);
		
		
		return top;
	}

}
