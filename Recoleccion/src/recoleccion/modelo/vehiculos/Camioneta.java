package recoleccion.modelo.vehiculos;

public class Camioneta extends Vehiculo {

	public Camioneta(int id) {
		super(id);
	}

	public Camioneta(Vehiculo v) {
		super(v);
	}

	@Override
	protected String getTipo() {
		return TipoVehiculo.CAMIONETA.name();
	}

	@Override
	public Long getCapacidad() {
		return 800L;
	}


	@Override
	public Long getVelocidad() {
		return 70L;
	}


	@Override
	public Long getRendimiento() {
		return 14L;
	}


	@Override
	public Long getPrecioCombustible() {
		return 40L;
	}

	@Override
	public int getEmpleados() {
		return 2;
	}

	@Override
	public double getTiempoDescarga() {
		return 0.1/60; //0,01 minutos demora en descargar una unidad de residuo 
	}

	@Override
	protected TipoVehiculo getTipoVehiculo() {
		return TipoVehiculo.CAMIONETA;
	}

	
}
