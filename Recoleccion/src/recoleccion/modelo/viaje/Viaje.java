package recoleccion.modelo.viaje;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import recoleccion.modelo.domicilios.Domicilio;
import recoleccion.modelo.domicilios.DomiciliosHandler;
import recoleccion.modelo.jornada.Vertedero;
import recoleccion.modelo.jornada.VertederoHandler;
import recoleccion.modelo.vehiculos.Vehiculo;
import recoleccion.modelo.vehiculos.VehiculoHandler;

public class Viaje {
	
	private Vehiculo vehiculo;
	
	private List<Domicilio> domicilios;

	public Viaje(int[] dataViaje) {
		if (dataViaje.length > 0){
			// seteoVehiculo
			vehiculo = dataViaje[0] < 0 ?
				VehiculoHandler.getInstance().get(dataViaje[0]) :
				VehiculoHandler.getInstance().randomVehiculo();
			
			// seteo domicilios
			// inicializa i dependiendo si vino vehiculo o no
			domicilios = new ArrayList<Domicilio>();
			for (int i = dataViaje[0] < 0 ? 1 : 0; i<dataViaje.length;i++){
				if (dataViaje[i]!=0){
					Domicilio d = DomiciliosHandler.getInstance().get(dataViaje[i]);
				//if (!domicilios.contains(d))
					domicilios.add(d);
				}
			}
		}
	}

	public Viaje(){
		vehiculo = null;
		domicilios = null;
	}
	
	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public List<Domicilio> getDomicilios() {
		return domicilios;
	}

	public void setDomicilios(List<Domicilio> domicilios) {
		this.domicilios = domicilios;
	}

	public void imprimir() {
		vehiculo.imprimir();
		if (CollectionUtils.isNotEmpty(domicilios)){
			for (Domicilio domicilio : domicilios) {
				domicilio.imprimir();
			}
		}
		
	}

	public void doViaje() {
		if (CollectionUtils.isNotEmpty(domicilios)){
			for (Domicilio domicilio: domicilios) {
				vehiculo.recolectar(domicilio);
			}
			vehiculo.verter(VertederoHandler.getInstance().get(vehiculo));
		}
	}
	
	public List<Viaje> corregir(List<Vehiculo> vehiculosUtilizados){
		//boolean valido=false;
		//int i=0;
		//Long carga=vehiculo.getCarga();
		//int cantDom=this.getDomicilios().size();
		List<Viaje> viajesCorregidos=new ArrayList<>();
		eliminarDomiciliosRepetidos();
		viajesCorregidos=agregarViajesExcedeCarga(vehiculosUtilizados);
		return viajesCorregidos;
		
	}
	
	private void eliminarDomiciliosRepetidos(){
		
	}
	
	private List<Viaje> agregarViajesExcedeCarga(List<Vehiculo> domicilios){
		return null;
	}
	
	
	
	

}
