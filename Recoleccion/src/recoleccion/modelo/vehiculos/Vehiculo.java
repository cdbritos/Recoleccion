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

	//tipos residuos que puede recolectar el vehiculo
	private Set<TipoResiduo> tiposResiduos;
	
	public abstract Long getCapacidad();

	public abstract Long getVelocidad();

	public abstract Long getRendimiento();
	
	public abstract Long getPrecioCombustible();
	
	public abstract int getEmpleados();

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
				carga += cantARecolectar;
				if (isLleno())
					break;
			}
			
		}
	}

	public void verter(Vertedero vertedero){
		this.setCoordenadas(vertedero.getCoordenadas());
		carga = 0L;	
	}
	
	public boolean isLleno(){
		return  capacidadActual() == 0;
	}
	
	public long capacidadActual(){
		return getCapacidad() - carga;
	}

	public double getMetrosRecorridos() {
		return metrosRecorridos;
	}

	public void setMetrosRecorridos(double metrosRecorridos) {
		this.metrosRecorridos = metrosRecorridos;
	}
	
	@Override
	public void setCoordenadas(Coordenada coordenadas) {
		metrosRecorridos += distance(coordenadas);
		super.setCoordenadas(coordenadas);
	}
	
	// retorna duracion en horas
	public double getDuracionJornada(){
		return (metrosRecorridos / 1000) / getVelocidad();
	}
	
	public double getCostoCombustible(){
		double litrosConsumidos = (metrosRecorridos / 1000) / getRendimiento();
		return litrosConsumidos * getPrecioCombustible();
	}
	
	public double getCostoJornada(){
		double costo = 0;
		if (metrosRecorridos > 0){
			//costo fijo
			costo = Jornada.COSTO_JORNAL_POR_EMPLEADO * getEmpleados();
			if (getDuracionJornada() > Jornada.DURACION_JORNAL_HORAS){
				long horasExtras = Math.round(Math.ceil(getDuracionJornada() - Jornada.DURACION_JORNAL_HORAS));
				costo += horasExtras * Jornada.COSTO_HORA_EXTRA;
			}
		}
		return costo;
	}

	
}
