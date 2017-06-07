package automationFramework;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebController {
	
	private WebDriver driver;
	private String URL;
	
	/*
	 * Constructor for the WebController, it adds AdBlock to the ChromeDriver, so we can block YouTube ads
	 * @param String URL - the url of the page we want to load (Given by arg[0] or default home page)
	 */
	protected WebController(String URL){
		ChromeOptions options = new ChromeOptions();
		//options.addExtensions(new File("AdBlock_v3.10.0.crx")); //DEV ONLY
		options.addExtensions(new File("Ad_Block/AdBlock_v3.10.0.crx"));
		this.driver = new ChromeDriver(options);
		this.URL = URL;
	}
	
	/*
	 * This method loads the first of n pages into the driver, it will either be a home page or some given page
	 * This method is only used when navigating Reddit pages, it should not be called anywehre else
	 * 
	 */
	protected void loadHomePage(){
		try{
			this.driver.get(this.URL);
		}catch(Exception e){
			System.out.println("Not a valid page: " + this.URL + "\nClosing program");
			this.closeDriver();
		}
	}
	
	/*
	 * This method sets the current URL to the new URL, called after we are done with the nth page to view (when we click next)
	 */
	protected void setNewURL(){
		this.URL = this.driver.getCurrentUrl();
	}
	
	/*
	 * VIDEO STATES:
	 * 	  -1 = unstarted
	 * 	  0 = ended
	 * 	  1 = playing
	 * 	  2 = paused
	 *    3 = buffering
	 */
	/*
	 * This method takes a YouTube URL and loads, plays, and closes the given video.
	 * @param String URL - the YouTube page we want to load
	 * 
	 */
	protected void loadVideoPage(String URL){
		try{
			JavascriptExecutor jse = (JavascriptExecutor) this.driver;
			
			this.driver.get(URL);
			
			//mute
			//jse.executeScript("return document.getElementById('movie_player').mute()"); //DEV ONLY
			
			//Full screen
			this.driver.findElement(By.cssSelector("button[class='ytp-fullscreen-button ytp-button'")).click();
			
			
			//Load the video
			while((long)jse.executeScript("return document.getElementById('movie_player').getPlayerState()") == 3){
				Thread.sleep(1000L);
			}
			
			//Play the video
			while((long)jse.executeScript("return document.getElementById('movie_player').getPlayerState()") == 1){
				Thread.sleep(1000L);
			}
			
			this.driver.navigate().back();
		}catch (Exception e){
			System.out.println("Unable to load video: " + URL + " moving to new video");
		}
	}
	
	/*
	 * This method clicks the next page button when we are done with the current page and sets the URL to the new page
	 */
	protected void nextPage(){
		WebElement next = this.driver.findElement(By.className("next-button"));
		next.click();
		this.setNewURL();
	}
	
	/*
	 * This method reads the current page and returns all of the links on it (should be 25 per page)
	 * @return ArrayList<String> - a List of string URLs from the current page (done like this so we dont lose reference to the
	 * WebElements when we move back and fourth between pages.
	 */
	protected ArrayList<String> getPageOfUrls(){ //https://stackoverflow.com/questions/26304224/find-element-by-attribute
		try{
			List<WebElement> urlElements = this.driver.findElements(By.cssSelector("a[class='title may-blank outbound']"));
			ArrayList<String> urls = new ArrayList<String>();
			
			for(int i=0; i<urlElements.size(); i++){
				urls.add(urlElements.get(i).getAttribute("href"));
			}
			
			return(urls);
		}catch(Exception e){
			System.out.println("Unable to load links from current page");
			return(null);
		}
	}
	
	/*
	 * This method closes the WebDriver, is used when we are done running
	 */
	protected void closeDriver(){
		this.driver.quit();
		System.out.println("End of run!");
	}
}
