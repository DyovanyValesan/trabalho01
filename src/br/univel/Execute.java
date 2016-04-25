package br.univel;

public class Execute {

	public static void main(String[] args) {
		Dao d = new Daoimpl();
		Cliente c = new Cliente();
		c.setId(1);
		c.setNome("Pontin");
		c.setTelefone("123456789");
		c.setEndereço("Rua Tal");
		
		d.excluir(c);
		d.create(c);
		d.salvar(c);
		d.salvar(c);
		d.salvar(c);
		d.listarTodos();
		d.buscar(c);
		d.atualizar(c);
		d.delete(c);
		d.listarTodos();		
	}
	
}
