package recoleccion.modelo.vehiculos;

import java.util.Set;

import recoleccion.modelo.data.Coordenable;
import recoleccion.modelo.data.TipoResiduo;

public abstract class Vehiculo extends Coordenable {

	//identificador del vehiculo
	private String identificador;
	
	//capacidad en m3 del vehiculo
	private Long capacidad;

	//velocidad promedio del vehiculo
	private Long velocidad;
		
	//rendimiento en km por litro
	private Long rendimiento;

	//tipos residuos que puede recolectar el vehiculo
	private Set<TipoResiduo> tiposResiduos;
	
	public Long getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(Long capacidad) {
		this.capacidad = capacidad;
	}

	public Long getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(Long velocidad) {
		this.velocidad = velocidad;
	}

	public Long getRendimiento() {
		return rendimiento;
	}

	public void setRendimiento(Long rendimiento) {
		this.rendimiento = rendimiento;
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
	
	
}
