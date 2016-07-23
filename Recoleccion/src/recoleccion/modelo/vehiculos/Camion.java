package recoleccion.modelo.vehiculos;

public class Camion extends Vehiculo {

	
	public Camion(int id) {
		super(id);
	}


	@Override
	protected String getTipo() {
		return TipoVehiculo.CAMION.name();
	}

	@Override
	protected void inicializar() {
		this.setCapacidad(2000L);
		this.setRendimiento(5L);
		this.setVelocidad(40L);
	}
	
	
}
