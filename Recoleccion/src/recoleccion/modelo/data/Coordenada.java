package recoleccion.modelo.data;

public class Coordenada {
	
	private Double latitud;
	
	private Double longitud;
	
	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public Coordenada(Double latitud, Double longitud) {
		super();
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public Coordenada() {
		super();
	}

	
	
}
