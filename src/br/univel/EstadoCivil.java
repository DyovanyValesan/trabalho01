package br.univel;

public enum EstadoCivil {

	SOLTEIRO("Solteiro", 1),
	CASADO("Casado", 2),
	VIUVO("Casado", 3);

	private String estadoCiv;
	private final int idestado;

	private EstadoCivil(String estadoCiv, int idestado){
		this.estadoCiv = estadoCiv;
		this.idestado = idestado;
	}

	public String getestadoCiv() {
		return estadoCiv;
	}

	public int getidestado() {
		return idestado;
	}

	public int toInteger() {
		return getidestado();
	}
}
