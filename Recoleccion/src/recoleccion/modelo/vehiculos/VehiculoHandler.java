package recoleccion.modelo.vehiculos;

public class VehiculoHandler {

	private static VehiculoHandler instance = null;
	
	private VehiculoHandler() {
		
	}
	
	public static VehiculoHandler getInstance(){
		
		if (instance == null)
			instance = new VehiculoHandler();
		
		return instance;
	}
	
	public Vehiculo get(int id){
		//String identificador = String.valueOf(Math.abs(id));
		
		return Math.abs(id) % 2 == 0 ? new Camion(id) : new Camioneta(id);
	}

	public Vehiculo get() {
		//retorna un vehiculo aletoria, solo se llamaria si es el primer viaje 
		// una solucion ya cruzada
		
		return new Camion(Long.valueOf(Math.round(Math.random()*100)).intValue());
	}
}
