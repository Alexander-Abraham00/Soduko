

public class Board implements SudokuSolver{

	private int[][] startBoard;
	private int[][] originalBoard;
	private static final int EMPTY = 0;
	private static final int SIZE = 9;
	
	public Board() {	
		//vill kanske ha möjlighet att välja boardSize??
	}
	
	/**
     * Initializes the board with values in the matrix start.
     */
    public void init(int[][] start) {
    	startBoard = start;
    	originalBoard = start;
    	
    }
	
    /**
     * Check if the position at row, col has a start value
     * @return true if the position has no start value, false otherwise
     */
    public boolean checkIfEmpty(int row, int col) {
    	if(startBoard[row][col] == EMPTY) {
    		return true;
    	}
    	return false;
    }
    
    // we check if a possible number is already in a row
 	private boolean isInRow(int row, int number) {
 		for (int i = 0; i < SIZE; i++)
 			if (startBoard[row][i] == number) {
 				return true;
 			}
 				
 		return false;
 	}
 	
 	// we check if a possible number is already in a column
 	private boolean isInCol(int col, int number) {
 		for (int i = 0; i < SIZE; i++)
 			if (startBoard[i][col] == number) {
 				return true;
 			}
 		return false;
 	}
 	
 	// we check if a possible number is in its 3x3 box
 	private boolean isInBox(int row, int col, int number) {
 		int r = row - row % 3;
 		int c = col - col % 3;
 		
 		for (int i = r; i < r + 3; i++)
 			for (int j = c; j < c + 3; j++)
 				if (startBoard[i][j] == number) {
 					return true;
 				}	
 		return false;
 	}
 	
	/**
     * Check if it is legal to place value at row, col.
     * @return true if value can be placed at row, col, false otherwise
     */
    public boolean checkIfLegal(int row, int col, int value) {
    	if(checkIfEmpty(row, col)) {
    		return !isInRow(row, value)  &&  !isInCol(col, value)  &&  !isInBox(row, col, value);
    	}
    	return false;	
    }

    /**
     * Returns the solution.
     * @return int matrix with a valid solution
     */
    public int[][] getBoard(){
    	return startBoard;
    }
    
    public int[][] getOriginalBoard(){
    	return originalBoard;
    }
    
    /**
     * Method to solve the sudoku.
     * @return true if solution was found, false otherwise
     */
    public boolean solve(int row, int col) {//solve(0, 0) 
    	
    		// Går genom brädet
            for (int i = 0; i < SIZE; i++) {
            	for (int j = 0; j < SIZE; j++) {
            		
            		//om platsen är tom
            		if(checkIfEmpty(i, j)) {
            			
            			//provar sig igenom alla nummer 1-9
            			for (int number = 1; number <= SIZE; number++) {
            				
            				//kollar om den får sätta in number på platsen
                			if (checkIfLegal(i, j, number)) {
                				add(i, j, number);
                				System.out.println("hittade lösningen: " + number + " för rad: " + i + " kolumn: " + j);
                				
                				//om det är löst returnera true
                				if(solve(row, col)) {
                					return true;
                				}else {//annars rensa platsen 
                					remove(row, col);
                				}
                			}
                		}
            			System.out.println("hittade ej lösning på rad: " + i + " kolumn: " + j);
            			return false;
            		}
            	}
             }
            return true; //löst sudokut
    	
    }

    /**
     * Adds value value at position row, col.
     */
    public void add(int row, int col, int value) {
    	startBoard[row][col] = value;
    }
   
    /**
     * Clears the sudoku.
     * Sätter allt till startboard
     */
    public boolean clear() {
    	if(startBoard == null) {
    		return false;
    	}else {
    		//går genom hela brädet och sätter det till originalbrädet
    		for(int i = 0; i < SIZE; i++) {
    			for(int j = 0; j < SIZE; j++) {
        			startBoard[i][j] = originalBoard[i][j];
        			System.out.println("Platsen: " + i + " " + j + " Sätts till " + originalBoard[i][j]);
        			
        			if(startBoard[i][j] != originalBoard[i][j]) {
        				System.out.println("Gick ej att rensa " + i + " " + j);
        				return false;
        			}
        		}
    		}
    		return true;
    	}
    }
    
    /**
     * Removes the value at row, col.
     */
    public void remove(int row, int col) {
    	startBoard[row][col] = EMPTY;
    }	
}
