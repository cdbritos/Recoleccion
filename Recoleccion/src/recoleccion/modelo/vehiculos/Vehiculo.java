<<<<<<< refs/remotes/origin/master
package recoleccion.modelo.vehiculos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import recoleccion.modelo.data.Coordenable;
import recoleccion.modelo.data.Coordenada;
import recoleccion.modelo.data.TipoResiduo;
import recoleccion.modelo.domicilios.Domicilio;
import recoleccion.modelo.jornada.Jornada;
import recoleccion.modelo.jornada.Vertedero;

public abstract class Vehiculo extends Coordenable {
	
	//identificador del vehiculo
	private String identificador;
	
	//carga en m3 del vehiculo
	private Long carga;

	//metros recorridos en la jornada
	private double metrosRecorridos;
	
	//tiempo consumido en recolectar
	private double tiempoRecolectando;

	//tiempo consumido en vertir
	private double tiempoVertiendo;

	//tipos residuos que puede recolectar el vehiculo
	private Set<TipoResiduo> tiposResiduos;
	
	public abstract Long getCapacidad();

	public abstract Long getVelocidad();

	public abstract Long getRendimiento();
	
	public abstract Long getPrecioCombustible();
	
	public abstract int getEmpleados();

	public abstract double getTiempoDescarga(); //en segundos
	
	protected abstract double getTiempoCarga(); // en segundos
	
	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Set<TipoResiduo> getTiposResiduos() {
		return tiposResiduos;
	}

	public void setTiposResiduos(Set<TipoResiduo> tiposResiduos) {
		this.tiposResiduos = tiposResiduos;
	}

	public void imprimir() {
		
		System.out.println("Vehiculo: " + getTipo() + " " + this.identificador + "," + tiposResiduos + " --> KM: " + (metrosRecorridos/1000) 
				+ " -- Duracion Jornada " + getDuracionJornada() + " -- Costo Jornada " + getCostoJornada() + "-- CC: " + getCostoCombustible());
		super.imprimir();
	}
	
	public Vehiculo(){
		metrosRecorridos = 0L;
		carga = 0L;
		tiempoRecolectando = 0L;
		tiempoVertiendo = 0L;
	}
	
	public Vehiculo(int id){
		this();
		this.identificador = String.valueOf(id);
	}
	
	public Vehiculo(Vehiculo v){
		this(Integer.valueOf(v.identificador).intValue());
		this.setCoordenadas(v.getCoordenadas());
		this.tiposResiduos = v.getTiposResiduos();
	}
	
	protected abstract String getTipo();
	protected abstract TipoVehiculo getTipoVehiculo();
	
	public Long getCarga() {
		return carga;
	}

	public void setCarga(Long carga) {
		this.carga = carga;
	}
	
	//TODO: la estrategia de recoleccion impacta sobre la solucion
	public void recolectar(Domicilio domicilio){	
		
		this.setCoordenadas(domicilio.getCoordenadas());
		
		for (TipoResiduo tr : this.tiposResiduos) {
			if (isLleno())
				break;
			
			long cantResiduo = domicilio.tieneResiduo(tr);
			if (cantResiduo > 0 ){
				long cantARecolectar = capacidadActual() > cantResiduo ? cantResiduo : capacidadActual();
				domicilio.recolectar(tr,cantARecolectar);
				tiempoRecolectando += cantARecolectar * getTiempoCarga();
				carga += cantARecolectar;
				
			}
			
		}
	}

	public void verter(Vertedero vertedero){
		this.setCoordenadas(vertedero.getCoordenadas());
		tiempoVertiendo += this.carga * getTiempoDescarga();
		carga = 0L;	
	}
	
	public boolean isLleno(){
		return  capacidadActual() == 0;
	}
	
	public long capacidadActual(){
		return getCapacidad() - carga;
	}
	
	@Override
	public void setCoordenadas(Coordenada coordenadas) {
		if (getCoordenadas() != null)
			metrosRecorridos += distance(coordenadas);
				
		super.setCoordenadas(coordenadas);
	}
	
	// retorna duracion en horas
	private double getDuracionJornada(){
		return (metrosRecorridos / 1000) / getVelocidad() + tiempoRecolectando/3600 + tiempoVertiendo/3600;
	}
	
	private double getCostoCombustible(){
		double litrosConsumidos = (metrosRecorridos / 1000) / getRendimiento();
		return litrosConsumidos * getPrecioCombustible();
	}
	
	public double getCostoJornada(){
		double costo = 0;
		if (metrosRecorridos > 0){
			costo += getCostoCombustible();
			costo += getCostoFijo();
			
			if (getDuracionJornada() > Jornada.DURACION_JORNAL_HORAS){
				long horasExtras = Math.round(Math.ceil(getDuracionJornada() - Jornada.DURACION_JORNAL_HORAS));
				costo += horasExtras * Jornada.COSTO_HORA_EXTRA * getEmpleados();
				costo += Jornada.DURACION_JORNAL_HORAS * Jornada.COSTO_HORA_POR_EMPLEADO * getEmpleados();
			}else
				costo += this.getDuracionJornada() * Jornada.COSTO_HORA_POR_EMPLEADO * getEmpleados();
		}
		return costo;
	}
	
	protected abstract double getCostoFijo();

	//Dada la lista de domicilios retorna una lista de 
	//Domicilios validos para el vehiculo, es decir que el vehiculo puede ir a recolectar algo
	//ordenada por distancia
	public List<Domicilio> domiciliosValidos(List<Domicilio> totalDomicilios){
		//Busca domicilios que vehiculo tenga algo para recolectar
		List<Domicilio> domValidos=new ArrayList<>();
		for (int i=0;i<totalDomicilios.size();i++){			
			Domicilio dom=totalDomicilios.get(i);
			if (this.puedeRecolectar(dom)){
				domValidos.add(dom);
			}
		}
		//los ordena de forma de hacer el viaje mas corto
//		List<Domicilio> domValidosOrdenados = new ArrayList<>();
//		for (Object o : Coordenable.sortFromOrigen(domValidos, this)) {
//			Domicilio d = (Domicilio) o;
//			domValidosOrdenados.add(d);
//		}
		
		return domValidos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identificador == null) ? 0 : identificador.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vehiculo other = (Vehiculo) obj;
		if (identificador == null) {
			if (other.identificador != null)
				return false;
		} else if (!identificador.equals(other.identificador))
			return false;
		return true;
	}

	public boolean puedeRecolectar(Domicilio domicilio) {
		boolean puede = false;
		for (TipoResiduo tr : tiposResiduos) {
			if (domicilio.tieneResiduo(tr) > 0)
				return true;
		}
		
		return puede;
	}

	public static Vehiculo getVehiculoByTipo(TipoVehiculo tv){
		if (TipoVehiculo.CAMION.equals(tv))
			return new Camion(0);
		else
			return new Camioneta(0); 
	}
	
	public static int getRandomCapacidadTope() {
		int tv = (new Random().nextInt(TipoVehiculo.values().length));
		Vehiculo v = getVehiculoByTipo(TipoVehiculo.values()[tv]);
		return  v.getCapacidad().intValue();
	}

	public boolean recolectaLoMismo(Vehiculo otro) {
		
		if (this.tiposResiduos.size() != otro.tiposResiduos.size())
			return false;
		
		for (TipoResiduo tr : this.tiposResiduos) {
			if (!otro.tiposResiduos.contains(tr))
				return false;
		}
		
		return true;
	}

	public boolean esMejor(Vehiculo otro) {
		return this.recolectaLoMismo(otro) && this.tiposResiduos.size() > otro.tiposResiduos.size();
	}

	
}
=======
package recoleccion.modelo.vehiculos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import recoleccion.modelo.data.Coordenable;
import recoleccion.modelo.data.Coordenada;
import recoleccion.modelo.data.TipoResiduo;
import recoleccion.modelo.domicilios.Domicilio;
import recoleccion.modelo.jornada.Jornada;
import recoleccion.modelo.jornada.Vertedero;

public abstract class Vehiculo extends Coordenable {
	
	//identificador del vehiculo
	private String identificador;
	
	//carga en m3 del vehiculo
	private Long carga;

	//metros recorridos en la jornada
	private double metrosRecorridos;
	
	//tiempo consumido en recolectar
	private double tiempoRecolectando;

	//tiempo consumido en vertir
	private double tiempoVertiendo;

	//tipos residuos que puede recolectar el vehiculo
	private Set<TipoResiduo> tiposResiduos;
	
	public abstract Long getCapacidad();

	public abstract Long getVelocidad();

	public abstract Long getRendimiento();
	
	public abstract Long getPrecioCombustible();
	
	public abstract int getEmpleados();

	public abstract double getTiempoDescarga(); //en segundos
	
	protected abstract double getTiempoCarga(); // en segundos
	
	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Set<TipoResiduo> getTiposResiduos() {
		return tiposResiduos;
	}

	public void setTiposResiduos(Set<TipoResiduo> tiposResiduos) {
		this.tiposResiduos = tiposResiduos;
	}

	public void imprimir() {
		
		System.out.println("Vehiculo: " + getTipo() + " " + this.identificador + "," + tiposResiduos + " --> KM: " + (metrosRecorridos/1000) 
				+ " -- Duracion Jornada " + getDuracionJornada() + " -- Costo Jornada " + getCostoJornada() + "-- CC: " + getCostoCombustible());
		super.imprimir();
	}
	
	public Vehiculo(){
		metrosRecorridos = 0L;
		carga = 0L;
		tiempoRecolectando = 0L;
		tiempoVertiendo = 0L;
	}
	
	public Vehiculo(int id){
		this();
		this.identificador = String.valueOf(id);
	}
	
	public Vehiculo(Vehiculo v){
		this(Integer.valueOf(v.identificador).intValue());
		this.setCoordenadas(v.getCoordenadas());
		this.tiposResiduos = v.getTiposResiduos();
	}
	
	protected abstract String getTipo();
	protected abstract TipoVehiculo getTipoVehiculo();
	
	public Long getCarga() {
		return carga;
	}

	public void setCarga(Long carga) {
		this.carga = carga;
	}
	
	//TODO: la estrategia de recoleccion impacta sobre la solucion
	public void recolectar(Domicilio domicilio){	
		
		this.setCoordenadas(domicilio.getCoordenadas());
		
		for (TipoResiduo tr : this.tiposResiduos) {
			if (isLleno())
				break;
			
			long cantResiduo = domicilio.tieneResiduo(tr);
			if (cantResiduo > 0 ){
				long cantARecolectar = capacidadActual() > cantResiduo ? cantResiduo : capacidadActual();
				domicilio.recolectar(tr,cantARecolectar);
				tiempoRecolectando += cantARecolectar * getTiempoCarga();
				carga += cantARecolectar;
				
			}
			
		}
	}

	public void verter(Vertedero vertedero){
		this.setCoordenadas(vertedero.getCoordenadas());
		tiempoVertiendo += this.carga * getTiempoDescarga();
		carga = 0L;	
	}
	
	public boolean isLleno(){
		return  capacidadActual() == 0;
	}
	
	public long capacidadActual(){
		return getCapacidad() - carga;
	}
	
	@Override
	public void setCoordenadas(Coordenada coordenadas) {
		if (getCoordenadas() != null)
			metrosRecorridos += distance(coordenadas);
				
		super.setCoordenadas(coordenadas);
	}
	
	// retorna duracion en horas
	private double getDuracionJornada(){
		return (metrosRecorridos / 1000) / getVelocidad() + tiempoRecolectando/3600 + tiempoVertiendo/3600;
	}
	
	private double getCostoCombustible(){
		double litrosConsumidos = (metrosRecorridos / 1000) / getRendimiento();
		return litrosConsumidos * getPrecioCombustible();
	}
	
	public double getCostoJornada(){
		double costo = 0;
		if (metrosRecorridos > 0){
			costo += getCostoCombustible();
			costo += getCostoFijo();
			
			if (getDuracionJornada() > Jornada.DURACION_JORNAL_HORAS){
				long horasExtras = Math.round(Math.ceil(getDuracionJornada() - Jornada.DURACION_JORNAL_HORAS));
				costo += horasExtras * Jornada.COSTO_HORA_EXTRA * getEmpleados();
				costo += Jornada.DURACION_JORNAL_HORAS * Jornada.COSTO_HORA_POR_EMPLEADO * getEmpleados();
			}else
				costo += this.getDuracionJornada() * Jornada.COSTO_HORA_POR_EMPLEADO * getEmpleados();
		}
		return costo;
	}
	
	protected abstract double getCostoFijo();

	//Dada la lista de domicilios retorna una lista de 
	//Domicilios validos para el vehiculo, es decir que el vehiculo puede ir a recolectar algo
	//ordenada por distancia
	public List<Domicilio> domiciliosValidos(List<Domicilio> totalDomicilios){
		//Busca domicilios que vehiculo tenga algo para recolectar
		List<Domicilio> domValidos=new ArrayList<>();
		for (int i=0;i<totalDomicilios.size();i++){			
			Domicilio dom=totalDomicilios.get(i);
			if (this.puedeRecolectar(dom)){
				domValidos.add(dom);
			}
		}
		//los ordena de forma de hacer el viaje mas corto
//		List<Domicilio> domValidosOrdenados = new ArrayList<>();
//		for (Object o : Coordenable.sortFromOrigen(domValidos, this)) {
//			Domicilio d = (Domicilio) o;
//			domValidosOrdenados.add(d);
//		}
		
		return domValidos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identificador == null) ? 0 : identificador.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vehiculo other = (Vehiculo) obj;
		if (identificador == null) {
			if (other.identificador != null)
				return false;
		} else if (!identificador.equals(other.identificador))
			return false;
		return true;
	}

	public boolean puedeRecolectar(Domicilio domicilio) {
		boolean puede = false;
		for (TipoResiduo tr : tiposResiduos) {
			if (domicilio.tieneResiduo(tr) > 0)
				return true;
		}
		
		return puede;
	}

	public static Vehiculo getVehiculoByTipo(TipoVehiculo tv){
		if (TipoVehiculo.CAMION.equals(tv))
			return new Camion(0);
		else
			return new Camioneta(0); 
	}
	
	public static int getRandomCapacidadTope() {
		int tv = (new Random().nextInt(TipoVehiculo.values().length));
		Vehiculo v = getVehiculoByTipo(TipoVehiculo.values()[tv]);
		return  v.getCapacidad().intValue();
	}

	public boolean recolectaLoMismo(Vehiculo otro) {
		
		if (this.tiposResiduos.size() != otro.tiposResiduos.size())
			return false;
		
		for (TipoResiduo tr : this.tiposResiduos) {
			if (!otro.tiposResiduos.contains(tr))
				return false;
		}
		
		return true;
	}

	public boolean esMejor(Vehiculo otro) {
		return this.recolectaLoMismo(otro) && this.tiposResiduos.size() > otro.tiposResiduos.size();
	}

	
}
>>>>>>> Ahora funca mejor
