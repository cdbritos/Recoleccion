package recoleccion.modelo.jornada;

import ec.EvolutionState;
import ec.simple.SimpleProblemForm;
import ec.simple.SimpleStatistics;

public class JornadaStatics extends SimpleStatistics {
	
	private static final long serialVersionUID = 1L;

	@Override
	public void finalStatistics(EvolutionState state, int result) {
		for(int x=0;x<state.population.subpops.length;x++ )
        {
			// finally describe the winner if there is a description
			if (doFinal && doDescription) 
				if (state.evaluator.p_problem instanceof SimpleProblemForm)
					((SimpleProblemForm)(state.evaluator.p_problem.clone())).describe(state, best_of_run[x], x, 0, statisticslog);      
        }
		
    }


	@Override
	public void generationBoundaryStatistics(EvolutionState state) {
		System.out.println("G");
	}


}
