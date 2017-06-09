package automationFramework;

/*
 * TODO:
 * Train the network on premade games S.T. it knows how to win
 * Randomly train it?
 * Make the automation faster????
 * 
 */
public class Main { 

	private static String OS = System.getProperty("os.name").toLowerCase();
	
	/*
	 * This method takes our OS field and returns what type of ChromeDriver we need to use 
	 * @return String - the string of the ChromeDriver path
	 */
	public static String getProperty(){
		String property = "OS is not supported!";
		if(OS.indexOf("win") >= 0){
			property = "ChromeDriver\\chromedriver_windows.exe";
		}else if(OS.indexOf("mac") >= 0){
			property = "ChromeDriver\\chromedriver_mac";
		}else if(OS.indexOf("nix") >= 0){
			property = "ChromeDriver\\chromedriver_linux"; 
		}else{
			System.out.println(property);
			System.exit(0);
		}
		return(property);
	}
	
	public static void main(String[] args) throws InterruptedException{
		String URL = "https://playtictactoe.org/";
		
		//Get which ChromeDriver to load based on the OS we are running
		//System.setProperty("webdriver.chrome.driver", getProperty());
		
		
		System.setProperty("webdriver.chrome.driver","C:\\Users\\User\\Desktop\\chromedriver_win32\\chromedriver.exe"); //DEV ONLY
				
		//WebController controller = new WebController(URL);
		//controller.loadHomePage();
		
		PopulationController controller = new PopulationController(URL);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run(){
				try {
					System.out.println("Shutting down the driver...this may take a second");
					Thread.sleep(3000L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				controller.closeDriver();
				System.out.println("Chrome shutdown properly");
			}
		});
		
		/*while(watched < pagesToWatch && true){
			playLinks(controller, controller.getPageOfUrls());
			
			controller.nextPage();
			watched++;
		}*/
		controller.checkPlay();
		Thread.sleep(3000L);
		controller.closeDriver();
	}

}
