package recoleccion.modelo.domicilios;

import recoleccion.modelo.data.TipoResiduo;

public class Pedido {
	
	private TipoResiduo residuo;
	
	private Long cantidad;

	public TipoResiduo getResiduo() {
		return residuo;
	}

	public void setResiduo(TipoResiduo residuo) {
		this.residuo = residuo;
	}

	public Long getCantidad() {
		return cantidad;
	}

	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	} 

	
	
	
}
