package recoleccion.modelo.jornada;

import java.util.ArrayList;

import recoleccion.ecj.IntegerVectorIndividualRecoleccion;
import ec.Individual;
import ec.simple.SimpleEvolutionState;
import ec.util.Checkpoint;

public class JornadaEvolutionState extends SimpleEvolutionState {

	private static final long serialVersionUID = 1L;
	ArrayList<IntegerVectorIndividualRecoleccion> tabuList = new ArrayList<IntegerVectorIndividualRecoleccion>();
	private static final int TABU_SIZE = 30;
	
	public int evolve()
    {
    if (generation > 0) 
        output.message("Generation " + generation);

    // EVALUATION
    statistics.preEvaluationStatistics(this);
    evaluator.evaluatePopulation(this);
    statistics.postEvaluationStatistics(this);
    
    IntegerVectorIndividualRecoleccion mejor_generacion = (IntegerVectorIndividualRecoleccion) this.population.subpops[0].individuals[0];
    for ( Individual ind : this.population.subpops[0].individuals) {
    	IntegerVectorIndividualRecoleccion candidato = (IntegerVectorIndividualRecoleccion) ind;  
		if (!esTabu(candidato) && candidato.fitness.betterThan(mejor_generacion.fitness)){
			mejor_generacion = candidato;
		}
	}
    
    IntegerVectorIndividualRecoleccion mejorCorrida = (IntegerVectorIndividualRecoleccion)((JornadaStatics) statistics).best_of_run[0];	
    if (mejor_generacion.fitness.betterThan(mejorCorrida.fitness))
    	((JornadaStatics) statistics).best_of_run[0] = mejor_generacion;
    
    tabuList.add(mejor_generacion);
    if (tabuList.size() > TABU_SIZE)
    	tabuList.remove(0);
    
    // SHOULD WE QUIT?
    if (evaluator.runComplete(this) && quitOnRunComplete)
        {
        output.message("Found Ideal Individual");
        return R_SUCCESS;
        }
    
    // SHOULD WE QUIT?
    if (generation == numGenerations-1)
        {
        return R_FAILURE;
        }

    // PRE-BREEDING EXCHANGING
    statistics.prePreBreedingExchangeStatistics(this);
    population = exchanger.preBreedingExchangePopulation(this);
    statistics.postPreBreedingExchangeStatistics(this);

    String exchangerWantsToShutdown = exchanger.runComplete(this);
    if (exchangerWantsToShutdown!=null)
        { 
        output.message(exchangerWantsToShutdown);
        /*
         * Don't really know what to return here.  The only place I could
         * find where runComplete ever returns non-null is 
         * IslandExchange.  However, that can return non-null whether or
         * not the ideal individual was found (for example, if there was
         * a communication error with the server).
         * 
         * Since the original version of this code didn't care, and the
         * result was initialized to R_SUCCESS before the while loop, I'm 
         * just going to return R_SUCCESS here. 
         */
        
        return R_SUCCESS;
        }

    // BREEDING
    statistics.preBreedingStatistics(this);

    population = breeder.breedPopulation(this);
    
    // POST-BREEDING EXCHANGING
    statistics.postBreedingStatistics(this);
        
    // POST-BREEDING EXCHANGING
    statistics.prePostBreedingExchangeStatistics(this);
    population = exchanger.postBreedingExchangePopulation(this);
    statistics.postPostBreedingExchangeStatistics(this);

    // INCREMENT GENERATION AND CHECKPOINT
    generation++;
    if (checkpoint && generation%checkpointModulo == 0) 
        {
        output.message("Checkpointing");
        statistics.preCheckpointStatistics(this);
        Checkpoint.setCheckpoint(this);
        statistics.postCheckpointStatistics(this);
        }

    return R_NOTDONE;
    }

	private boolean esTabu(IntegerVectorIndividualRecoleccion candidato) {
		for (IntegerVectorIndividualRecoleccion ind : tabuList) {
			if (candidato.fitness.equivalentTo(ind.fitness))
				return true;
		}
		return false;
	}


}
