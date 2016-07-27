package recoleccion.ecj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import recoleccion.modelo.domicilios.Domicilio;
import recoleccion.modelo.jornada.Jornada;
import recoleccion.modelo.jornada.VertederoHandler;
import recoleccion.modelo.vehiculos.Vehiculo;
import recoleccion.modelo.viaje.Viaje;
import recoleccion.solucion.Solucion;
import ec.EvolutionState;
import ec.util.Parameter;
import ec.vector.IntegerVectorIndividual;

/**
 *
 * @author ggutierrez
 */
public class IntegerVectorIndividualRecoleccion extends IntegerVectorIndividual{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7478676840715483431L;
	
	@Override
	public void setup(EvolutionState state, Parameter base) {
		// TODO Auto-generated method stub
		super.setup(state, base);
	}
    
    @Override
    public void reset(EvolutionState state, int thread) {
       //CARGANDO LA SOLUCION RANDOMICA
       Solucion sol=new Solucion();
       Viaje viaje=new Viaje();
       List<Domicilio> domicilios = Jornada.getInstance().getDomicilioHandler().getDomicilios();
       List<Viaje> viajes=new ArrayList<>();             
       Random rand=new Random();
       while(!domicilios.isEmpty()){
           List<Domicilio> domiciliosViajes=new ArrayList<>();
           viaje=new Viaje();
            Vehiculo vehiculoActual=Jornada.getInstance().randomVehiculo();
            viaje.setVehiculo(vehiculoActual);
            //int randDomicilio=rand.nextInt(domicilios.size());  
            List<Domicilio> domiciliosValidos=vehiculoActual.domiciliosValidos(domicilios);
            //int randDomicilio=rand.nextInt(domiciliosValidos.size());  
            while (!vehiculoActual.isLleno() && !domiciliosValidos.isEmpty()){
                //randDomicilio=rand.nextInt(domicilios.size()); 
            	int randDomicilio=rand.nextInt(domiciliosValidos.size()); 
                Domicilio dom=new Domicilio();
                dom.setCoordenadas(domiciliosValidos.get(randDomicilio).getCoordenadas());
                dom.setIdentificador(domiciliosValidos.get(randDomicilio).getIdentificador());
                dom.setPedidos(domiciliosValidos.get(randDomicilio).getPedidos());
                domiciliosViajes.add(dom);
                vehiculoActual.recolectar(dom);
                domiciliosValidos.remove(dom);
                if (!dom.tieneResiduo()){
                	domicilios.remove(dom);
                }
            }        
            vehiculoActual.verter(VertederoHandler.getInstance().get(vehiculoActual));
            if (domiciliosViajes.size()>0){
                viaje.setDomicilios(domiciliosViajes);  
                viajes.add(viaje);
            }
       }
       sol.setViajes(viajes);
       
       System.out.print("GENOMA: ");
       int posGenoma=0;
       for (int i=0;i<sol.getViajes().size();i++){
           genome[posGenoma]=Integer.parseInt(sol.getViajes().get(i).getVehiculo().getIdentificador());
           System.out.print(genome[posGenoma]+"|");
           for (int j=0;j<sol.getViajes().get(i).getDomicilios().size();j++){
               posGenoma++;
               genome[posGenoma]=Integer.parseInt(sol.getViajes().get(i).getDomicilios().get(j).getIdentificador());
               System.out.print(genome[posGenoma]+"|");
           }
       
       }
       System.out.println();
	   
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
		    
}