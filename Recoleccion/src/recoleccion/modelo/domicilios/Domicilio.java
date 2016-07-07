package recoleccion.modelo.domicilios;

import java.util.List;

import recoleccion.modelo.data.Coordenable;

public class Domicilio extends Coordenable {

	private String identificador;
	
	private List<Pedido> pedidos;

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

}
