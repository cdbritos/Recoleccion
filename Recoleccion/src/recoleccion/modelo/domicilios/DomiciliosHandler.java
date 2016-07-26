package recoleccion.modelo.domicilios;

import java.util.List;


public class DomiciliosHandler {

	private static DomiciliosHandler instance = null;
	
	private List<Domicilio> domicilios;
	
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

	public List<Domicilio> getDomicilios() {
		return domicilios;
	}

	public void setDomicilios(List<Domicilio> domicilios) {
		this.domicilios = domicilios;
	}
}


