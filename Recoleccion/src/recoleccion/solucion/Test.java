package recoleccion.solucion;


public class Test {
	
	public static void main(String[] args) {
		
		int[] genome = {1,2,3,
						-1,1,23,343,
						-2,12,2,3,22,12,3,4,
						-4,1,3,4,5,16,26,
						-3,1,2,3,4,5,6,
						-1,4,5,67,3,23};
		
		RecoleccionIntegerVectorIndividual r = new RecoleccionIntegerVectorIndividual();
		r.genome = genome;
		
		Solucion s = new Solucion(r);
		s.imprimir();
		
		
	}

}
