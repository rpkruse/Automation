package automationFramework;

import java.util.Random; //rangeMin + (rangeMax - rangeMin) * r.nextDouble();

public class PopulationController extends WebController{
	
	private Player[] population = new Player[Config.POPULATION_SIZE];
	private Player[] mostFit = new Player[Config.NUM_TO_KEEP_PER_GEN];
	
	protected PopulationController(String url){
		super(url);
		
		for(int i=0; i<Config.POPULATION_SIZE; i++){
			this.population[i] = new Player();
		}
	}
	
	private void updateMostFit(int i){
		//Loop for the ones we want to keep
		for(int k=0; k<Config.NUM_TO_KEEP_PER_GEN; k++){
			//If we haven't filled it yet, fill the spot
			if(mostFit[k] == null){
				mostFit[k] = population[i];
			}else{//Otherwise check if we have a more fit person
				if(mostFit[k].fitness < population[i].fitness){
					mostFit[k] = population[i];
				}
			}
		}
	}
	
	protected void checkPlay() throws InterruptedException{
		Random rand = new Random();
		//For every generation
		for(int j=0; j<Config.GENERATIONS_TO_RUN; j++){
			//Run each player
			for(int i=0; i<Config.POPULATION_SIZE; i++){
				super.loadHomePage(population[i]);
				super.checkPlay();
				System.out.println("Fitness of player: " + i + " is: " + population[i].fitness);
				
				this.updateMostFit(i);
			} //End of generation
			
			System.out.println("\n----------End of generation: " + j + "----------\n");
			
			for(int i=0; i<Config.POPULATION_SIZE; i++){
				if(i < Config.NUM_TO_KEEP_PER_GEN){
					population[i] = mostFit[i];
				}else{
					population[i] = new Player();
					if(rand.nextDouble() < Config.MUTATERATE1) //If we pass the mutate rate, the new spawn is based on its parents brain
						population[i].brain.mutateBrain(population[rand.nextInt(Config.NUM_TO_KEEP_PER_GEN)].brain);
				}
			}
		} //End of run
	}
	
	
}
