package recoleccion.modelo.vehiculos;

public class Camion extends Vehiculo {

	
	public Camion(int id) {
		super(id);
	}

	public Camion(Vehiculo v) {
		super(v);
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
	
	public double getTiempoDescarga(){
		return 0.01/60;  
	}

	@Override
	protected TipoVehiculo getTipoVehiculo() {
		return TipoVehiculo.CAMION;
	}
	
	
}
