package recoleccion.modelo.jornada;

import recoleccion.modelo.data.Coordenable;

public class VertederoHandler {

	private static VertederoHandler instance = null;
	
	private VertederoHandler() {
		
	}
	
	public static VertederoHandler getInstance(){
		
		if (instance == null)
			instance = new VertederoHandler();
		
		return instance;
	}
	
	public Vertedero get(Coordenable vehiculo) {
		//retorna el vertedero mas cercano al vehiculo
		return new Vertedero();
	}
}
