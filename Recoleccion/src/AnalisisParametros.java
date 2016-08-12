import java.io.BufferedReader;
import java.io.IOException;

import ec.Evolve;
import recoleccion.modelo.jornada.Jornada;


public class AnalisisParametros {

	public static void main(String[] args) {
		
		for(int fila = 3; fila < 30; fila++){
			String[] args2 = {"-file", "recoleccion.params",String.valueOf(fila)};
			Evolve.main(args2);
		}
		

	}

		

}
