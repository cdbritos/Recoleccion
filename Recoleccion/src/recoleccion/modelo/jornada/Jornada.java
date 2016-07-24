package recoleccion.modelo.jornada;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import recoleccion.ecj.IntegerVectorIndividualRecoleccion;
import recoleccion.modelo.data.Coordenada;
import recoleccion.modelo.data.TipoResiduo;
import recoleccion.modelo.domicilios.Domicilio;
import recoleccion.modelo.domicilios.Pedido;
import recoleccion.modelo.vehiculos.Camion;
import recoleccion.modelo.vehiculos.Camioneta;
import recoleccion.modelo.vehiculos.TipoVehiculo;
import recoleccion.modelo.vehiculos.Vehiculo;
import recoleccion.solucion.Solucion;
import ec.EvolutionState;
import ec.Individual;
import ec.Problem;
import ec.simple.SimpleFitness;
import ec.simple.SimpleProblemForm;
import ec.util.Parameter;

public class Jornada extends Problem implements SimpleProblemForm {
	
	private static final long serialVersionUID = 1L;
	
	public static final long DURACION_JORNAL_HORAS = 8;
	public static final long COSTO_JORNAL_POR_EMPLEADO = 3000;
	public static final long COSTO_HORA_EXTRA = (COSTO_JORNAL_POR_EMPLEADO / DURACION_JORNAL_HORAS) * 3;
	
	public static final String DEPOSITOS_IN = "in_depositos";
    public static final String DOM_JOR_IN = "in_domicilios_jornada";
    public static final String VERTEDEROS_IN = "in_vertederos";
    
    public File in_depositos;
    public File in_domicilios_jornada;
    public File in_vertederos;   
    
    private static Jornada instancia = null;
    
    public Jornada(){
    	
    }
	
    public static final Jornada getInstance(){
    	
    	if (instancia == null)
    		instancia = new Jornada();
    	
    	return instancia;
    }
    
	public List<Deposito> depositos;
	
	public List<Domicilio> domicilios;
	
	public Vertedero vertedero;

	public List<Deposito> getDepositos() {
		return depositos;
	}

	public void setDepositos(List<Deposito> depositos) {
		this.depositos = depositos;
	}

	public List<Domicilio> getDomicilios() {
		return domicilios;
	}

	public void setDomicilios(List<Domicilio> domicilios) {
		this.domicilios = domicilios;
	}

	public Vertedero getVertedero() {
		return vertedero;
	}

	public void setVertedero(Vertedero vertedero) {
		this.vertedero = vertedero;
	}

	@Override
	public void evaluate(EvolutionState state, Individual ind, int subpopulation, int threadnum) {
	
		if (ind.evaluated) return;
        
        IntegerVectorIndividualRecoleccion ind2 = (IntegerVectorIndividualRecoleccion)ind;
        
        double fitness = new Solucion(ind2).fitness();
        
        ((SimpleFitness) ind2.fitness).setFitness(state, fitness*(-1), fitness==0);
        
        ind.evaluated=true;
        
        
	}
	
	@Override
	public void setup(EvolutionState state, Parameter base) {
	   //super.setup(state, base);
		
	   in_depositos = state.parameters.getFile(base.push(DEPOSITOS_IN), null);
       in_domicilios_jornada = state.parameters.getFile(base.push(DOM_JOR_IN), null);
       in_vertederos = state.parameters.getFile(base.push(VERTEDEROS_IN), null);
	   
       try {
    	   //cargo vertedero
    	   vertedero = cargarVertedero();
    	    	   
    	   //cargo domicilios con sus pedidos
    	   domicilios = cargarDomicilios();
    	   
    	   //cargo depositos
    	   depositos = cargarDepositos();

    	   imprimir();
      } catch (Exception e) {
    	  System.out.println("ERROR CARGANDO PARAMETROS DE ENTRADA");
      }
	}
	
	private void imprimir() {
		vertedero.imprimir();
		
		if (CollectionUtils.isNotEmpty(depositos))
			for (Deposito deposito : depositos) {
				deposito.imprimir();
			}
		
		if (CollectionUtils.isNotEmpty(domicilios))
			for (Domicilio dom : domicilios) {
				dom.imprimir();
			}

	}

	private List<Deposito> cargarDepositos() throws FileNotFoundException  {
		   List<Deposito> depositos =new ArrayList<>();
		   
	       Scanner s = new Scanner(in_depositos);
	       Coordenada coord=new Coordenada();
	       int idVehiculo=0;
	       Deposito deposito=null;
	       List<Vehiculo> vehiculos=null;
	       String linea = s.nextLine(); 
	       String [] cadena=linea.split(",");
	       
	       while (s.hasNextLine()) {
			   
			   if (cadena[0].equals("DEPOSITO")){
	                   double latitud=Double.parseDouble(cadena[1]);
	                   double longitud=Double.parseDouble(cadena[2]);
	                   coord=new Coordenada(latitud,longitud);
	                   deposito = new Deposito();
	                   deposito.setCoordenadas(coord);
	                   linea = s.nextLine(); 
	                   cadena=linea.split(",");
	           }else{
	                  boolean salir=false;
	                  vehiculos = new ArrayList<>();
	                  while (!salir && !cadena[0].equals("DEPOSITO")){
	                       
	                	  int cantVehiculos=Integer.parseInt(cadena[0]);
	                       
	                       for(int i=0;i<cantVehiculos;i++){
	                               idVehiculo++;
	                               if (cadena[1].equals(TipoVehiculo.CAMION.name())){
	                                       Camion camion=new Camion(idVehiculo);
	                                       camion.setCoordenadas(coord);
	                                       camion.setTiposResiduos(calcularTipoResiduo(linea));
	                                       vehiculos.add(camion);

	                               }
	                               else if (cadena[1].equals(TipoVehiculo.CAMIONETA.name())){
	                                       Camioneta camiononeta=new Camioneta(idVehiculo);
	                                       camiononeta.setCoordenadas(coord);
	                                       camiononeta.setTiposResiduos(calcularTipoResiduo(linea));
	                                       vehiculos.add(camiononeta); 
	                               }else
	                            	   System.out.println("ERROR: TIPO VEHICULO INCORRECTO");
	                       }
	                       
	                       if (s.hasNextLine()){
	                           linea = s.nextLine(); 
	                           cadena=linea.split(",");
	                       }else{
	                           salir=true;
	                       }
	                       
	                  }
	                  
	                  deposito.setFlota(vehiculos);
	                  depositos.add(deposito);
			   }
		   }
	       
	       s.close();
	       	
	       return depositos;
		}

	   private List<Domicilio> cargarDomicilios() throws FileNotFoundException {
		   Scanner s = new Scanner(in_domicilios_jornada);
		   int identificador=0;
		   List<Domicilio> domicilios = new ArrayList<Domicilio>();
		   while (s.hasNextLine()) {
	            String linea = s.nextLine(); 
	            String [] cadena=linea.split(",");
	            
	            double latitud=Double.parseDouble(cadena[0]);
	            double longitud=Double.parseDouble(cadena[1]);
	            
	            int cantPedidos = 2;
	            List<Pedido> pedidos=new ArrayList<>();
	                        
	            while (cantPedidos<cadena.length){
	                Pedido pedido=new Pedido();
	                pedido.setCantidad(Long.valueOf(cadena[cantPedidos]));
	                cantPedidos++;
	                String tipoResiduo=cadena[cantPedidos];
	                pedido.setResiduo(TipoResiduo.valueOf(tipoResiduo.toUpperCase()));
	                cantPedidos++;
	                pedidos.add(pedido);
	            }
	           
	            Coordenada coord = new Coordenada(latitud,longitud);
	            Domicilio domicilio = new Domicilio(identificador++);
	            domicilio.setCoordenadas(coord);
	            domicilio.setPedidos(pedidos);
	            
	            domicilios.add(domicilio);
		   }
		   
		   s.close();
		   
		   return domicilios;
		}
	   
	   private Vertedero cargarVertedero() throws FileNotFoundException  {
		   Scanner s = new Scanner(in_vertederos);
		   Vertedero vertedero = new Vertedero();
		   while (s.hasNextLine()) {
			   String linea = s.nextLine();         
			   double latitud=Double.parseDouble(linea.split(",")[0]);
			   double longitud=Double.parseDouble(linea.split(",")[1]);
	           Coordenada coord=new Coordenada();
	           coord.setLatitud(latitud);
	           coord.setLongitud(longitud);
	           vertedero.setCoordenadas(coord);
		   }
		   
		   s.close();
		   
		   return vertedero;
	   }
	   
	   private Set<TipoResiduo> calcularTipoResiduo(String linea){
		   Set<TipoResiduo> tiposResiduos=new HashSet<TipoResiduo>();
		   String[] cadena=linea.split(",");
		   int contTipos=2;
		   while (contTipos<cadena.length){
			   tiposResiduos.add(TipoResiduo.valueOf(cadena[contTipos]));
	           contTipos++;
		   }
		   return tiposResiduos;
		   
	   }
	   
	    @Override
	    public void describe(EvolutionState state, Individual ind,
	    		int subpopulation, int threadnum, int log) {
	    	// TODO Auto-generated method stub
	    	state.output.println( ind.genotypeToStringForHumans(), log );
	    }

	    public Vehiculo randomVehiculo(){
	    	return randomDeposito().randomVehiculo();
	    }
	    
	    public Deposito randomDeposito(){
			try {
		        return depositos.get((new Random()).nextInt(depositos.size()));
		    }
		    catch (Throwable e){
		        return null;
		    }
	    }
}
