
package recoleccion.modelo.jornada;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import recoleccion.modelo.data.TipoResiduo;

/**
 *
 * @author ggutierrez
 */
public class GeneradorRecoleccion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        double matrizDistancia [][]=new double [Integer.parseInt(args[0])][Integer.parseInt(args[0])];
        ArrayList<String> domicilios=new ArrayList<>();
        ArrayList<String> domiciliosGenerador=new ArrayList<>();
        String fichero="domicilios.txt";
        try {
            FileReader fr = new FileReader(fichero);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            while((linea = br.readLine()) != null){
              domicilios.add(linea);
            }
            fr.close();
            
            int n=domicilios.size();  //numeros aleatorios
            int k=n;  //auxiliar;
            ArrayList<Integer> numeros=new ArrayList<>();
            int[] resultado=new int[Integer.parseInt(args[0])];
            Random rnd=new Random();
            int res;
   
            ArrayList<Integer> mapeo=new ArrayList<>();
            for(int i=0;i<Integer.parseInt(args[0]);i++){
                res=rnd.nextInt(n);
                while (mapeo.contains(res)){
                    res=rnd.nextInt(n);
                }
                mapeo.add(res);
                domiciliosGenerador.add(i,domicilios.get(res));               
            }
            double latitudCol;
            double longitudCol;
            double latitudFila;
            double longitudFila;
            String archivo_distancia = "distancias_domicilios.txt";
            File archivo = new File(archivo_distancia);
            PrintWriter  pw;
            String esc="";
            FileWriter fichero_esc=new FileWriter(archivo_distancia);
            pw = new PrintWriter(fichero_esc);
            for(int i=0;i<Integer.parseInt(args[0]);i++){
                for(int j=0;j<Integer.parseInt(args[0]);j++){                   
                    if (i==j){
                        matrizDistancia[i][j]=0;
                        esc="0 ";
                        //pw.print("0 ");
                    }else{
                        latitudCol=Double.parseDouble(domiciliosGenerador.get(j).split(",")[0]);
                        longitudCol=Double.parseDouble(domiciliosGenerador.get(j).split(",")[1]);
                        latitudFila=Double.parseDouble(domiciliosGenerador.get(i).split(",")[0]);
                        longitudFila=Double.parseDouble(domiciliosGenerador.get(i).split(",")[1]);
                        matrizDistancia[i][j]=Math.sqrt(Math.pow(latitudCol-latitudFila,2)+Math.pow(longitudCol-longitudFila,2));
                        double distancia=Math.sqrt(Math.pow(latitudCol-latitudFila,2)+Math.pow(longitudCol-longitudFila,2));             
                        esc=String.valueOf(distancia)+" ";
                    }
                    pw.print(esc);
                }
                pw.println();
            }
            fichero_esc.close();
            
            String archivo_jornada = "domicilios_jornada.txt";
            archivo = new File(archivo_jornada);
            esc="";
            fichero_esc=new FileWriter(archivo);
            pw = new PrintWriter(fichero_esc);
            
            TipoResiduo tipoResiduos[] = TipoResiduo.values();
            
            int tipoResiduosPos;
            int m3Residuos;
            int contador=0;
            int m3Tope=20;
            boolean hayTipo=false;
            for (int i=0;i<domiciliosGenerador.size();i++){
                esc=domiciliosGenerador.get(i);
                contador=0;
                while (contador<tipoResiduos.length){
                    if (rnd.nextBoolean()){
                        m3Residuos=rnd.nextInt(m3Tope);
                        hayTipo=true;
                        while (m3Residuos==0){
                            m3Residuos=rnd.nextInt(m3Tope);
                        }
                        //tipoResiduosPos=rnd.nextInt(tipoResiduos.length-1);
                        esc+=","+Integer.toString(m3Residuos)+","+tipoResiduos[contador];
                    }
                    /*
                     * AVECES AGREGA DOMICILIOS SIN NADA 
                     * 
                     * */
                    contador++;
                }
                if (!hayTipo){
                    m3Residuos=rnd.nextInt(m3Tope);
                    while (m3Residuos==0){
                        m3Residuos=rnd.nextInt(m3Tope);
                    }
                    tipoResiduosPos=rnd.nextInt(tipoResiduos.length-1);
                    esc+=","+Integer.toString(m3Residuos)+","+tipoResiduos[tipoResiduosPos];
                }
                pw.print(esc);
                pw.println();
                esc="";
            }
            
            fichero_esc.close();
            
             //se imprime el resultado;
            /*System.out.println("El resultado de la matriz es:");
            for(int i=0;i<Integer.parseInt(args[0]);i++){
                System.out.println(resultado[i]);
            }*/
            
        }
        catch(Exception e) {
            System.out.println("Excepcion leyendo fichero "+ fichero + ": " + e);
        }
    }
    
}
