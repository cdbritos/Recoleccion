package recoleccion.ecj;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import recoleccion.modelo.data.Coordenada;
import recoleccion.modelo.data.TipoResiduo;
import recoleccion.modelo.domicilios.Domicilio;
import recoleccion.modelo.domicilios.Pedido;
import recoleccion.modelo.jornada.Deposito;
import recoleccion.modelo.jornada.Jornada;
import recoleccion.modelo.jornada.Vertedero;
import recoleccion.modelo.vehiculos.Camion;
import recoleccion.modelo.vehiculos.Camioneta;
import recoleccion.modelo.vehiculos.TipoVehiculo;
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
    public void reset(EvolutionState state, int thread) {
       //CARGANDO LA SOLUCION RANDOMICA
       Solucion sol=new Solucion();
       Viaje viaje=new Viaje();
       List<Domicilio> domicilios = Jornada.getInstance().getDomicilios();
       //List<Domicilio> domiciliosCompletos=new ArrayList<>();
      // List<Domicilio> domiciliosViajes=new ArrayList<>();
       List<Viaje> viajes=new ArrayList<>();
       //domiciliosCompletos=jornada.getDomicilios();         
       
       Random rand=new Random();
       //List<Vehiculo> vehiculos=new ArrayList<>();
       while(!domicilios.isEmpty()){
           List<Domicilio> domiciliosViajes=new ArrayList<>();
           viaje=new Viaje();
           //System.out.println("DOMICILIO: "+domicilios);
            int randDeposito=rand.nextInt(Jornada.getInstance().getDepositos().size());
            Deposito depActual=Jornada.getInstance().getDepositos().get(randDeposito);
            int randVehiculo=rand.nextInt(depActual.getFlota().size());
            Vehiculo vehiculoActual=depActual.getFlota().get(randVehiculo);
            viaje.setVehiculo(vehiculoActual);
            //int randDomicilio=rand.nextInt(domiciliosCompletos.size()-1);
             
            int i=0;
            boolean valido=false;
            while (i<domicilios.size() && vehiculoActual.getCapacidad()>0){
                //System.out.println("VEHICULO: "+vehiculoActual);
                //randDomicilio=rand.nextInt(domiciliosCompletos.size()-1);
                valido=false;
                
                Domicilio dom=new Domicilio();
                dom.setIdentificador(domicilios.get(i).getIdentificador());
                dom.setPedidos(domicilios.get(i).getPedidos());
                //dom=domicilios.get(i);
                Iterator iter = vehiculoActual.getTiposResiduos().iterator();
                while (iter.hasNext() && !valido) {
                    TipoResiduo tr=(TipoResiduo)iter.next();
                    if (dom.tieneResiduo(tr) > 0){
                        int j=0;
                        //TipoResiduo trPedido=dom.getPedidos().get(j).getResiduo();
                        while (j<dom.getPedidos().size()){
                            TipoResiduo trPedido=dom.getPedidos().get(j).getResiduo();
                            if (dom.tieneResiduo(tr) > 0){
                                //pedidosABorrar.add(dom.getPedidos().get(j));
                                Long capacidadActual=vehiculoActual.getCapacidad()-dom.getPedidos().get(j).getCantidad();
                                //System.out.println("CAPACIDAD ACTUAL: "+capacidadActual+" TIPO RESIDUOS: "+vehiculoActual.getTiposResiduos()+" ID VEHICULO: "+vehiculoActual.getIdentificador());
                                vehiculoActual.setCapacidad(capacidadActual);
                                //dom.getPedidos().remove(dom.getPedidos().get(j));
                                dom.getPedidos().remove(j);
                            }
                            j++;
                            //trPedido=dom.getPedidos().get(j).getResiduo();
                        }
                        //if (v.getCapacidad()>=0){
                            valido=true;
                            /*Long capacidadActual=vehiculoActual.getCapacidad()-dom.getPedidos().get(j).getCantidad();
                            vehiculoActual.setCapacidad(capacidadActual);
                            dom.getPedidos().remove(dom.getPedidos().get(j));*/
                            domiciliosViajes.add(dom);
                            if (dom.getPedidos().isEmpty()){
                                //System.out.println("REMOVI: "+dom.getIdentificador());
                                //domicilios.remove(dom);
                                //domicilios.remove(i);
                                domicilios.remove(dom);
                            }
                        //}             
                    }
                }
               i++;
                /*Domicilio domActual=domicilioValido(vehiculoActual,domiciliosCompletos,valido);
                domiciliosViajes.add(domActual);*/
                //System.out.println("CANT DOM: "+domiciliosCompletos.size());
                //System.out.println("CANT DOM: "+domicilios.size());
                
               
            }        
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
   
   
   public Domicilio domicilioValido(Vehiculo v,List<Domicilio> domicilios ,boolean valido){
	   Domicilio dom=new Domicilio();
       valido=false;
       int i=0;
       while (i<domicilios.size() && !valido){
            dom=domicilios.get(i);
            Iterator<TipoResiduo> iter = v.getTiposResiduos().iterator();
            
            while (iter.hasNext() && !valido) {
                TipoResiduo tr=(TipoResiduo)iter.next();
                if (dom.tieneResiduo(tr) > 0){
                    //System.out.println("CONTIENE PEDIDO: "+dom);
                    int j=0;
                    TipoResiduo trPedido=dom.getPedidos().get(j).getResiduo();
                    while (trPedido!=tr){
                        j++;
                        trPedido=dom.getPedidos().get(j).getResiduo();
                    }
                    //if (v.getCapacidad()>=0){
                        valido=true;
                        Long capacidadActual=v.getCapacidad()-dom.getPedidos().get(j).getCantidad();
                        v.setCapacidad(capacidadActual);
                        dom.getPedidos().remove(dom.getPedidos().get(j));
                        if (dom.getPedidos().isEmpty()){
                            //System.out.println("REMOVI: "+dom);
                            domicilios.remove(dom);
                        }
                    //}             
                }
            }
           i++;
       }
       if (i==domicilios.size()){
           valido=true;
       }
       
       return dom;
              
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