package recoleccion.modelo.domicilios;


public class DomiciliosHandler {

	private static DomiciliosHandler instance = null;
	
	private DomiciliosHandler() {
		
	}
	
	public static DomiciliosHandler getInstance(){
		
		if (instance == null)
			instance = new DomiciliosHandler();
		
		return instance;
	}
	
	public Domicilio get(int id){
		return new Domicilio(id);
	}
}


