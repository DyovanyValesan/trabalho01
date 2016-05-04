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
		Cliente c1 = new Cliente(1, "Valesan", "Av 123", "1111-1111", EstadoCivil.SOLTEIRO.getidestado());
		Cliente c2 = new Cliente(2, "Alemao", "Rua 456", "2222-2222", EstadoCivil.CASADO.getidestado());
		Cliente c3 = new Cliente(3, "Tião", "Av 789", "3333-3333", EstadoCivil.VIUVO.getidestado());

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
				String DRIVE_CLASS = "org.h2.Drive";
				String url = "jdbc:h2:./BancoValesan";
				String user = "sa";
				String pass = "sa";
				con = DriverManager.getConnection(url, user, pass);
				System.out.println("Conexão Realizada! \n");
			}catch(SQLException e){
				System.out.println("Erro ao realizar a conexão com o banco de dados!");
			}

	}

	public static void main(String[] args) throws SQLException{
		System.out.println("Mecânismo Persistência de Objetos");
		new Execute();
	}

}
