package recoleccion.ecj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import recoleccion.modelo.domicilios.Domicilio;
import recoleccion.modelo.jornada.Jornada;
import recoleccion.modelo.jornada.VertederoHandler;
import recoleccion.modelo.vehiculos.Vehiculo;
import recoleccion.modelo.vehiculos.VehiculoHandler;
import recoleccion.solucion.Solucion;
import recoleccion.solucion.Solucion.Viaje;
import ec.EvolutionState;
import ec.util.MersenneTwisterFast;
import ec.util.Parameter;
import ec.vector.IntegerVectorIndividual;
import ec.vector.IntegerVectorSpecies;
import ec.vector.VectorIndividual;

/**
 *
 * @author ggutierrez
 */
public class IntegerVectorIndividualRecoleccion extends IntegerVectorIndividual{

	private static final long serialVersionUID = -7478676840715483431L;
	
	@Override
	public void setup(EvolutionState state, Parameter base) {
		// TODO Auto-generated method stub
		super.setup(state, base);
	}
    
    public void reset(EvolutionState state, int thread) {
       //CARGANDO LA SOLUCION RANDOMICA           
       Solucion sol=new Solucion();
	   
	   List<Viaje> viajes=new ArrayList<>();
	   List<Domicilio> domicilios=sol.getDomiciliosSolucion();
	   
	   while (!domicilios.isEmpty()){
		   Vehiculo vehiculoActual=sol.getRandomVehiculoSolucion();
		   List<Domicilio> domiciliosValidos=vehiculoActual.domiciliosValidos(domicilios);
		   List<Domicilio> domViajes=new ArrayList<>();
		   while (!domiciliosValidos.isEmpty()){
			   Domicilio domCercano=domicilioMasCerca(vehiculoActual,domiciliosValidos);
			   
			   //CORREGIR DURACION DE VIAJE
			   if (vehiculoActual.llegueDuracionMaxima()){
				   vehiculoActual.verter(VertederoHandler.getInstance().getVertederos().get(0));				   
				   sol.getVehiculosSolucion().remove(vehiculoActual);
				   break;
			   }
			   //CORREGIR DURACION DE VIAJE
			   if (vehiculoActual.puedeRecolectar(domCercano)){
				   vehiculoActual.recolectar(domCercano);
				   domViajes.add(domCercano);
			   }else if (!vehiculoActual.isLleno())
				   domiciliosValidos.remove(domCercano);
			      			   
			   if (vehiculoActual.isLleno()){
				   vehiculoActual.verter(VertederoHandler.getInstance().getVertederos().get(0));
			   }
			   
			   if (!domCercano.tieneResiduo()){
				   domicilios.remove(domCercano);
			   }
		   }
		   
		   Viaje viaje=sol.new Viaje(vehiculoActual,domViajes);
		   viajes.add(viaje);   		   
	   }
	   
	   sol.setViajes(viajes);
       
       sol.setGenoma(this);
       System.out.println(this.genotypeToStringForHumans());
       
               
    }
   
	private static Domicilio domicilioMasCerca(Vehiculo v,List<Domicilio> domiciliosARecorrer){
		double distanciaMenor=Double.MAX_VALUE;
		Domicilio domCerca=null;
		for (int i=0;i<domiciliosARecorrer.size();i++){		
			double distancia=v.distance(domiciliosARecorrer.get(i).getCoordenadas());
			if (distancia<=distanciaMenor){
				distanciaMenor=distancia;
				domCerca=domiciliosARecorrer.get(i);
			}			
		}
		return domCerca;
	}
   
   /* ****************************************************
    * COMIENZO MERGE  
    * ****************************************************/
   
	public int getCantViajes(){
		int cantidadViajes = 0;
		
		for(int i = 0; i < genomeLength(); i++)
			if (genome[i] < 0)
				cantidadViajes++;
		
		return cantidadViajes;
		
	};
	
	private int[] getComienzosViajes(){
		
		int[] points = new int[getCantViajes()];
		int j = 0;
		for(int i = 0; i < genomeLength(); i++){
			if (genome[i] < 0){
				points[j] = i;
				j++;
			}
		}
		
		return points;
	}
	
	public int[][] getViajesIndividuo(){
		int[] points = this.getComienzosViajes();
		int[][] viajes = new int[points.length+1][];
		this.split(this.getComienzosViajes(), viajes);
		
		return viajes;
	}

	private void split(int[] points, int[][] pieces) {
	    int point0, point1;
	    point0 = 0; point1 = points[0];
	    for(int x=0;x<pieces.length;x++){
	    	pieces[x] = new int[point1-point0];
	    	System.arraycopy(genome,point0,pieces[x],0,point1-point0);
	    	point0 = point1;
	    	if (x >=pieces.length-2)
	    		point1 = genome.length;
	    	else point1 = points[x+1];
        }
	}

    /** Destructively mutates the individual in some default manner.  The default form
    simply randomizes genes to a uniform distribution from the min and max of the gene values. */
	public void defaultMutate(EvolutionState state, int thread)
    {
    IntegerVectorSpecies s = (IntegerVectorSpecies) species;
    for(int x = 0; x < genome.length; x++)
        if (state.random[thread].nextBoolean(s.mutationProbability(x)))
            {
            int old = genome[x];
            for(int retries = 0; retries < s.duplicateRetries(x) + 1; retries++)
                {
                switch(s.mutationType(x))
                    {
                    case IntegerVectorSpecies.C_RESET_MUTATION:
                        genome[x] = randomValueFromClosedInterval(x, state.random[thread]);
                        break;
                    default:
                        state.output.fatal("In IntegerVectorIndividual.defaultMutate, default case occurred when it shouldn't have");
                        break;
                    }
                if (genome[x] != old) break;
                // else genome[x] = old;  // try again
                }
            }
    }

	private int randomValueFromClosedInterval(int pos, MersenneTwisterFast mersenneTwisterFast) {
		if (genome[pos] < 0){
			//retorna un vehiculo aleatorio
			return genome[pos];
			//return VehiculoHandler.doMutate(genome[pos],randomValueFromClosedInterval(Jornada.getInstance().getMinGene(), -1, mersenneTwisterFast));
		}
		
		//retorna un domicilio
		return randomValueFromClosedInterval(1, Jornada.getInstance().getMaxGene(), mersenneTwisterFast);
	}
}
