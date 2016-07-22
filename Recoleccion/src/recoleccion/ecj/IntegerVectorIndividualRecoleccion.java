package recoleccion.ecj;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import recoleccion.modelo.data.Coordenada;
import recoleccion.modelo.data.TipoResiduo;
import recoleccion.modelo.domicilios.Domicilio;
import recoleccion.modelo.domicilios.Pedido;
import recoleccion.modelo.jornada.Deposito;
import recoleccion.modelo.jornada.Jornada;
import recoleccion.modelo.jornada.Vertedero;
import recoleccion.modelo.vehiculos.Camion;
import recoleccion.modelo.vehiculos.Camioneta;
import recoleccion.modelo.vehiculos.Vehiculo;
import recoleccion.modelo.viaje.Viaje;
import recoleccion.solucion.Solucion;
import ec.EvolutionState;
import ec.util.Parameter;
import ec.vector.IntegerVectorIndividual;

/**
 *
 * @author ggutierrez
 */
public class IntegerVectorIndividualRecoleccion extends IntegerVectorIndividual{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7478676840715483431L;
	
	public static final String DEPOSITOS_IN = "in_depositos";
    public static final String DOM_JOR_IN = "in_domicilios_jornada";
    public static final String VERTEDEROS_IN = "in_vertederos";
    
    public File in_depositos;
    public File in_domicilios_jornada;
    public File in_vertederos;

    ArrayList<Integer> numeros= new ArrayList<Integer>();
    int res;
    Random rnd=new Random();
    //int[] genome1=new int[5];
    //Parameter base;

    /*@Override
    public void setup(EvolutionState state, Parameter base) {
        //super.setup(state, base); //To change body of generated methods, choose Tools | Templates.
        System.out.println("PARAMETROS: "+base);
        in_depositos = state.parameters.getFile(base.push(DEPOSITOS_IN), null);
       in_domicilios_jornada = state.parameters.getFile(base.push(DOM_JOR_IN), null);
       in_vertederos = state.parameters.getFile(base.push(VERTEDEROS_IN), null);
       System.out.println("VERTEDEROS: "+in_vertederos);
    }*/
    
   
    
   @Override
    public void reset(EvolutionState state, int thread) {
        //super.reset(state, thread); //To change body of generated methods, choose Tools | Templates.
	   Parameter base = defaultBase(); 
	   in_depositos = state.parameters.getFile(base.push(DEPOSITOS_IN), null);
       in_domicilios_jornada = state.parameters.getFile(base.push(DOM_JOR_IN), null);
       in_vertederos = state.parameters.getFile(base.push(VERTEDEROS_IN), null);
	   
       System.out.println("PARAMETROS: "+base);
       
	   Vertedero vertedero=new Vertedero();
	   Scanner s = null;

       try {
    	   s = new Scanner(in_vertederos);
    	   while (s.hasNextLine()) {
    		String linea = s.nextLine(); 
                System.out.println("VERTEDERO: "+linea);
    		double latitud=Double.parseDouble(linea.split(",")[0]);
               double longitud=Double.parseDouble(linea.split(",")[1]);
               Coordenada coord=new Coordenada();
               coord.setLatitud(latitud);
               coord.setLongitud(longitud);
               vertedero.setCoordenadas(coord);
    	   }
    	   
    	   List<Domicilio> domicilios=new ArrayList<>();
    	   s = new Scanner(in_domicilios_jornada);
    	   int identificador=0;
    	   while (s.hasNextLine()) {
                String linea = s.nextLine(); 
                System.out.println("DOMICILIO: "+linea);
                String [] cadena=linea.split(",");
                double latitud=Double.parseDouble(cadena[0]);
                double longitud=Double.parseDouble(cadena[1]);
                int cantPedidos=2;
                List<Pedido> pedidos=new ArrayList<>();
                Domicilio domicilio=new Domicilio();
                while (cantPedidos<cadena.length){
                    Pedido pedido=new Pedido();
                    pedido.setCantidad(Long.valueOf(cadena[cantPedidos]));
                    cantPedidos++;
                    String tipoResiduo=cadena[cantPedidos];
                    TipoResiduo tr = null;
                    if (tipoResiduo.equals("Poda")){
                            tr=TipoResiduo.PODA;
                    }else if(tipoResiduo.equals("Escombro")){
                            tr=TipoResiduo.ESCOMBROS;
                    }else{
                            tr=TipoResiduo.GRAN_VOLUMEN;
                    }
                    pedido.setResiduo(tr);
                    cantPedidos++;
                    pedidos.add(pedido);
                }
               
               Coordenada coord=new Coordenada();
               coord.setLatitud(latitud);
               coord.setLongitud(longitud);
               domicilio.setCoordenadas(coord);
               domicilio.setPedidos(pedidos);
               domicilio.setIdentificador(Integer.toString(identificador));
               identificador++;
               domicilios.add(domicilio);
    	   }
    	   
            List<Deposito> depositos=new ArrayList<>();
            s = new Scanner(in_depositos);
            Coordenada coord=new Coordenada();
            int idVehiculo=0;
            Deposito deposito=null;
            List<Vehiculo> vehiculos=null;
            String linea = s.nextLine(); 
            System.out.println("DEPOSITO: "+linea);
            String [] cadena=linea.split(",");
            while (s.hasNextLine()) {
    		   /*String linea = s.nextLine(); 
                   System.out.println("DEPOSITO: "+linea);
    		   String [] cadena=linea.split(",");*/
    		   if (cadena[0].equals("DEPOSITO")){
                        double latitud=Double.parseDouble(cadena[1]);
                        double longitud=Double.parseDouble(cadena[2]);
                        coord=new Coordenada();
                        coord.setLatitud(latitud);
                        coord.setLongitud(longitud);
                        deposito=new Deposito();
                        deposito.setCoordenadas(coord);
                        linea = s.nextLine(); 
                        cadena=linea.split(",");
                        //deposito.setFlota(vehiculos);
                        //depositos.add(deposito);
    		   }else{
                       boolean salir=false;
                       vehiculos=new ArrayList<>();
                       while (!salir && !cadena[0].equals("DEPOSITO")){
                            int cantVehiculos=Integer.parseInt(cadena[0]);
                            //List<Vehiculo> vehiculos=new ArrayList<>();
                            for(int i=0;i<cantVehiculos;i++){
                                    idVehiculo++;
                                    if (cadena[1].equals("CAMIONES")){
                                            Camion camion=new Camion(idVehiculo);
                                            camion.setCoordenadas(coord);
                                            camion.setTiposResiduos(calcularTipoResiduo(linea));
                                            vehiculos.add(camion);

                                    }
                                    else{
                                            Camioneta camiononeta=new Camioneta(idVehiculo);
                                            camiononeta.setCoordenadas(coord);
                                            camiononeta.setTiposResiduos(calcularTipoResiduo(linea));
                                            vehiculos.add(camiononeta); 
                                    }
                                    //deposito.setFlota(vehiculos);
                                    //depositos.add(deposito);
                            }
                            if (s.hasNextLine()){
                                linea = s.nextLine(); 
                                cadena=linea.split(",");
                            }else{
                                salir=true;
                            }
                            
                       }
                         deposito.setFlota(vehiculos);
                         depositos.add(deposito);
    		   }
    		   
    		   
    	   }
    	   
    	   Jornada jornada=new Jornada();
    	   jornada.setDepositos(depositos);
    	   jornada.setDomicilios(domicilios);
    	   jornada.setVertedero(vertedero);
           
           System.out.println("CANT DEPOSITOS: "+jornada.getDepositos().size());
           System.out.println("CANT VEHICULOS DEP1: "+jornada.getDepositos().get(0).getFlota().size());
           System.out.println("CANT VEHICULOS DEP2: "+jornada.getDepositos().get(1).getFlota().size());
           System.out.println("CANT DOMICILIOS: "+jornada.getDomicilios().size());
           System.out.println("CANT VERTEDEROS: "+jornada.getVertedero());
           
           
           //CARGANDO LA SOLUCION RANDOMICA
           Solucion sol = new Solucion();
           Viaje viaje = new Viaje();
           
           List<Domicilio> domiciliosCompletos=new ArrayList<>();
           List<Domicilio> domiciliosViajes=new ArrayList<>();
           List<Viaje> viajes=new ArrayList<>();
           domiciliosCompletos=jornada.getDomicilios();
           
           Random rand=new Random();
           //List<Vehiculo> vehiculos=new ArrayList<>();
           while(!domiciliosCompletos.isEmpty()){
               System.out.println("DOMICILIO: "+domiciliosCompletos);
                int randDeposito=rand.nextInt(jornada.getDepositos().size()-1);
                Deposito depActual=jornada.getDepositos().get(randDeposito);
                int randVehiculo=rand.nextInt(depActual.getFlota().size()-1);
                Vehiculo vehiculoActual=depActual.getFlota().get(randVehiculo);
                viaje.setVehiculo(vehiculoActual);
                //int randDomicilio=rand.nextInt(domiciliosCompletos.size()-1);
                 
                int i=0;
                Domicilio dom=new Domicilio();
                boolean valido=false;
                while (i<domicilios.size() && !valido && vehiculoActual.getCapacidad()>0){
                    System.out.println("VEHICULO: "+vehiculoActual);
                    //randDomicilio=rand.nextInt(domiciliosCompletos.size()-1);
                    valido=false;
                    
                    dom=domicilios.get(i);
                    Iterator iter = vehiculoActual.getTiposResiduos().iterator();
                    Pedido pedido=null;
                    while (iter.hasNext() && !valido) {
                        TipoResiduo tr=(TipoResiduo)iter.next();
                        if (contieneResiduos(dom.getPedidos(),tr)){
                            System.out.println("CONTIENE PEDIDO: "+dom);
                            int j=0;
                            TipoResiduo trPedido=dom.getPedidos().get(j).getResiduo();
                            while (trPedido!=tr){
                                j++;
                                trPedido=dom.getPedidos().get(j).getResiduo();
                            }
                            //if (v.getCapacidad()>=0){
                                valido=true;
                                Long capacidadActual=vehiculoActual.getCapacidad()-dom.getPedidos().get(j).getCantidad();
                                vehiculoActual.setCapacidad(capacidadActual);
                                dom.getPedidos().remove(dom.getPedidos().get(j));
                                domiciliosViajes.add(dom);
                                if (dom.getPedidos().isEmpty()){
                                    System.out.println("REMOVI: "+dom);
                                    domicilios.remove(dom);
                                }
                            //}             
                        }
                    }
                   i++;
                    
                    
                    
                    
                    /*Domicilio domActual=domicilioValido(vehiculoActual,domiciliosCompletos,valido);
                    domiciliosViajes.add(domActual);*/
                    System.out.println("CANT DOM: "+domiciliosCompletos.size());
                    
                   
                }               
                viaje.setDomicilios(domiciliosViajes);  
                viajes.add(viaje);
           }
           sol.setViajes(viajes);
           
           for (int i=0;i<sol.getViajes().size();i++){
               System.out.println("SOLUCION: "+sol.getViajes().size());
           }
           
           

       } catch (Exception ex) {
           System.out.println("Mensaje: " + ex.getMessage());
           ex.printStackTrace();
	   } finally {
	           // Cerramos el fichero tanto si la lectura ha sido correcta o no
	           try {
	                   if (s != null)
	                           s.close();
	           } catch (Exception ex2) {
	                   System.out.println("Mensaje 2: " + ex2.getMessage());
	           }
	   }
	   
	   
	   
        /*for(int i=0;i<(genome.length-1);i++){
                numeros.add(i, i+1);
        }
        
        genome[0]=0;
        int k=numeros.size();
        int n=k;
        for(int i=1;i<=k;i++){
            res=rnd.nextInt(n);            
            genome[i]=(Integer)numeros.get(res);
            numeros.remove(res);
            n--;
        }*/
        
    }
   public Domicilio domicilioValido(Vehiculo v,List<Domicilio> domicilios ,boolean valido){
       Domicilio dom=new Domicilio();
       valido=false;
       int i=0;
       while (i<domicilios.size() && !valido){
            dom=domicilios.get(i);
            Iterator iter = v.getTiposResiduos().iterator();
            Pedido pedido=null;
            while (iter.hasNext() && !valido) {
                TipoResiduo tr=(TipoResiduo)iter.next();
                if (contieneResiduos(dom.getPedidos(),tr)){
                    System.out.println("CONTIENE PEDIDO: "+dom);
                    int j=0;
                    TipoResiduo trPedido=dom.getPedidos().get(j).getResiduo();
                    while (trPedido!=tr){
                        j++;
                        trPedido=dom.getPedidos().get(j).getResiduo();
                    }
                    //if (v.getCapacidad()>=0){
                        valido=true;
                        Long capacidadActual=v.getCapacidad()-dom.getPedidos().get(j).getCantidad();
                        v.setCapacidad(capacidadActual);
                        dom.getPedidos().remove(dom.getPedidos().get(j));
                        if (dom.getPedidos().isEmpty()){
                            System.out.println("REMOVI: "+dom);
                            domicilios.remove(dom);
                        }
                    //}             
                }
            }
           i++;
       }
       if (i==domicilios.size()){
           valido=true;
       }
       
       return dom;
        
   } 
    
   public boolean contieneResiduos(List<Pedido> pedidos, TipoResiduo tr){
       boolean contiene=false;
       int i=0;
       while (i<pedidos.size() && !contiene){
           TipoResiduo trPedido=pedidos.get(i).getResiduo();
           if (trPedido==tr){
               contiene=true;
           }
           i++;
       }
       return contiene;
   }
    
   /*public Pedido domicilioValido(Vehiculo v,Domicilio dom,boolean valido){
        Iterator iter = v.getTiposResiduos().iterator();
        Pedido pedido=null;
        while (iter.hasNext() && !valido) {
            TipoResiduo tr=(TipoResiduo)iter.next();
            if (dom.getPedidos().contains(tr)){
                
                int i=0;
                while (!dom.getPedidos().get(i).equals(tr)){
                    i++;
                }
                if (v.getCapacidad()>=0){
                    valido=true;
                    pedido=new Pedido();
                    pedido=dom.getPedidos().get(i);
                }             
            }
        }
        return pedido;
        
   }*/
   
  
   
   
   
   public Set<TipoResiduo> calcularTipoResiduo(String linea){
	   Set<TipoResiduo> tiposResiduos=new HashSet<TipoResiduo>();
	   String[] cadena=linea.split(",");
	   int contTipos=2;
	   while (contTipos<cadena.length){
		   TipoResiduo tr = null;
		   if (cadena[contTipos].equals("GRAN_VOLUMEN")){
			   tr=TipoResiduo.GRAN_VOLUMEN;
		   }else if (cadena[contTipos].equals("PODA")){
			   tr=TipoResiduo.PODA;
		   }else{
			   tr=TipoResiduo.ESCOMBROS;
		   }
		   tiposResiduos.add(tr);
           contTipos++;
	   }
	   return tiposResiduos;
	   
   }
   
   /* ****************************************************
    * COMIENZO MERGE  
    * ****************************************************/
   
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
	    int point0, point1;
	    point0 = 0; point1 = points[0];
	    for(int x=0;x<pieces.length;x++){
	    	pieces[x] = new int[point1-point0];
	    	System.arraycopy(genome,point0,pieces[x],0,point1-point0);
	    	point0 = point1;
	    	if (x >=pieces.length-2)
	    		point1 = genome.length;
	    	else point1 = points[x+1];
        }
	}
		    
}