package recoleccion.solucion;

import ec.vector.IntegerVectorIndividual;

public class RecoleccionIntegerVectorIndividual extends IntegerVectorIndividual {

	private static final long serialVersionUID = 4891884198508606745L;

	public int getCantViajes(){
		int cantidadViajes = 0;
		
		for(int i = 0; i < genomeLength(); i++)
			if (genome[i] < 0)
				cantidadViajes++;
		
		return cantidadViajes;
		
	};
	
	private int[] getComienzosViajes(){
		
		int[] points = new int[getCantViajes()];
		int j = 0;
		for(int i = 0; i < genomeLength(); i++){
			if (genome[i] < 0){
				points[j] = i;
				j++;
			}
		}
		
		return points;
	}
	
	public int[][] getViajesIndividuo(){
		int[] points = this.getComienzosViajes();
		int[][] viajes = new int[points.length+1][];
		this.split(this.getComienzosViajes(), viajes);
		
		return viajes;
	}

	private void split(int[] points, int[][] pieces) {
		 {
		        int point0, point1;
		        point0 = 0; point1 = points[0];
		        for(int x=0;x<pieces.length;x++)
		            {
		            pieces[x] = new int[point1-point0];
		            System.arraycopy(genome,point0,pieces[x],0,point1-point0);
		            point0 = point1;
		            if (x >=pieces.length-2)
		                point1 = genome.length;
		            else point1 = points[x+1];
		            }
		        }
		    
	}
	
	
}
