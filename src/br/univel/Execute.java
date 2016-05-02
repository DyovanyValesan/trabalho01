package br.univel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import br.univel.EstadoCivil;

public class Execute extends SqlGenImpl{

	Connection con;


	public Execute() throws SQLException{

		AbrirConexao();

		//Istancia a classe generica ImplDao com o vetor para armazenar os objetos Clientes
		DaoImpl<Cliente, Integer> clientes = new DaoImpl<>();

		//Cria a tabela
		String strCreateTable = getCreateTable(con, new Cliente());

		//Istancia os objetos a serem inseridos
		Cliente c1 = new Cliente(1, "Gabriel", "Avenida 01", "0000-1111", EstadoCivil.SOLTEIRO.getidestado());
		Cliente c2 = new Cliente(2, "Gibimba", "Rua 02", "0000-2222", EstadoCivil.CASADO.getidestado());
		Cliente c3 = new Cliente(3, "Alakart", "Avenida 03", "0000-3333", EstadoCivil.VIUVO.getidestado());

		System.out.println("");
		//Insere 3 objetos Clientes
		clientes.Salvar(c1);
		clientes.Salvar(c2);
		clientes.Salvar(c3);

		//Insere os 3 objetos instanciados na tabela criada
		PreparedStatement ps;
		ps = getSqlInsert(con, c1);
		ps.addBatch();
		ps = getSqlInsert(con, c2);
		ps.addBatch();
		ps = getSqlInsert(con, c3);
		ps.addBatch();
		ps.executeBatch();

		//Lista os 3 objetos presentes no banco
		PreparedStatement pl = getSqlSelectAll(con, c1);
		clientes.listarTodos();

		//Busca o objeto id 1
		PreparedStatement pb = getSqlSelectById(con, c1);
		clientes.buscar(1);

		//Altera o objeto id 1
		c1.setNome("Gabriel");
		clientes.atualizar(c1);
		PreparedStatement pu = getSqlUpdateById(con, c1);

		//Remove o objeto id 2
		PreparedStatement pr = getSqlDeleteById(con, c1);
		clientes.excluir(2);

		//Lista todos os objetos novamente
		PreparedStatement pf = getSqlSelectAll(con, c1);
		clientes.listarTodos();

		ps.close();
		pl.close();
		pb.close();
		pu.close();
		pr.close();
		pf.close();
		con.close();

	}

	private void AbrirConexao(){
			try{
				String url = "jdbc:h2:./BancoPontin";
				String user = "sa";
				String pass = "sa";
				con = DriverManager.getConnection(url, user, pass);
				System.out.println("Conexão Realizada!");
			}catch(SQLException e){
				System.out.println("Erro ao realizar a conexão com o banco de dados!");
			}

	}

	public static void main(String[] args) throws SQLException{
		System.out.println("-----------------|Mecânismo Genérico para persistência de objetos|-----------------\n");
		new Execute();
	}

}
