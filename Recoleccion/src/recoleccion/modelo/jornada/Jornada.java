package recoleccion.modelo.jornada;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import recoleccion.ecj.IntegerVectorIndividualRecoleccion;
import recoleccion.modelo.data.Coordenada;
import recoleccion.modelo.data.TipoResiduo;
import recoleccion.modelo.domicilios.Domicilio;
import recoleccion.modelo.domicilios.DomiciliosHandler;
import recoleccion.modelo.domicilios.Pedido;
import recoleccion.modelo.vehiculos.Camion;
import recoleccion.modelo.vehiculos.Camioneta;
import recoleccion.modelo.vehiculos.TipoVehiculo;
import recoleccion.modelo.vehiculos.Vehiculo;
import recoleccion.modelo.vehiculos.VehiculoHandler;
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
	public static final long COSTO_HORA_POR_EMPLEADO = 300;	
	public static final long COSTO_HORA_EXTRA = COSTO_HORA_POR_EMPLEADO * 3;
	public static final double DURACION_JORNADA = 16.0;
	
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
    
    
	@Override
	public void evaluate(EvolutionState state, Individual ind, int subpopulation, int threadnum) {
	
		if (ind.evaluated) return;
        
        IntegerVectorIndividualRecoleccion ind2 = (IntegerVectorIndividualRecoleccion)ind;
        
        double fitness = Double.MAX_VALUE;
		try {
			Solucion sol = new Solucion(ind2); 
			fitness = sol.fitness();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR CALCULANDO FITNESS");
		}
        
        ((SimpleFitness) ind2.fitness).setFitness(state, fitness*(-1), fitness == 0);
        
        ind.evaluated=true;      
	}
	
	public void limpiarGenome(IntegerVectorIndividualRecoleccion ind2) {
		
		while (ArrayUtils.contains(ind2.genome,0))		
			ind2.setGenome(ArrayUtils.removeElement(ind2.genome, 0));
	}

	@Override
	public void setup(EvolutionState state, Parameter base) {
	   //super.setup(state, base);
		
	   in_depositos = state.parameters.getFile(base.push(DEPOSITOS_IN), null);
       in_domicilios_jornada = state.parameters.getFile(base.push(DOM_JOR_IN), null);
       in_vertederos = state.parameters.getFile(base.push(VERTEDEROS_IN), null);
	   
       try {
    	   //cargo vertedero
    	   VertederoHandler.getInstance().setVertederos(cargarVertedero());
    	    	   
    	   //cargo depositos
    	   VehiculoHandler.getInstance().setVehiculos(cargarDepositos());
    	   
    	   //cargo domicilios con sus pedidos
    	   DomiciliosHandler.getInstance().setDomicilios(cargarDomicilios());

    	   //imprimir();
      } catch (Exception e) {
    	  e.printStackTrace();
    	  System.out.println("ERROR CARGANDO PARAMETROS DE ENTRADA");
      }
      
      base.param="pop.subpop.0.species";
      state.parameters.set(base.push("genome-size"), String.valueOf(getGenomeSize()));
      state.parameters.set(base.push("min-gene"), String.valueOf(getMinGene()));
      state.parameters.set(base.push("max-gene"), String.valueOf(getMaxGene()));
       
      //TODO las soluciones arrancan con un vehiculo
      state.parameters.set(base.push("min-gene.0"), String.valueOf(getMinGene()));
      state.parameters.set(base.push("max-gene.0"), "-1");
       
	}
	
	private void imprimir() {
		for (Vertedero vertedero : VertederoHandler.getInstance().getVertederos()) {
			vertedero.imprimir();
		}
		
		if (CollectionUtils.isNotEmpty(VehiculoHandler.getInstance().getVehiculos()))
			for (Vehiculo vehiculo : VehiculoHandler.getInstance().getVehiculos()) {
				vehiculo.imprimir();
			}
		
		if (CollectionUtils.isNotEmpty(DomiciliosHandler.getInstance().getDomicilios()))
			for (Domicilio dom : DomiciliosHandler.getInstance().getDomicilios()) {
				dom.imprimir();
			}

	}

	public List<Vehiculo> cargarDepositos() throws FileNotFoundException  {
		   //List<Deposito> depositos =new ArrayList<>();
		   
	       Scanner s = new Scanner(in_depositos);
	       Coordenada coord=new Coordenada();
	       int idVehiculo=0;
	       Deposito deposito=null;
	       List<Vehiculo> vehiculos=null;
	       List<Vehiculo> vehiculosJornada= new ArrayList<Vehiculo>();
	       String linea = null; 
	       String [] cadena= null;
	       
	       while (s.hasNextLine()) {
			   linea = s.nextLine();
			   cadena=linea.split(",");
			   if (cadena[0].equals("DEPOSITO")){
	                   double latitud=Double.parseDouble(cadena[1]);
	                   double longitud=Double.parseDouble(cadena[2]);
	                   coord=new Coordenada(latitud,longitud);
	                   deposito = new Deposito();
	                   deposito.setCoordenadas(coord);
	           }else{

	        	   int cantVehiculos=Integer.parseInt(cadena[0]);
                   vehiculos = new ArrayList<Vehiculo>();
                   
                   for(int i=0;i<cantVehiculos;i++){
                       idVehiculo--;
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
                   
                   vehiculosJornada.addAll(vehiculos);
	           	}
	                  
                //deposito.setFlota(vehiculos);
                //depositos.add(deposito);
                
	       	}
		   
	       
	       s.close();
	       	
	       return vehiculosJornada;
		}

	   private List<Domicilio> cargarDomicilios() throws FileNotFoundException {
		   Scanner s = new Scanner(in_domicilios_jornada);
		   int identificador=1;
		   List<Domicilio> domicilios = new ArrayList<Domicilio>();
		   while (s.hasNextLine()) {
	            String linea = s.nextLine(); 
	            String [] cadena=linea.split(",");
	            
	            double latitud=Double.parseDouble(cadena[1]);
	            double longitud=Double.parseDouble(cadena[2]);
	            
	            int cantPedidos = 3;
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
	            domicilio.setMunicipio(cadena[0]);
	            domicilio.setCoordenadas(coord);
	            domicilio.setPedidos(pedidos);
	            
	            domicilios.add(domicilio);
		   }
		   
		   s.close();
		   
		   return domicilios;
		}
	   
	   private List<Vertedero> cargarVertedero() throws FileNotFoundException  {
		   Scanner s = new Scanner(in_vertederos);
		   List<Vertedero>	vertederos = new ArrayList<Vertedero>();
		   while (s.hasNextLine()) {
			   Vertedero vertedero = new Vertedero();
			   String linea = s.nextLine();         
			   double latitud=Double.parseDouble(linea.split(",")[0]);
			   double longitud=Double.parseDouble(linea.split(",")[1]);
	           Coordenada coord=new Coordenada();
	           coord.setLatitud(latitud);
	           coord.setLongitud(longitud);
	           vertedero.setCoordenadas(coord);
	           vertederos.add(vertedero);
		   }
		   
		   s.close();
		   
		   return vertederos;
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
	   
	   public int getMaxGene(){
		   return DomiciliosHandler.getInstance().getDomicilios().size();
	   }
	   
	   public int getMinGene(){
		   return VehiculoHandler.getInstance().getVehiculos().size()*-1;
	   }
	   
	   public int getGenomeSize(){
		   long vehiculoMinimo = VehiculoHandler.getInstance().getCapacidadMinima();
		   List<Domicilio> doms = DomiciliosHandler.getInstance().getDomicilios();
		   int size = 0;
		   for (Domicilio domicilio : doms) {
			   for (Pedido p : domicilio.getPedidos()) {
				   size += Math.ceil((p.getCantidad() / vehiculoMinimo))+1;
			   }
		   }
		   return size*2;
	   }
	   
	    @Override
	    public void describe(EvolutionState state, Individual ind, int subpopulation, int threadnum, int log) {
	   
	    	//state.output.println("FITNESS: " + ind.fitness.fitness() , log);
	    	//state.output.println("POBLACION: " + state.parameters.getString(new ec.util.Parameter("pop.subpop.0.size"),null) , log);
	    	//state.output.println("MUTATION-PROB: " + state.parameters.getString(new ec.util.Parameter("pop.subpop.0.species.mutation-prob"),null) , log);
	    	//state.output.println("CROSSOVER-PROB: " + state.parameters.getString(new ec.util.Parameter("pop.subpop.0.species.crossover-prob"),null), log);
	    	//state.output.println("TOURNAMENT-SIZE: " + state.parameters.getString(new ec.util.Parameter("select.tournament.size"),null), log);
	    	//state.output.println("ELITE-FRACTION: " + state.parameters.getString(new ec.util.Parameter("breed.elite-fraction.0"),null), log);
	    	
	    	//int jobNum = ((Integer)(state.job[0])).intValue();
	    	//escribirEnExcel(jobNum,Integer.valueOf(state.runtimeArguments[2]).intValue(),ind.fitness.fitness()); 
	    	
	    	
	    	/*Solucion sol;
			try {	
			
				
					sol = new Solucion((IntegerVectorIndividualRecoleccion) ind);
					sol.fitness();
		    		for (Vehiculo v : sol.getVehiculosSolucion()) {	
		    			if (v.getCostoJornada() > 0)
		    				v.imprimir();
		    		}
		    	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
	    	
	    }

	    
	    
	    private synchronized void  escribirEnExcel(int column, int fila, double fitness) {
	    	String fileName = "Resultado2.xls";
	    	FileOutputStream fileOut = null;
	    	FileInputStream fileIn = null;	
			try {
				fileIn = new FileInputStream(fileName);
				HSSFWorkbook workbook = new HSSFWorkbook(fileIn);
				HSSFSheet sheet = workbook.getSheetAt(0);
				
				Row r = sheet.getRow(fila); // 10-1
				if (r == null) {
				   // First cell in the row, create
				   r = sheet.createRow(fila);
				}

				Cell c = r.getCell(column); // 4-1
				if (c == null) {
				    // New cell
				    c = r.createCell(column, Cell.CELL_TYPE_NUMERIC);
				}
				c.setCellValue(fitness);
								
				fileOut = new FileOutputStream(fileName);
				workbook.write(fileOut);
					
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
	                fileOut.close();
	            } catch (Exception ex) {
	                System.out.println("Error in file processing after close it (Error al procesar el fichero después de cerrarlo): " + ex);
	            }
				try {
	                fileIn.close();
	            } catch (Exception ex) {
	                System.out.println("Error in file processing after close it (Error al procesar el fichero después de cerrarlo): " + ex);
	            }
			}
			
		}



		public Vehiculo randomVehiculo(){
	    	return VehiculoHandler.getInstance().randomVehiculo();
	    }
	    
		public DomiciliosHandler getDomicilioHandler() {
			return DomiciliosHandler.getInstance();
		}

		public VehiculoHandler getVehiculoHandler() {
			return VehiculoHandler.getInstance();
		}
		
		public VertederoHandler getVertederoHandler(){
			return VertederoHandler.getInstance();
		}

	/*NOS QUEDA POR CORREGIR:
	 * BORRAR LOS CEROS DE LOS VECTORES SOLUCION (NO CONSIDERARLOS)
	 * EL LARGO DEL GENOMA SERIA EL LARGO DEL PEOR CASO
	 */
}
