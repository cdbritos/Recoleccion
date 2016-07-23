package recoleccion.modelo.domicilios;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import recoleccion.modelo.data.Coordenable;
import recoleccion.modelo.data.TipoResiduo;

public class Domicilio extends Coordenable {

	private String identificador;
	
	private List<Pedido> pedidos;

	public Domicilio() {};
	
	public Domicilio(int id) {
		this.identificador = String.valueOf(id);
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	@Override
	protected String getTipo() {
		return "DOMICILIO";
	}
	
	public void imprimir() {
		super.imprimir();
		System.out.println("	ID: " + this.identificador);
		System.out.println("	PEDIDOS:");
		if (CollectionUtils.isNotEmpty(pedidos)){
			for (Pedido pedido : pedidos) {
				System.out.print("		");
				pedido.imprimir();
			}
		}else
			System.out.println("		SIN PEDIDOS");
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identificador == null) ? 0 : identificador.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Domicilio other = (Domicilio) obj;
		if (identificador == null) {
			if (other.identificador != null)
				return false;
		} else if (!identificador.equals(other.identificador))
			return false;
		return true;
	}

	public boolean tieneBasura(){
		if (CollectionUtils.isNotEmpty(pedidos)){
			for (Pedido pedido : pedidos) {
				if (pedido.getCantidad() > 0)
					return true;
			}
		}
		return false;
	}
	
	public boolean tieneBasura(TipoResiduo tr){
	    int i=0;
	    while (i<pedidos.size()){
	    	if (tr.equals(pedidos.get(i).getResiduo())){
	           return pedidos.get(i).getCantidad() > 0L;
	        }
	        i++;
	    }
	    return false;
	}
}
