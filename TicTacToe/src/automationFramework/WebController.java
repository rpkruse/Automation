package automationFramework;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebController {
	
	private Grid grid = new Grid();
	private Player player;
	
	private WebDriver driver;
	private WebElement boardElement;
	private WebElement restartElement;
	
	private String URL;
	
	private int turn = 0; //%2==0 means we are doing the first turn
	
	/*
	 * Save our URL and load AdBlock into our ChromeDriver
	 */
	protected WebController(String URL){
		ChromeOptions options = new ChromeOptions();
		options.addExtensions(new File("AdBlock_v3.10.0.crx")); //DEV ONLY
		//options.addExtensions(new File("Ad_Block/AdBlock_v3.10.0.crx"));
		this.driver = new ChromeDriver(options);
		//this.driver = new ChromeDriver();
		this.URL = URL;
	}
	
	protected void loadHomePage(Player player){
		try{
			this.driver.get(this.URL);
			this.boardElement = this.driver.findElement(By.xpath("/html/body/div[3]/div[1]"));
			this.restartElement = this.driver.findElement(By.xpath("/html/body/div[3]/div[2]"));
			//this.driver.findElement(By.className("mute")).click(); //mute the stupid game
			
			this.player = player;
			this.grid.clearBoard();
		}catch(Exception e){
			System.out.println("Not a valid page: " + this.URL + "\nClosing program");
			this.closeDriver();
		}
	}
	
	protected void checkPlay() throws InterruptedException{
		for(int i=0; i<Config.NUMOFGAMES; i++){

			while(!this.restartElement.isDisplayed()){
				int playersPlay;
				
				//We start the first round of this game
				if(this.turn % 2 == 0){
					//playersPlay = player.playRandomly(this.grid);
					playersPlay = player.playFromBrain(this.grid);
				}else{
					//Update for the CPU player
					this.grid.getBoard(this.boardElement);
					//playersPlay = player.playRandomly(this.grid);
					playersPlay = player.playFromBrain(this.grid);
				}
				
				if(playersPlay < 0) break;
				
				//Click where he playes the tile
				this.driver.findElement(By.xpath(this.grid.getTileToClick(playersPlay))).click();
				
				//Must sleep because the program plays too fast
				Thread.sleep(500);
				
				//update our int[] board with the newly placed value
				this.grid.changeBoard(playersPlay, Config.PLAYER_TILE);
				
				//Update for the CPU player
				this.grid.getBoard(this.boardElement);
			}
			
			//Once the game is one clear the board
			this.grid.clearBoard();
			//Sleep because we click restart way too fast
			Thread.sleep(500);
			//IF I DONT WORK ITS B/c I REMOVED THIS BLOCK//try{
				//Click restart
				this.restartElement.click();
			//}catch(Exception e){
				
			//}
			//Increment the turn
			this.turn++;
			//System.out.println("Finished Game: " + i);
		}
		//At the end of all the games return the fitness of the player
		player.getFitness(this.driver);
	}
	
	/*
	 * This method closes the WebDriver, is used when we are done running
	 */
	protected void closeDriver(){
		this.driver.quit();
	}
}
