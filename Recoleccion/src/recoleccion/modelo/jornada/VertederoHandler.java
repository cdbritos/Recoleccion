package recoleccion.modelo.jornada;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import recoleccion.modelo.data.Coordenable;

public class VertederoHandler {

	private static VertederoHandler instance = null;
	
	private List<Vertedero> vertederos;
	
	private VertederoHandler() {
		
	}
	
	public static VertederoHandler getInstance(){
		
		if (instance == null)
			instance = new VertederoHandler();
		
		return instance;
	}
	
	public Vertedero get(Coordenable vehiculo) {
		if (CollectionUtils.isNotEmpty(vertederos))
			return vertederos.get(0);
		return null;
	}

	public List<Vertedero> getVertederos() {
		return vertederos;
	}

	public void setVertederos(List<Vertedero> vertederos) {
		this.vertederos = vertederos;
	}
}
