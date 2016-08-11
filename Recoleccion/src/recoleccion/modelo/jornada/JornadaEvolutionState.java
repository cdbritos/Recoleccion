package recoleccion.modelo.jornada;

import ec.simple.SimpleEvolutionState;

public class JornadaEvolutionState extends SimpleEvolutionState {

	private static final long serialVersionUID = 2988336103730866041L;
	
	public static final int[] GENERATIONS = {500, 2, 3};
	public static final double[] MUTATION_PROBS = {0.01, 0.05, 0.1};
	public static final double[] CROSSOVER_PROBS = {0.75, 0.75, 0.9};
	public static final int[] TOURNAMENT_SIZE = {2, 3, 7};
	
	@Override
	public void startFresh() {
		
		int jobNum = ((Integer)(job[0])).intValue();
		parameters.set(new ec.util.Parameter("generations"), "" + (GENERATIONS[jobNum % GENERATIONS.length]));
		jobNum /= GENERATIONS.length; 
		
		parameters.set(new ec.util.Parameter("pop.subpop.0.species.mutation-prob"), "" + (MUTATION_PROBS[jobNum % MUTATION_PROBS.length]));
		jobNum /= MUTATION_PROBS.length; 
			
    	parameters.set(new ec.util.Parameter("pop.subpop.0.species.crossover-prob"), "" + (CROSSOVER_PROBS[jobNum % CROSSOVER_PROBS.length]));
		jobNum /= CROSSOVER_PROBS.length; 

		parameters.set(new ec.util.Parameter("select.tournament.size"), "" + (TOURNAMENT_SIZE[jobNum % TOURNAMENT_SIZE.length]));
		jobNum /= TOURNAMENT_SIZE.length;	 	

		super.startFresh();
	}
}
