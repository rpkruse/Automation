package automationFramework;

public class Config {
	//Game stats:
	public final static int NUMOFGAMES = 10;
	public final static int BOARDSIZE = 9;
	
	//Brain stats:
	public final static int BRAINSIZE = 180; //40 num of neurons (180 worked really well)
	public final static int BRAINDENSITY = 3;//3 num of synapses per neuron
	public final static int HIDDEN_LAYERS = 4; //9-27-81-27
	
	//Population stats:
	public final static int POPULATION_SIZE = 30;
	public final static int GENERATIONS_TO_RUN = 100;
	public final static int NUM_TO_KEEP_PER_GEN = 10;
	
	public final static double MUTATERATE1 = 0.1; //How often they mutate
	public final static double MUTATERATE2 = 0.3; //How bad is each mutation
	
	//Board stats:
	public final static double PLAYER_TILE = 1.0;
	public final static double EMPTY_TILE = 0.5;
	public final static double CPU_TILE = 0.0;
}
