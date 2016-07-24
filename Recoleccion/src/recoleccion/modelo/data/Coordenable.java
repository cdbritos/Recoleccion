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
		System.out.println(getTipo() + ": " + (coordenadas == null ? "SIN COORDENADAS" : coordenadas.toString()));
	}
	
	/* http://www.geodatasource.com/developers/java */
	
	/* devuelve la distancia en metros entre 2 coordenadas */
	public double distance (double lat2, double lon2) {
		double theta = coordenadas.getLongitud() - lon2;
		double dist = Math.sin(deg2rad(coordenadas.getLatitud())) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(coordenadas.getLatitud())) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344; // aca tengo distancia en kilometros
		dist = dist * 1000; // distancia en metros
		
		return (dist);
	}

	public double distance(Coordenada punto){
		return distance(punto.getLatitud(),punto.getLongitud());
	}
	
	public double distance(Coordenable punto){
		return distance(punto.getCoordenadas());
	}
	
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts decimal degrees to radians						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
	
}
