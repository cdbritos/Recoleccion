package recoleccion.modelo.domicilios;

import recoleccion.modelo.data.TipoResiduo;

public class Pedido {
	
	private TipoResiduo residuo;
	
	private Long cantidad;

	public Pedido(TipoResiduo tr) {
		super();
		residuo = tr;
		cantidad = 0L;
	}
	
	public Pedido(TipoResiduo residuo, Long cantidad) {
		super();
		this.residuo = residuo;
		this.cantidad = cantidad;
	}
	
	public Pedido(){
		super();
	}

	public Pedido(Pedido p) {
		this.residuo = p.getResiduo();
		this.cantidad = p.cantidad;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((residuo == null) ? 0 : residuo.hashCode());
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
		Pedido other = (Pedido) obj;
		if (residuo != other.residuo)
			return false;
		return true;
	} 
	
	public void imprimir(){
		System.out.println(residuo + ": " + cantidad);
	}

	public void recolectar(long cantARecolectar) {
		cantidad -= cantARecolectar;
	}
}
