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
	public Long getCapacidad() {
		return 2000L;
	}


	@Override
	public Long getVelocidad() {
		return 40L;
	}


	@Override
	public Long getRendimiento() {
		return 5L;
	}


	@Override
	public Long getPrecioCombustible() {
		return 38L;
	}

	@Override
	public int getEmpleados() {
		return 3;
	}
	
	
}
