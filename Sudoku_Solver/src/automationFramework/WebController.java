package automationFramework;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class WebController {
	
	private WebDriver driver;
	private String URL;
	private Grid grid;
	private Actions action;
	
	protected WebController(String URL){
		this.driver = new ChromeDriver();
		this.URL = URL;
		this.loadPage();
	}
	
	/*
	 * This method loads the web page and creates an action, this is used for inserting the correct values 
	 * for some reason I couln't focus the web elements
	 * 
	 */
	protected void loadPage(){
		try{
			this.driver.get(this.URL);
			this.action = new Actions(this.driver);
		}catch(Exception e){
			System.out.println("Not a valid page: " + this.URL + "\nClosing program");
			this.closeDriver();
		}
	}
		
	/*
	 * This method takes the initial board and parses out each value to create the grid object
	 */
	protected void parseStartingBoard(){
		ArrayList<Integer> board = new ArrayList<Integer>();
		String path;
		//Loop through each block in the board and append it to our ArrayList
		for(int i=1; i<10; i++){
			for(int j=1; j<10; j++){
				path = "//*[@id='table']/tbody/tr[" + i + "]/td[" + j  + "]";
				String text = this.driver.findElement(By.xpath(path)).getText();
				//Parse it out, very simple
				if(text.equals(" ")) board.add(0); else board.add(Integer.parseInt(text));
			}
		}
		//Once we have the whole initial board we can create our grid object
		this.grid = new Grid(board);
	}
	
	/*
	 * This method is called after we parse and solve the board.
	 * It takes the new values and inserts them into their cell
	 */
	protected void insertSolvedBoard() throws InterruptedException{
		if(this.grid.solveSudoku(0, 0, this.grid.board)){
			System.out.println("Found solved solution");
			String path;
			
			for(int i=1; i<10; i++){
				for(int j=1; j<10; j++){
					path = "//*[@id='table']/tbody/tr[" + i + "]/td[" + j  + "]";
					WebElement cell = this.driver.findElement(By.xpath(path));
					String text = cell.getText();
					//If we have a null input we will insert 
					if(text.equals(" ")){
						String value = Integer.toString(this.grid.board[i-1][j-1]);
						this.performClickAction(cell, value);
					}
				}
			}
			
			this.driver.findElement(By.xpath("//*[@id='primary']/div/div[2]/nav/ul/li[3]/i")).click();
		}else{
			System.out.println("Unable to find solution");
		}
	}
	
	/*
	 * This method clicks on a given element and sends a given key string to the element
	 * @param WebElement element - The element we want to perform the action on
	 * @param String toEnter - The key sequence we want to submit
	 */
	protected void performClickAction(WebElement element, String toEnter){
		try{
			action.moveToElement(element);
			action.click();
			action.sendKeys(toEnter);
			action.build().perform();
		}catch(Exception e){
			System.out.println("Unable to perform click action");
		}
	}
	/*
	 * This method closes the WebDriver, is used when we are done running
	 */
	protected void closeDriver(){
		this.driver.quit();
	}
}
