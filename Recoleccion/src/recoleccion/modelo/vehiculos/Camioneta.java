package recoleccion.modelo.vehiculos;

public class Camioneta extends Vehiculo {

	public Camioneta(int id) {
		super(id);
	}

	@Override
	protected TipoVehiculo getTipo() {
		return TipoVehiculo.CAMIONETA;
	}

}
