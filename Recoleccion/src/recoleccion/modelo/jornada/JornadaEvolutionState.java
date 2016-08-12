package recoleccion.modelo.jornada;

import ec.simple.SimpleEvolutionState;

public class JornadaEvolutionState extends SimpleEvolutionState {

	private static final long serialVersionUID = 2988336103730866041L;
	
	public static final int[] TAMANO_POBLACION = {50, 100, 200};
	public static final double[] MUTATION_PROBS = {0.001, 0.01, 0.1};
	public static final double[] CROSSOVER_PROBS = {0.5, 0.75, 1.0};
	public static final int[] TOURNAMENT_SIZE = {2, 3, 7};
	private static final double[] ELITE_FRACTION = {0.10,0.25,0.50};
	
	
	@Override
	public void startFresh() {
		
		int jobNum = ((Integer)(job[0])).intValue();
		parameters.set(new ec.util.Parameter("pop.subpop.0.size"), "" + (TAMANO_POBLACION[jobNum % TAMANO_POBLACION.length]));
		jobNum /= TAMANO_POBLACION.length; 
		
		parameters.set(new ec.util.Parameter("pop.subpop.0.species.mutation-prob"), "" + (MUTATION_PROBS[jobNum % MUTATION_PROBS.length]));
		jobNum /= MUTATION_PROBS.length; 
			
    	parameters.set(new ec.util.Parameter("pop.subpop.0.species.crossover-prob"), "" + (CROSSOVER_PROBS[jobNum % CROSSOVER_PROBS.length]));
		jobNum /= CROSSOVER_PROBS.length; 

		parameters.set(new ec.util.Parameter("select.tournament.size"), "" + (TOURNAMENT_SIZE[jobNum % TOURNAMENT_SIZE.length]));
		jobNum /= TOURNAMENT_SIZE.length;	 	

		parameters.set(new ec.util.Parameter("breed.elite-fraction.0"), "" + (ELITE_FRACTION[jobNum % ELITE_FRACTION.length]));
		jobNum /= ELITE_FRACTION.length;
		 
		super.startFresh();
	}
}
