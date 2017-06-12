package automationFramework;

import java.util.ArrayList;

public class Grid {
	
	protected int[][] board = new int[Config.ROW_SIZE][Config.COL_SIZE];
	
	/*
	 * Constructor:
	 * Takes a 1D array converts it to a 2d array for easy solving
	 * @param ArrayList<Integer> board - The parsed 1D array of the board
	 */
	public Grid(ArrayList<Integer> board){
		for(int i=0; i<Config.ROW_SIZE; i++){
			for(int j=0; j<Config.COL_SIZE; j++){
				this.board[i][j] = board.get((i * Config.ROW_SIZE) + j);
			}
		}
	}
	
	/*
	 * This method uses the backtracking algorithm to solve the puzzle
	 * @param int i - the i'th row to solve for
	 * @param int j - the j'th row to solve for
	 * @param int[][] board - the current board we are solving for
	 * @return boolean - if we found a solution for the board or not
	 */
	protected boolean solveSudoku(int i, int j, int[][] board){
		if(i==9){
			i=0;
			if(++j == 9){
				return(true);
			}
		}
		
		if(board[i][j] != 0) //Skip filled cells
			return(solveSudoku(i+1,j,board));
		
		for(int value = 1; value <= 9; value++){
			if(legalMove(i,j,value,board)){
				board[i][j] = value;
				if(solveSudoku(i+1,j,board))
					return(true);
			}
		}
		
		board[i][j] = 0; //Bad solution => reset backtrack
		return(false); //no solution
	}
	
	/*
	 * This method checks to see if we have a valid move (IE only one distinct digit per row/column/3x3 block)
	 * @param int i - The i'th row we are currently on
	 * @param int j - The j'th column we are currently on
	 * @param int[][] board - The current board we are checking
	 * @return boolean - If we can make the move or not
	 */
	protected boolean legalMove(int i, int j, int value, int[][] board){
		//Check the rows
		for(int k=0; k<9; k++){ 
			if(value == board[k][j]){
				return(false);
			}
		}
		
		//Check the columns
		for(int k=0; k<9; k++){ 
			if(value == board[i][k]){
				return(false);
			}
		}
		
		//Check the 3x3 blocks
		int rowOffSet = (i / 3) * 3;
		int colOffSet = (j / 3) * 3;
		for(int k=0; k< 3; k++){
			for(int m=0; m<3; m++){
				if(value == board[rowOffSet+k][colOffSet+m]){
					return(false);
				}
			}
		}
		//All tests passed => valid move
		return(true);
	}
}
