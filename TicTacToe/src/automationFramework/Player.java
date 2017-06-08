package automationFramework;

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
			
			for(int j=0; j<Config.BRAINDENSITY; j++){
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
		
		for(int i=inputs.length; i<Config.BRAINSIZE; i++){
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
