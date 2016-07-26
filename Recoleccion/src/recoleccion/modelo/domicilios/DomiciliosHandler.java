package recoleccion.modelo.domicilios;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;


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
		Domicilio d = new Domicilio(id);
		return domicilios.get(domicilios.indexOf(d));
	}

	public List<Domicilio> getDomicilios() {
		List<Domicilio> result = new ArrayList<Domicilio>();
		
		if (CollectionUtils.isNotEmpty(domicilios)){
			for (Domicilio d : domicilios) {
				result.add(new Domicilio(d));
			}
		}
		return result;
	}

	public void setDomicilios(List<Domicilio> domicilios) {
		this.domicilios = domicilios;
	}
	
	public Domicilio randomVehiculo(){
		try {
	        return domicilios.get((new Random()).nextInt(domicilios.size()));
	    }
	    catch (Throwable e){
	        return null;
	    }
    }
}


