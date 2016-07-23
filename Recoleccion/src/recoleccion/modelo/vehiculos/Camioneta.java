package recoleccion.modelo.vehiculos;

public class Camioneta extends Vehiculo {

	public Camioneta(int id) {
		super(id);
	}

	@Override
	protected String getTipo() {
		return TipoVehiculo.CAMIONETA.name();
	}

	@Override
	protected void inicializar() {
		this.setCapacidad(800L);
		this.setRendimiento(11L);
		this.setVelocidad(70L);
	}
	
}
