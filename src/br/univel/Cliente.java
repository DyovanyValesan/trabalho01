package br.univel;

@Tabela("CAD_CLIENTE")
public class Cliente {
	@Coluna(pk=true)
	private int id;
	@Coluna(nome="CLNOME")
	private String nome;
	@Coluna(nome="CLENDEREÇO")
	private String endereço;
	@Coluna(nome="CLTELEFONE")
	private String telefone;
	//private EstadoCivil estadoCivil = EstadoCivil.CASADO;
	public Cliente() {
		this(0, null, null, null, null);
	}

	public Cliente(int id, String nome, String endereço, String telefone, Object object) {
		super();
		this.id = id;
		this.nome = nome;
		this.nome = endereço;
		this.nome = telefone;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEndereço() {
		return endereço;
	}
	public void setEndereço(String endereço) {
		this.endereço = endereço;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

}
