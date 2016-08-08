package recoleccion.modelo.vehiculos;

public class Camioneta extends Vehiculo {

	public Camioneta(int id) {super(id);}

	public Camioneta(Vehiculo v) {super(v);}

	protected String getTipo() {return TipoVehiculo.CAMIONETA.name();}

	public Long getCapacidad() {return 800L;}

	public Long getVelocidad() {return 70L;}
		
	public Long getRendimiento() {return 14L;}

	public Long getPrecioCombustible() {return 40L;}

	public int getEmpleados() {return 2;}

	public double getTiempoDescarga() {return 0.5;}
	
	protected double getTiempoCarga() {return 0.5;}
	
	protected TipoVehiculo getTipoVehiculo() {return TipoVehiculo.CAMIONETA;}

	@Override
	protected double getCostoFijo() {
	
		return 2000;
	}

	
}
