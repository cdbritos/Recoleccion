package recoleccion.modelo.data;

public abstract class Coordenable {

	private Coordenada coordenadas;

	public Coordenada getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(Coordenada coordenadas) {
		this.coordenadas = coordenadas;
	}
	
	protected abstract String getTipo();
		
	public void imprimir() {
		System.out.println(getTipo() + ": " + coordenadas.getLatitud() + "," + coordenadas.getLongitud());
	}
}
