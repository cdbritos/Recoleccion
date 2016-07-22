package recoleccion.ecj;

import ec.EvolutionState;
import ec.Individual;
import ec.Problem;
import ec.simple.SimpleFitness;
import ec.simple.SimpleProblemForm;
import ec.simple.SimpleStatistics;
import ec.util.Parameter;
import ec.vector.IntegerVectorIndividual;
import static ec.vector.VectorSpecies.P_GENOMESIZE;
import java.io.File;

import java.lang.System;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author ggutierrez
 */
public class Recoleccion extends Problem implements SimpleProblemForm{
    
    public static final String MATRIZ_IN = "in_matriz";
    public static final String TEMPORADAS_IN = "in_temporadas";
    public final static String P_GENOMESIZE = "genome-size";

    int [][] mCostos;
    ArrayList<ArrayList<Integer>> filas = new ArrayList<>();
    
    ArrayList numeros= new ArrayList<Integer>();
    int res;
    Random rnd=new Random();
    public File in_matriz;
    public File in_temporadas;
    int[][] temporadas=new int[3][2];

    
    @Override
    public void setup(EvolutionState state, Parameter base) {
        Parameter def = defaultBase();  
        super.setup(state, base); //To change body of generated methods, choose Tools | Templates.
        in_matriz = state.parameters.getFile(base.push(MATRIZ_IN), null);
        in_temporadas = state.parameters.getFile(base.push(TEMPORADAS_IN), null);
        
        int fila=0;
        Scanner s = null;

        /*try {
                // Leemos el contenido del fichero
                System.out.println("... Leemos el contenido de la matriz ...");
                s = new Scanner(in_matriz);
                // Leemos linea a linea el fichero
                while (s.hasNextLine()) {
                        ArrayList<Integer> columnas = new ArrayList<>();
                        String linea = s.nextLine(); 	// Guardamos la linea en un String
                        String [] arrayStr=linea.split(" ");
                        for (int columna=0;columna<arrayStr.length;columna++){
                            columnas.add(columna,Integer.parseInt(arrayStr[columna]));
                            //mCostos[fila][columna]=Integer.parseInt(arrayStr[columna]);
                        }
                        filas.add(fila, columnas);
                        fila++;
                        System.out.println(linea);      // Imprimimos la linea
                }
            base.param="pop.subpop.0.species";
            state.parameters.set(base.push(P_GENOMESIZE), String.valueOf(fila));
            state.parameters.set(base.push("min-gene"), "1");
            state.parameters.set(base.push("max-gene"), String.valueOf(fila-1));
   
             System.out.println("... Leemos el contenido de las temporadas ...");
                s = new Scanner(in_temporadas);
                // Leemos linea a linea el fichero
                fila=0;
                while (s.hasNextLine()) {
                        String linea = s.nextLine(); 	// Guardamos la linea en un String
                        String [] arrayStr=linea.split(",");
                        for (int col=0;col<arrayStr.length;col++){
                            temporadas[fila][col]=Integer.parseInt(arrayStr[col]);
                        }
                        fila++;
                        System.out.println(linea);      // Imprimimos la linea
                }
            
                
        } catch (Exception ex) {
                System.out.println("Mensaje: " + ex.getMessage());
        } finally {
                // Cerramos el fichero tanto si la lectura ha sido correcta o no
                try {
                        if (s != null)
                                s.close();
                } catch (Exception ex2) {
                        System.out.println("Mensaje 2: " + ex2.getMessage());
                }
        }*/
  
    }
    
    

    @Override
    public void evaluate(EvolutionState state, Individual ind, int subpopulation, int thread) {
   
            if (ind.evaluated) return;
            
            IntegerVectorIndividual ind2 = (IntegerVectorIndividual)ind;
            
            double valor = 0;//Calcular el fitness del individuo
            double aSumar=0;
            
            //funcion de fitness
           /* for (int i=1;i<ind2.genome.length;i++){
                //valor+=((mCostos[ind2.genome[i-1]][ind2.genome[i]])*Porcentaje(i));//+balanceo(ind2.genome[i-1],ind2.genome[i]);
                aSumar=(filas.get(ind2.genome[i-1]).get(ind2.genome[i]));
                if (aSumar<0) {
                        aSumar=Integer.MAX_VALUE;
                }    
                valor+= Math.round((aSumar*Porcentaje(i*5))); 
            }*/
            
           //System.out.println(ind2.genome[0]+"-"+ind2.genome[1]+"-"+ind2.genome[2]+"-"+ind2.genome[3]+"-"+ind2.genome[4]);
           
           ((SimpleFitness) ind2.fitness).setFitness(state, valor*(-1), valor==0);
            ind.evaluated=true;
            
            
            }
            
    
    public double Porcentaje(int j){
        /*if (j==1)
            return 1;
        else if (j==2)
            return 1.1;
        else
            return 1.3;*/
        //temporada baja
        if (j<=temporadas[1][0])
            return 1;
        else if ((j>temporadas[1][0]) && (j<=temporadas[2][0])) //temporada media
            return 1.1;
        else // temporada alta
            return 1.3;
    }
    
    public int balanceo(int i1,int i2){
        int balance=0;
        if (i1==i2)
            balance=Integer.MAX_VALUE;
        
        return balance;
    }
    
    @Override
    public void describe(EvolutionState state, Individual ind,
    		int subpopulation, int threadnum, int log) {
    	// TODO Auto-generated method stub
    	state.output.println( ind.genotypeToStringForHumans(), log );
    }
    
}

