package recoleccion.modelo.vehiculos;

import java.util.Set;

import recoleccion.modelo.data.Coordenable;
import recoleccion.modelo.data.TipoResiduo;
import recoleccion.modelo.domicilios.Domicilio;

public abstract class Vehiculo extends Coordenable {
	
	private static final double UMBRAL_LLENO = 0.9; 

	//identificador del vehiculo
	private String identificador;
	
	//capacidad en m3 del vehiculo
	private Long capacidad;
	
	//carga en m3 del vehiculo
	private Long carga;

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

	public void imprimir() {
		
		System.out.println("Vehiculo: " + getTipo() + " " + this.identificador + "," + tiposResiduos);
		
	}
	
	public Vehiculo(){
		carga = 0L;
		inicializar();
	}
	
	public Vehiculo(int id){
		this();
		this.identificador = String.valueOf(id);
	}
	
	protected abstract void inicializar();
	
	protected abstract String getTipo();

	public Long getCarga() {
		return carga;
	}

	public void setCarga(Long carga) {
		this.carga = carga;
	}
	
	//TODO: la estrategia de recoleccion impacta sobre la solucion
	public void recolectar(Domicilio domicilio){	
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

	public boolean isLleno(){
		return  capacidadActual() == 0;
	}
	
	public long capacidadActual(){
		return capacidad - carga;
	}
	
}
