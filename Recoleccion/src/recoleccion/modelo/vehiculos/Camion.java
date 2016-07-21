package recoleccion.modelo.vehiculos;

public class Camion extends Vehiculo {

	public Camion(int id) {
		super(id);
	}

	@Override
	protected TipoVehiculo getTipo() {
		return TipoVehiculo.CAMION;
	}
	
	
}
