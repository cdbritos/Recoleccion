package recoleccion.modelo.vehiculos;

public class Camion extends Vehiculo {

	public Camion(int id) {
		super(id);
	}


	@Override
	protected TipoVehiculo getTipo() {
		return TipoVehiculo.CAMION;
	}

	@Override
	protected void inicializar() {
		this.setCapacidad(2000L);
		this.setRendimiento(8L);
		this.setVelocidad(40L);
	}
	
	
}
