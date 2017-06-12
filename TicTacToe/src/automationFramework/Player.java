package automationFramework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

class Brain{ //https://users.auth.gr/kehagiat/Research/GameTheory/12CombBiblio/TicTacToe.pdf
	
	private double[] act = new double[Config.BRAINSIZE];
	private double[][] w = new double[Config.BRAINSIZE][Config.BRAINDENSITY];
	private int[][] ix = new int[Config.BRAINSIZE][Config.BRAINDENSITY];
	
	Random rand = new Random();
	
	public Brain(){
		//Init activations
		for(int i=0; i<Config.BRAINSIZE; i++){
			this.act[i] = 0;
		}
		
		for(int i = 0; i<Config.BRAINSIZE; i++){
			this.w[i] = new double[Config.BRAINDENSITY];
			this.ix[i] = new int[Config.BRAINDENSITY];
			
			for(int j=0; j<Config.BRAINDENSITY; j++){ //9-27-81-27
				this.w[i][j] = -1.2 * rand.nextDouble();
				this.ix[i][j] = rand.nextInt(Config.BRAINSIZE);
			}
		}
	}
	
	protected double[] tick(double[] inputs){
		double[] out = new double[Config.BOARDSIZE];
		
		//first 9 neurons are inputs
		for(int i=0; i<inputs.length; i++){
			this.act[i] = inputs[i];
		}
		
		int inputSize = inputs.length;
		this.act[inputSize] = 1;
		this.act[inputSize + 1] = 1;
		this.act[inputSize + 2] = 1;
		this.act[inputSize + 3] = 1;
		this.act[inputSize + 4] = 1;
		this.act[inputSize + 5] = 1;
		
		for(int i=inputSize + 6; i<Config.BRAINSIZE; i++){
			double a = 0;
			for(int j=0; j<Config.BRAINDENSITY; j++){
				a += this.w[i][j] * this.act[this.ix[i][j]];
			}
			this.act[i] = 1.0/(1.0+Math.exp(-a));
		}
		
		for(int i=0; i<inputs.length; i++){
			out[i] = this.act[Config.BRAINSIZE - 1 - i];
		}
		return(out);
	}
	
	protected void mutateBrain(Brain brain){
		for(int i=0; i<Config.BRAINSIZE; i++){
			for(int j=0; j<Config.BRAINDENSITY; j++){
				double m = brain.w[i][j];
				if(rand.nextDouble() < Config.MUTATERATE1) m += ((int)Config.MUTATERATE2 * rand.nextDouble());
				this.w[i][j] = m;
				
				m = brain.ix[i][j];
				if(rand.nextDouble() < Config.MUTATERATE1) m = rand.nextInt(Config.BRAINSIZE);
				this.ix[i][j] = (int) m;
			}
		}
	}
}

public class Player {

	protected Brain brain;
	protected int fitness; 

	public Player(){
		this.brain = new Brain();
		//this.trainBrain();
	}
	
	//note it is totally terrible to do it this way...I am only doing this to test the network!
	public void trainBrain(){
		String fn = "training_data.txt";
		String line;
		
		try{
			FileReader fileReader = new FileReader(fn);
			BufferedReader buffer = new BufferedReader(fileReader);
			
			while((line = buffer.readLine()) != null){
				String[] data = line.split(",");
				
				if(data[data.length-1].equals("positive")){

					double[] inputs = new double[Config.BOARDSIZE];
					
					for(int i=0; i<Config.BOARDSIZE; i++){
						if(data[i].equals("x")){
							inputs[i] = Config.PLAYER_TILE;
						}else if(data[i].equals("o")){
							inputs[i] = Config.CPU_TILE;
						}else{
							inputs[i] = Config.EMPTY_TILE;
						}
					}
					
					this.brain.tick(inputs);
				}
			}
			
			buffer.close();
		}catch(Exception e){
			System.out.println("Error when training brain!");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void getFitness(WebDriver driver){
		int wins = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[4]/p[1]/span[4]")).getText());
		int ties = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[4]/p[2]/span")).getText());
		//int losses = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[4]/p[3]/span[4]")).getText());
		
		//this.fitness = (wins + (ties/2)) - losses;
		this.fitness = wins + (ties/2);
	}
	
	protected int playFromBrain(Grid grid){
		int out = -1;
		double[] brainOutputs = this.brain.tick(grid.board);
		double highest = -1;
		
		for(int i=0; i<brainOutputs.length; i++){
			if(brainOutputs[i] >= highest && grid.board[i] == Config.EMPTY_TILE){
				highest = brainOutputs[i];
				out = i;
			}
			//System.out.println("Output at: " + i + " is: " + brainOutputs[i]);
		}
		return(out);
	}
}
