package automationFramework;

import java.util.ArrayList;

public class Main {

	private static String OS = System.getProperty("os.name").toLowerCase();
	
	public static void playLinks(WebController controller, ArrayList<String> links){
		for(int i=0; i<links.size(); i++){
			//if(i==0) continue;
			String url = links.get(i);
			controller.loadVideoPage(url);
		}
	}
	
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
		//Init these two to default values...so we can always watch something if the user messes up input
		int pagesToWatch = 1;
		String URL = "https://www.reddit.com/r/youtubehaiku/";
		int watched = 0;
		
		//Get which ChromeDriver to load based on the OS we are running
		System.setProperty("webdriver.chrome.driver", getProperty());
		
		
		//System.setProperty("webdriver.chrome.driver","C:\\Users\\User\\Desktop\\chromedriver_win32\\chromedriver.exe"); //DEV ONLY
		
		try{
			URL = args[0];
			pagesToWatch = Integer.parseInt(args[1]);
		}catch(Exception e){
			System.out.println("Invalid arguments! Be sure to put <link>, <number of pages to view>");
			System.exit(0);
		}
				
		WebController controller = new WebController(URL);
		controller.loadHomePage();
		
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
		
		while(watched < pagesToWatch && true){
			playLinks(controller, controller.getPageOfUrls());
			
			controller.nextPage();
			watched++;
		}
		controller.closeDriver();
	}

}
