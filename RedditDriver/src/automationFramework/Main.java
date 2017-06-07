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
	
	public static void main(String[] args) throws InterruptedException{
		//Init these two to default values...so we can always watch something if the user messes up input
		int pagesToWatch = 1;
		String URL = "https://www.reddit.com/r/youtubehaiku/";
		int watched = 0;
		
		if(OS.indexOf("win") >= 0){
			System.setProperty("webdriver.chrome.driver","ChromeDriver\\chromedriver_windows.exe");
		}else if(OS.indexOf("mac") >= 0){
			System.setProperty("webdriver.chrome.driver","ChromeDriver\\chromedriver_mac.app");
		}else if(OS.indexOf("nix") >= 0){
			System.setProperty("webdriver.chrome.driver","ChromeDriver\\chromedriver_linux.exe"); //no idea what the extension is
		}else{
			System.out.println("OS is not supported!");
			System.exit(0);
		}
		
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
