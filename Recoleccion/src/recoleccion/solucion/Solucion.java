package recoleccion.solucion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;

import recoleccion.ecj.IntegerVectorIndividualRecoleccion;
import recoleccion.modelo.domicilios.Domicilio;
import recoleccion.modelo.domicilios.DomiciliosHandler;
import recoleccion.modelo.jornada.VertederoHandler;
import recoleccion.modelo.vehiculos.Vehiculo;
import recoleccion.modelo.vehiculos.VehiculoHandler;

public class Solucion {
	
	private List<Viaje> viajes;
	
	private List<Domicilio> domiciliosSolucion;
	
	private List<Vehiculo> vehiculosSolucion;
	
	public List<Viaje> getViajes() {
		return viajes;
	}

	public void setViajes(List<Viaje> viajes) {
		this.viajes = viajes;
	}

	public List<Domicilio> getDomiciliosSolucion() {
		return domiciliosSolucion;
	}

	public void setDomiciliosSolucion(List<Domicilio> domiciliosSolucion) {
		this.domiciliosSolucion = domiciliosSolucion;
	}

	public List<Vehiculo> getVehiculosSolucion() {
		return vehiculosSolucion;
	}

	public void setVehiculosSolucion(List<Vehiculo> vehiculosSolucion) {
		this.vehiculosSolucion = vehiculosSolucion;
	}
	
	public Solucion(IntegerVectorIndividualRecoleccion ind) throws Exception {
		this();
		
		this.viajes = new ArrayList<Viaje>();
		int[][] viajesInd = ind.getViajesIndividuo();
		for (int i = 0; i < viajesInd.length; i++) {
			if (viajesInd[i].length > 0){
				Viaje v = new Viaje(viajesInd[i]);
				this.viajes.add(v);
			}
		}		
		//System.out.println(ind.genotypeToStringForHumans());
	}
	
	public Solucion() {
		this.setDomiciliosSolucion(DomiciliosHandler.getInstance().getDomicilios());
		this.setVehiculosSolucion(VehiculoHandler.getInstance().getVehiculos());
	}

	// viajes sin domicilio se dejan en la solucion tiene fitness 0
	public double fitness(){
		double fitness = 0;
		List<Vehiculo> vehiculosUtilizados = new ArrayList<Vehiculo>();
		
		if (CollectionUtils.isNotEmpty(viajes)){
			for (Viaje viaje : viajes) {
				viaje.doViaje();
				if (!vehiculosUtilizados.contains(viaje.getVehiculo()))
					vehiculosUtilizados.add(viaje.getVehiculo());
			}
		}
		
		for (Vehiculo vehiculo : vehiculosUtilizados){
			fitness += vehiculo.getCostoJornada();
		}		
		return fitness;
	}
	
	public void imprimir(){
		if (CollectionUtils.isNotEmpty(viajes)){
			for (Viaje viaje : viajes) {
				viaje.imprimir();
			}
		}
	}
	
	public void corregir(List<Vehiculo> vehiculosUtilizados){
	
		for (Viaje viaje : viajes) {
			List<Viaje> viajesCorregidos=new ArrayList<>();
			viajesCorregidos=viaje.corregir(vehiculosUtilizados);
			for (Viaje viajeCorregido : viajesCorregidos){
				viajes.add(viajeCorregido);
			}
		}
		setViajes(viajes);
	}
	
	private Vehiculo getVehiculoSolucion(int id) throws Exception{
		for (Vehiculo vehiculo : vehiculosSolucion) {
			if (vehiculo.getIdentificador().equals(String.valueOf(id)))
				return vehiculo;
		}
		throw new Exception("NO ENCONTRO VEHICULO: " + id);
		
	}
	
	private Vehiculo getRandomVehiculoSolucion() {
		try {
	        return vehiculosSolucion.get((new Random()).nextInt(vehiculosSolucion.size()));
	    }
	    catch (Throwable e){
	        return null;
	    }
	}
	
	private Domicilio getDomicilioSolucion(int id) {
		Domicilio d = new Domicilio(id);
		try {
			return domiciliosSolucion.get(domiciliosSolucion.indexOf(d));
		} catch (Exception e) {
			System.out.println("ERROR BUSCANDO DOMICILIO: " + id);
		}
		
		return null;
	}
	
	public class Viaje {
		
		private Vehiculo vehiculo;
		
		private List<Domicilio> domicilios;
		
		public Viaje(int[] dataViaje) throws Exception {
			// seteoVehiculo
			this.vehiculo = dataViaje[0] < 0 ? 
					getVehiculoSolucion(dataViaje[0]) :
					getRandomVehiculoSolucion();
			
			// seteo domicilios
			// inicializa i dependiendo si vino vehiculo o no
			this.domicilios = new ArrayList<Domicilio>();
			for (int i = dataViaje[0] < 0 ? 1 : 0; i<dataViaje.length;i++){
				if (dataViaje[i] != 0){
					Domicilio d = getDomicilioSolucion(dataViaje[i]);
					this.domicilios.add(d);
				}
			}
		}

		public Viaje(){
			vehiculo = null;
			domicilios = null;
		}
		
		public Vehiculo getVehiculo() {
			return vehiculo;
		}

		public void setVehiculo(Vehiculo vehiculo) {
			this.vehiculo = vehiculo;
		}

		public List<Domicilio> getDomicilios() {
			return domicilios;
		}

		public void setDomicilios(List<Domicilio> domicilios) {
			this.domicilios = domicilios;
		}

		public void imprimir() {
			if (vehiculo != null)
				vehiculo.imprimir();
			else System.out.println("VIAJE SIN VEHICULO");
			
			if (CollectionUtils.isNotEmpty(domicilios)){
				for (Domicilio domicilio : domicilios) {
					domicilio.imprimir();
				}
			}
			
		}

		public void doViaje() {
			if (CollectionUtils.isNotEmpty(domicilios)){
				for (Domicilio domicilio: domicilios) {
					vehiculo.recolectar(domicilio);
				}
				vehiculo.verter(VertederoHandler.getInstance().get(vehiculo));
			}
		}
		
		public List<Viaje> corregir(List<Vehiculo> vehiculosUtilizados){
			//boolean valido=false;
			//int i=0;
			//Long carga=vehiculo.getCarga();
			//int cantDom=this.getDomicilios().size();
			List<Viaje> viajesCorregidos=new ArrayList<>();
			eliminarDomiciliosRepetidos();
			viajesCorregidos=agregarViajesExcedeCarga(vehiculosUtilizados);
			return viajesCorregidos;
			
		}
		
		private void eliminarDomiciliosRepetidos(){
			
		}
		
		private List<Viaje> agregarViajesExcedeCarga(List<Vehiculo> domicilios){
			return null;
		}
	}

	
}
