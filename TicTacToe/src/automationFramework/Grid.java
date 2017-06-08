package automationFramework;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Grid {
	
	protected HashMap<Integer, String> xPath = new HashMap<Integer, String>();
	protected double[] board = new double[Config.BOARDSIZE];
	
	public Grid(){
		//Set up our xPaths, note they go from tile 1 - 9
		for(int i=1; i<=Config.BOARDSIZE; i++){
			this.xPath.put(i, "/html/body/div[3]/div[1]/div[" + i + "]");
		}
		
		//Setup our board with all 0s, empty tiles
		for(int i=0; i<Config.BOARDSIZE; i++){
			this.board[i] = 0.5;
		}
	}
	
	protected void getBoard(WebElement board){		
		for(int i=1; i<=Config.BOARDSIZE; i++){
			int trueIndex = i-1;
			
			//If there is a tile already placed continue, we don't need to check twice
			if(this.board[trueIndex] == Config.PLAYER_TILE || this.board[trueIndex] == Config.CPU_TILE) continue;
			
			//Try to get a tile, if it doesn't work then we don't have anything placed there
			try{
				WebElement tile = board.findElement(By.xpath(this.xPath.get(i)));
				tile.findElement(By.className("o"));
				this.board[trueIndex] = Config.CPU_TILE;
				//System.out.println("CPU played tile on: " + i);
			}catch(Exception e){
				//System.out.println(e);
				//System.out.println("No tile placed on tile: " + i);
			}
		}
	}
	
	protected void changeBoard(int index, double playerTile){
		this.board[index] = playerTile;
	}
	
	protected String getTileToClick(int index){
		index++; //move up an index because the tiles are stored 1-9, board goes 0-8
		return(this.xPath.get(index));
	}
	
	protected void clearBoard(){
		for(int i=0; i<Config.BOARDSIZE; i++){
			this.board[i] = Config.EMPTY_TILE;
		}
	}
}
