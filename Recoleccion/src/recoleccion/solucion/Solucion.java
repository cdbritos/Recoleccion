package recoleccion.solucion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

import recoleccion.ecj.IntegerVectorIndividualRecoleccion;
import recoleccion.modelo.domicilios.Domicilio;
import recoleccion.modelo.domicilios.DomiciliosHandler;
import recoleccion.modelo.jornada.Jornada;
import recoleccion.modelo.jornada.VertederoHandler;
import recoleccion.modelo.vehiculos.Vehiculo;
import recoleccion.modelo.vehiculos.VehiculoHandler;

public class Solucion {
	
	private List<Viaje> viajes;
	
	private List<Domicilio> domiciliosSolucion;
	
	private List<Vehiculo> vehiculosSolucion;
	
	private IntegerVectorIndividualRecoleccion individuo;
	
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
		this.individuo = ind;
		
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
		this.viajes = new ArrayList<Viaje>();
		this.setDomiciliosSolucion(DomiciliosHandler.getInstance().getDomicilios());
		this.setVehiculosSolucion(VehiculoHandler.getInstance().getVehiculos());
	}

	// viajes sin domicilio se dejan en la solucion tiene fitness 0
	public double fitness(){
		double fitness = 0;
		
		individuo.genome = new int[0];
		//Collections.shuffle(viajes);
		
		if (CollectionUtils.isNotEmpty(viajes)){
			for (Viaje viaje : viajes) {
				viaje.doViaje();
				individuo.genome = ArrayUtils.addAll(individuo.genome, viaje.getGenomaViaje());
			}
		}
		
		/*List<Domicilio> domiciliofaltantes = getDomiciliosFaltantes();
		while (CollectionUtils.isNotEmpty(domiciliofaltantes)){
			Vehiculo v = getRandomVehiculoSolucion();
			List<Domicilio> domiciliosValidosVehiculo = v.domiciliosValidos(domiciliofaltantes);
			if (CollectionUtils.isNotEmpty(domiciliosValidosVehiculo)){
				Viaje viaje = this.new Viaje(v,domiciliosValidosVehiculo);
				viaje.doViaje();
				
				if (CollectionUtils.isNotEmpty(viaje.domicilios)){
					this.viajes.add(viaje);
					individuo.genome = ArrayUtils.addAll(individuo.genome, viaje.getGenomaViaje());
				}
			}
			
			domiciliofaltantes = getDomiciliosFaltantes();
		}*/
		
		//COMIENZO - AGREGUE ESTO PARA CORREGIR LOS VEHICULOS
		List<Domicilio> domiciliofaltantes = getDomiciliosFaltantes();
		if (CollectionUtils.isNotEmpty(domiciliofaltantes)){
			List<Domicilio> domiciliosValidosVehiculo=new ArrayList<>();
			for (Viaje viaje : this.viajes){
				Vehiculo vActual=viaje.getVehiculo();
				int j=0;
				while (j<domiciliofaltantes.size()){
					Domicilio domActual=domiciliofaltantes.get(j);
					if (!vActual.isLleno() &&  vActual.puedeRecolectar(domActual)){
							domiciliosValidosVehiculo.add(domActual);
							domiciliofaltantes.remove(domActual);
					}
					j++;
				}
				//viaje.setDomicilios((List<Domicilio>) ListUtils.union(domiciliosValidosVehiculo,viaje.getDomicilios()));
				if (!domiciliosValidosVehiculo.isEmpty()){
					List<Domicilio> domiciliosTotal=new ArrayList<>();
					domiciliosTotal.addAll(domiciliosValidosVehiculo);
					domiciliosTotal.addAll(viaje.getDomicilios());
					viaje.setDomicilios(domiciliosTotal);
					viaje.doViaje();
					individuo.genome = ArrayUtils.addAll(individuo.genome, viaje.getGenomaViaje());
				}
			}
			
			
			while (CollectionUtils.isNotEmpty(domiciliofaltantes)){
				Vehiculo v = getRandomVehiculoSolucion();
				domiciliosValidosVehiculo = v.domiciliosValidos(domiciliofaltantes);
				if (CollectionUtils.isNotEmpty(domiciliosValidosVehiculo)){
					Viaje viaje = this.new Viaje(v,domiciliosValidosVehiculo);
					viaje.doViaje();
					
					if (CollectionUtils.isNotEmpty(viaje.domicilios)){
						this.viajes.add(viaje);
						individuo.genome = ArrayUtils.addAll(individuo.genome, viaje.getGenomaViaje());
					}
				}
				
				domiciliofaltantes = getDomiciliosFaltantes();
			}
		}
		//FIN - AGREGUE ESTO PARA CORREGIR LOS VEHICULOS
		
				
		for (Vehiculo vehiculo : vehiculosSolucion){
			fitness += vehiculo.getCostoJornada();
		}
		
		return fitness;
	}
	
	public List<Domicilio> getDomiciliosFaltantes() {
		List<Domicilio> faltantes = new ArrayList<Domicilio>();
		for (Domicilio domicilio : domiciliosSolucion) {
			if (domicilio.tieneResiduo())
				faltantes.add(domicilio);
		}
		return faltantes;
	}

	private long getTotalResiduosRestantes() {
		long faltante = 0;
		for (Domicilio domicilio : domiciliosSolucion) {
			faltante += domicilio.getFaltante();
		}
		return faltante;
	}

	public void imprimir(){
		if (CollectionUtils.isNotEmpty(viajes)){
			for (Viaje viaje : viajes) {
				viaje.imprimir();
			}
		}
		System.out.println("------DOMICILIOS FINAL------------------------");
		if (CollectionUtils.isNotEmpty(DomiciliosHandler.getInstance().getDomicilios())){
			for (Domicilio d : DomiciliosHandler.getInstance().getDomicilios()) {
				d.imprimir();
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
	
	public Vehiculo getRandomVehiculoSolucion() {
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
		
		public Viaje(Vehiculo v, List<Domicilio> doms) {
			this.vehiculo = v;
			this.domicilios = doms;
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

		public int[] getGenomaViaje(){
			if (domicilios.size() == 0)
				return new int[0];
			
			int[] genomaViaje = new int[domicilios.size()+1];
			genomaViaje[0] = Integer.valueOf(vehiculo.getIdentificador()).intValue();
			for (int i = 1; i <= domicilios.size(); i++)
				genomaViaje[i] = Integer.valueOf(domicilios.get(i-1).getIdentificador()).intValue();
			
			return genomaViaje;
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
				List<Domicilio> domiciliosRecolectados = new ArrayList<Domicilio>();
				for (Domicilio domicilio: domicilios) {
					if (vehiculo.isLleno())
						break;
					
					if (vehiculo.puedeRecolectar(domicilio)){
						vehiculo.recolectar(domicilio);
						domiciliosRecolectados.add(domicilio);
					}
				}
				
				if (vehiculo.getCarga() > 0)
					vehiculo.verter(VertederoHandler.getInstance().get(vehiculo));
				
				this.setDomicilios(domiciliosRecolectados);
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

	public void setGenoma(IntegerVectorIndividualRecoleccion ind) {
		int posGenoma=0;  
		ind.genome = new int[Jornada.getInstance().getGenomeSize()];
		for (int i=0;i<this.getViajes().size();i++){
           ind.genome[posGenoma]=Integer.parseInt(this.getViajes().get(i).getVehiculo().getIdentificador());  
           for (int j=0;j<this.getViajes().get(i).getDomicilios().size();j++){  
               posGenoma++;  
               ind.genome[posGenoma]=Integer.parseInt(this.getViajes().get(i).getDomicilios().get(j).getIdentificador());  
           }
           posGenoma++;
       }   
	}

	
}
