package recoleccion.modelo.domicilios;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
	
	public Domicilio(Domicilio d){
		this.setCoordenadas(d.getCoordenadas());
		this.identificador = d.getIdentificador();
		if (CollectionUtils.isNotEmpty(d.pedidos))
			this.pedidos = new ArrayList<Pedido>();
			for (Pedido p : d.getPedidos()) {
				this.pedidos.add(new Pedido(p));
			}

	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		List<Pedido> lstPedidos=new ArrayList<>();
		for (int i=0;i<pedidos.size();i++){
			lstPedidos.add(pedidos.get(i));
		}
		this.pedidos = lstPedidos;
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

	public boolean tieneResiduo(){
		if (CollectionUtils.isNotEmpty(pedidos)){
			for (Pedido pedido : pedidos) {
				if (pedido.getCantidad() > 0)
					return true;
			}
		}
		return false;
	}
	
	public long tieneResiduo(TipoResiduo tr){
	    int i=0;
	    while (i<pedidos.size()){
	    	if (tr.equals(pedidos.get(i).getResiduo())){
	           return pedidos.get(i).getCantidad();
	        }
	        i++;
	    }
	    return 0;
	}

	public void recolectar(TipoResiduo tr, long cantARecolectar) {
		this.pedidos.get(this.pedidos.indexOf(new Pedido(tr))).recolectar(cantARecolectar);
	}
	
	//Dado un set de residuos verifico si el domicilio tiene alguno de ellos
	public boolean residuosValidos(Set<TipoResiduo> residuos){
		boolean valido=false;
		Iterator<TipoResiduo> iter = residuos.iterator();
		while (iter.hasNext() && !valido) {
			TipoResiduo tr=(TipoResiduo) iter.next();
			Pedido p = new Pedido(tr);
			if (this.getPedidos().contains(p)){
				valido=true;
			}
		}
		return valido;

	}

	public long getFaltante() {
		long faltante = 0;
		for (Pedido p : pedidos) {
			faltante += p.getCantidad();
			
		}
		return faltante;
	}
		
}
