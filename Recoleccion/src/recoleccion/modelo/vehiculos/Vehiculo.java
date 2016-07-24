package recoleccion.modelo.vehiculos;

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

	public abstract double getTiempoDescarga();
	
	protected double getTiempoCarga(){
		return 0.1/60; // 0,1 minuto demora en cargar una unidad de residuo
	}
	
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
		
		System.out.println("Vehiculo: " + getTipo() + " " + this.identificador + "," + tiposResiduos);
		
	}
	
	public Vehiculo(){
		metrosRecorridos = 0;
		carga = 0L;
		tiempoRecolectando = 0L;
		tiempoVertiendo = 0L;
	}
	
	public Vehiculo(int id){
		this();
		this.identificador = String.valueOf(id);
	}
	
	protected abstract String getTipo();

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
			long cantResiduo = domicilio.tieneResiduo(tr);
			if (cantResiduo > 0 ){
				long cantARecolectar = capacidadActual() > cantResiduo ? cantResiduo : capacidadActual();
				domicilio.recolectar(tr,cantARecolectar);
				tiempoRecolectando += cantARecolectar * getTiempoCarga();
				carga += cantARecolectar;
				if (isLleno())
					break;
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
		metrosRecorridos += distance(coordenadas);
		super.setCoordenadas(coordenadas);
	}
	
	// retorna duracion en horas
	private double getDuracionJornada(){
		return (metrosRecorridos / 1000) / getVelocidad() + tiempoRecolectando + tiempoVertiendo;
	}
	
	private double getCostoCombustible(){
		double litrosConsumidos = (metrosRecorridos / 1000) / getRendimiento();
		return litrosConsumidos * getPrecioCombustible();
	}
	
	public double getCostoJornada(){
		double costo = 0;
		if (metrosRecorridos > 0){
			//costo fijo
			costo += Jornada.COSTO_JORNAL_POR_EMPLEADO * getEmpleados();
			costo += getCostoCombustible(); 
			if (getDuracionJornada() > Jornada.DURACION_JORNAL_HORAS){
				long horasExtras = Math.round(Math.ceil(getDuracionJornada() - Jornada.DURACION_JORNAL_HORAS));
				costo += horasExtras * Jornada.COSTO_HORA_EXTRA * getEmpleados();
			}
		}
		return costo;
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

	
}
