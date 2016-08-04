package recoleccion.modelo.jornada;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

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
import recoleccion.solucion.Solucion.Viaje;

public class GeneradorGreedy {
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		File in_depositos = new File("depositos.txt");
	    File in_domicilios_jornada = new File("domicilios_jornada.txt");
	    File in_vertederos = new File("vertederos.txt");
	   
       try {
    	   //cargo vertedero
    	   VertederoHandler.getInstance().setVertederos(cargarVertedero(in_vertederos));
    	    	   
    	   //cargo depositos
    	   VehiculoHandler.getInstance().setVehiculos(cargarDepositos(in_depositos));
    	   
    	   //cargo domicilios con sus pedidos
    	   DomiciliosHandler.getInstance().setDomicilios(cargarDomicilios(in_domicilios_jornada));

    	   imprimir();
    	   
    	   Solucion sol=new Solucion();
    	   
    	   List<Viaje> viajes=new ArrayList<>();
    	   List<Domicilio> domicilios=sol.getDomiciliosSolucion();
    	   while (!domicilios.isEmpty()){
    		   Vehiculo vehiculoActual=sol.getRandomVehiculoSolucion();
    		   List<Domicilio> domiciliosValidos=vehiculoActual.domiciliosValidos(domicilios);
    		   List<Domicilio> domViajes=new ArrayList<>();
    		   while (!domiciliosValidos.isEmpty()){
    			   Domicilio domCercano=domicilioMasCerca(vehiculoActual,domiciliosValidos);
    			   domViajes.add(domCercano);
    			   
    			   if (vehiculoActual.puedeRecolectar(domCercano))
    				   vehiculoActual.recolectar(domCercano);
    			   else if (!vehiculoActual.isLleno())
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
    	   
    	   List<Vehiculo> vehiculos = sol.getVehiculosSolucion();
    	   double costo = 0;
    	   for (Vehiculo vehiculo : vehiculos) {
    		   vehiculo.imprimir();
    		   costo += vehiculo.getCostoJornada();
    	   }


    	   System.out.println("COSTO JORNADA: " + costo);

      } catch (Exception e) {
    	  System.out.println("ERROR CARGANDO PARAMETROS DE ENTRADA");
      }

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
	
	private static void imprimir() {
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
	private static List<Vehiculo> cargarDepositos(File f_depositos) throws FileNotFoundException  {
		   List<Deposito> depositos =new ArrayList<>();
		   
	       Scanner s = new Scanner(f_depositos);
	       Coordenada coord=new Coordenada();
	       int idVehiculo=0;
	       Deposito deposito=null;
	       List<Vehiculo> vehiculos=null;
	       List<Vehiculo> vehiculosJornada= new ArrayList<Vehiculo>();
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
	                       
	                       if (s.hasNextLine()){
	                           linea = s.nextLine(); 
	                           cadena=linea.split(",");
	                       }else{
	                           salir=true;
	                       }
	                       
	                  }
	                  
	                  deposito.setFlota(vehiculos);
	                  depositos.add(deposito);
	                  vehiculosJornada.addAll(vehiculos);
			   }
		   }
	       
	       s.close();
	       	
	       return vehiculosJornada;
		}

	   private static List<Domicilio> cargarDomicilios(File f_domicilios) throws FileNotFoundException {
		   Scanner s = new Scanner(f_domicilios);
		   int identificador=1;
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
	   
	   private static List<Vertedero> cargarVertedero(File f_vertederos) throws FileNotFoundException  {
		   Scanner s = new Scanner(f_vertederos);
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
	   
	   private static Set<TipoResiduo> calcularTipoResiduo(String linea){
		   Set<TipoResiduo> tiposResiduos=new HashSet<TipoResiduo>();
		   String[] cadena=linea.split(",");
		   int contTipos=2;
		   while (contTipos<cadena.length){
			   tiposResiduos.add(TipoResiduo.valueOf(cadena[contTipos]));
	           contTipos++;
		   }
		   return tiposResiduos;
		   
	   }

}
