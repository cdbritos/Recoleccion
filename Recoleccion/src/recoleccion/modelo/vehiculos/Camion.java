package recoleccion.modelo.vehiculos;

public class Camion extends Vehiculo {
	
	public Camion(int id) {	super(id);}
	public Camion(Vehiculo v) {	super(v);}

	protected String getTipo() {return TipoVehiculo.CAMION.name();	}

	public Long getCapacidad() {return 2000L;}

	public Long getVelocidad() {return 40L;}

	public Long getRendimiento() {return 5L;}

	public Long getPrecioCombustible() {return 38L;}

	public int getEmpleados() {return 3;}
	
	public double getTiempoDescarga(){return 0.1;}
	
	protected double getTiempoCarga() {return 1;}

	public TipoVehiculo getTipoVehiculo() {return TipoVehiculo.CAMION;}
	
	@Override
	protected double getCostoFijo() {
		return 3000;
	}
		
}
