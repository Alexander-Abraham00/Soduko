import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI {
	
	/*Saker som inte funkar:
	 * clear rensar och lägger in nytt bräde med siffrorna man skrivit själv
	 * läser inte in rätt värden från filerna. Lägger bara in nollor
	 * solve funkar inte. Hittar aldrig någon lösning
	 * kan ej kolla om användaren skrivit in rätt lösning själv eftersom solve metoden inte funkar korrekt
	 */

	private int gridSize = 9;
	private int margin_top = 30;
	private int margin_bottom = 30;
	private int margin_left = 30;
	private int margin_right = 30;
	
	private JFrame frame;
	
	private JButton button_solve;
	private JButton button_checkIfRight;
	private JButton button_clear;
	private JButton button_exit;
	private JButton button_newSudoku;
	
	private JPanel info_panel;
	private JPanel board_top;
	private JPanel pre_board;
	
	private int[][] boardToUse;
	private int[][] originalBoard;
	private float frameX;
	private float frameY;
	
	private Board tempBoard;
	private JMenuBar menyBar;
	private JMenu meny;
	private JMenuItem menyItem;
	private ArrayList <Board> boardMeny;
	
	public GUI() {
		
		boardMeny = new ArrayList <Board> ();
		frame = new JFrame();
		button_solve = new JButton("Solve");
		button_clear = new JButton("Clear board");
		info_panel = new JPanel();
		board_top = new JPanel();
		pre_board = new JPanel();
		button_checkIfRight = new JButton("Check if right");
		button_exit = new JButton("Exit");
		button_newSudoku = new JButton("New Sudoku");
		
		
		
		//Sätter upp ytan i fönstret där knapp och räknare kommer vara
		info_panel.setBorder(BorderFactory.createEmptyBorder(margin_top, margin_bottom, margin_left, margin_right));
		info_panel.setLayout(new GridLayout(0, 2, 10, 10));
		info_panel.add(button_solve);
		info_panel.add(button_clear);
		info_panel.add(button_checkIfRight);
		info_panel.add(button_exit);
		info_panel.add(button_newSudoku);
		
		
		
		//Sätter upp ytan i fönstret där sodukobrädet kommer vara
		board_top.setBorder(BorderFactory.createEmptyBorder(margin_top, margin_bottom, margin_left, margin_right));
		board_top.setLayout(new GridLayout(gridSize, gridSize, 1, 1));
		board_top.setBackground(Color.black);
		board_top.setVisible(true);
		
		
		//Sätter upp fönstret där allt kommer synas
		frame.add(info_panel, BorderLayout.SOUTH);
		frame.add(board_top);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Sudoku Solver");
		frame.pack();
		frame.setVisible(true);
		frame.setSize(600, 700);
		frameX = (float)frame.getSize().getWidth();
		frameY = (float)frame.getSize().getHeight();
		//frame.setResizable(false);
		
		//Meny för att läsa från fil
		menyBar = new JMenuBar();
		meny =  new JMenu("File");
		menyItem = new JMenuItem("Load file");
		frame.add(menyBar, BorderLayout.NORTH);
		menyBar.add(meny);
		meny.add(menyItem);
		
		button_exit.addActionListener(exit -> {
			System.exit(0);
		});
		
		button_newSudoku.addActionListener(newSoduko -> {
		
			//ifall listan med bräden är tom
			if(boardMeny.size() == 0) {
				JOptionPane.showMessageDialog(frame,"Finns inga bräden i listan", "List is empty", JOptionPane.WARNING_MESSAGE);	
			}else if(boardMeny.size() == 1){
				JOptionPane.showMessageDialog(frame,"Finns bara ett bräde i listan", "Add more boards", JOptionPane.WARNING_MESSAGE);
			}else {
				Random rand = new Random();
				int nextBoard = rand.nextInt(boardMeny.size());
				
				//ska kolla om objektet i listan boardMeny med index nextBoard är samma som det som används (tempBoard)
				while(boardMeny.get(nextBoard).getBoard().equals(tempBoard.getBoard())) {
					nextBoard = rand.nextInt(boardMeny.size());
				}
				System.out.println("Använder sudoku: " + nextBoard + " i listan");
				
				//när vi hittat ett bräde som inte redan används lägger vi in det brädet i vår GUI
				clearGUI();
				Board newBoard = new Board();
				newBoard.init(boardMeny.get(nextBoard).getBoard());
				insertBoard(newBoard);
				
				
			}
			System.out.println("Antal sudoku i listan: " + boardMeny.size());
		});
		
		button_checkIfRight.addActionListener(check -> {
			
			Board checkAnswer = new Board();
			checkAnswer.init(originalBoard);
			
			//om en lösning finns
			if(checkAnswer.solve(0, 0)) {
				
				//går genom listan och kollar om alla rutor har siffror
				for(int i = 1; i <= 9; i++) {
					for(int j = 1; j <= 9; j++) {
						
						//om platsen har en siffra
						if(!tempBoard.checkIfEmpty(i, j)) {
							
							//om siffran är samma som lösningen (boardToUse är den som används i tempBoard)
							if(originalBoard[i][j] == tempBoard.getBoard()[i][j]) {
								
								//om vi kommer hela vägen till ruta 81 och alla är samma har vi hittat rätt lösning
								if(i == 9 && j == 9) {
									JOptionPane.showMessageDialog(frame,"Korrekt lösning! :)", "Sudoku solved", JOptionPane.PLAIN_MESSAGE);
								}
							}
						}else { //om någon ruta är tom kommer en warning
							JOptionPane.showMessageDialog(frame,"Alla rutor är ej ifyllda", "Sudoku no solv :(", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
				//gått genom hela brädet utan warning = fel lösning
				JOptionPane.showMessageDialog(frame,"Felaktig lösning", "Sudoku no solv :(", JOptionPane.WARNING_MESSAGE);	
			}
			JOptionPane.showMessageDialog(frame,"Sudokut saknar lösning :(", "No work :(", JOptionPane.ERROR_MESSAGE);
		});
		
		button_solve.addActionListener(Solve -> {
			if(tempBoard.solve(0, 0)) {
				insertBoard(tempBoard);
			}else {
				JOptionPane.showMessageDialog(frame,"Sudokut gick ej att lösa :(", "No work :(", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		//sätter brädet till det man skrivit in??
		//ska sätta brädet till originalsiffrorna (gråa rutorna)
		button_clear.addActionListener(clear -> {
			if(!tempBoard.clear()) {
				JOptionPane.showMessageDialog(frame,"Kan ej rensa brädet", "No work :(", JOptionPane.WARNING_MESSAGE);
			}else {
				clearGUI();
				insertBoard(tempBoard);
			}
		});
		
		menyItem.addActionListener(loadFile -> {
			frame.remove(pre_board);
			JFileChooser fileToUse = new JFileChooser("/Users/alle/Documents/edaa30-workspace/SudokuSolver/src/SudokuBoards");
			
			int boii = fileToUse.showOpenDialog(frame);

	        if (boii == JFileChooser.APPROVE_OPTION) {
	            File file = fileToUse.getSelectedFile();
	            readSudokuFromFile(file);
	            
	        }
		});
	}
	
	public void clearGUI() {
		board_top.removeAll();
	}
	
	//lägger till board b i listan boardMeny
	public void addBoardToList(Board b) {
		Board boardToAdd = new Board();
		boardToAdd.init(originalBoard);
		this.boardMeny.add(boardToAdd);
	}
	
	public void readSudokuFromFile(File f) {
		try {
        	Board boardFromFile = new Board();
			int [][] arrayFromFile = new int[9][9];
			boardFromFile.init(arrayFromFile);
        	Scanner s = new Scanner(f);
        	
        	//sålänge det finns fler rader att läsa av
        	while(s.hasNextLine()) {
        		String line = s.nextLine();
            	for(String str: line.split(" ")) {
            		int nbr = Integer.parseInt(str);
            		for(int row = 0; row < 9; row ++) {
            			for(int col = 0; col < 9; col ++) {
            				boardFromFile.add(row, col, nbr);
            			}
            		}
            	}	
        	}
        	
        	s.close();
        	clearGUI();
            insertBoard(boardFromFile);
            
            //bara för att se vad som läses in
            for(int i = 0; i < 9; i++) {
            	for(int j = 0; j < 9; j++) {
            	System.out.print(" " + arrayFromFile[i][j]);
	            }
            	System.out.println();
            }
            System.out.println();
            addBoardToList(boardFromFile);
        }catch (FileNotFoundException e){
        	System.out.println("Could not open file: " + f);
        	System.exit(1);
        }
	}
	
	//Visar ett valt sudokubräde i GUIn 
	public void insertBoard(Board b) {
		
		this.boardToUse = b.getBoard();
		this.originalBoard = b.getOriginalBoard();
		int[][] temp = boardToUse;
		this.tempBoard = new Board();
		this.tempBoard.init(temp);
		String nbr;
		
		//Går genom fönstrets grid och lägger till bräde
		for(int i = 0; i < 9; i++) { //row
			for(int j = 0; j < 9; j++) { //col
				
				nbr = Integer.toString(temp[i][j]);
				if(temp[i][j] == 0) {
					int indexI = i;
					int indexJ = j;
					JPanel grid = new JPanel();
					grid.setBackground(Color.white);
					JTextField number = new JTextField("", 1);
					number.setPreferredSize(new Dimension(50, 50));
					number.setAlignmentX((frameX - margin_left - margin_right)/(gridSize/2) );
					number.setAlignmentY((frameY - margin_top - margin_bottom)/(gridSize/2) );
					number.setBorder(null);
					grid.add(number);
					board_top.add(grid);
					
					//action listener till när man skriver in ett värde i sudokut
					number.addActionListener(textField -> {
						String nbrToUse = number.getText();
						int changedNumber = Integer.parseInt(nbrToUse);
						if(changedNumber > 9) {
							JOptionPane.showMessageDialog(frame,"Siffran får inte vara mer än 9", "Aj Aj Aj!!", JOptionPane.ERROR_MESSAGE);
						}else {
							tempBoard.add(indexI, indexJ, changedNumber);
							JTextField newNumber = new JTextField(nbrToUse, 1);
							grid.add(newNumber);
							board_top.add(grid);
							board_top.repaint();
							frame.repaint();
							System.out.println(changedNumber + Integer.toString(indexI) + Integer.toString(indexJ));	
						}
					});
					
				}else {
					JPanel grid = new JPanel();
					grid.setBackground(Color.LIGHT_GRAY);
					JTextField number = new JTextField(nbr, 1);
					number.setEditable(false);
					number.setBackground(Color.LIGHT_GRAY);
					number.setPreferredSize(new Dimension(50, 50));
					number.setBorder(null);
					grid.add(number);
					board_top.add(grid);
				}	
			}	
		}
		board_top.repaint();
		frame.repaint();
	}
}
