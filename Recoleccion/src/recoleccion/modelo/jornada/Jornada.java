package recoleccion.modelo.jornada;

import java.util.List;

import recoleccion.modelo.domicilios.Domicilio;

public class Jornada {
	
	private List<Deposito> depositos;
	
	private List<Domicilio> domicilios;
	
	private Vertedero vertedero;

	public List<Deposito> getDepositos() {
		return depositos;
	}

	public void setDepositos(List<Deposito> depositos) {
		this.depositos = depositos;
	}

	public List<Domicilio> getDomicilios() {
		return domicilios;
	}

	public void setDomicilios(List<Domicilio> domicilios) {
		this.domicilios = domicilios;
	}

	public Vertedero getVertedero() {
		return vertedero;
	}

	public void setVertedero(Vertedero vertedero) {
		this.vertedero = vertedero;
	}
	
}
