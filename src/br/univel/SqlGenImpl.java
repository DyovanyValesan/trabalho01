package br.univel;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.univel.Coluna;
import br.univel.Tabela;

public class SqlGenImpl extends SqlGen {


	@Override
	protected String getCreateTable(Connection con, Object obj) {

		StringBuilder sql = new StringBuilder();
		Class<Object> cl = (Class<Object>) obj.getClass();

		try{

			//Declaração da tabela
			{

				String nomeTabela;
				if (cl.isAnnotationPresent(Tabela.class)) {
					Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
					nomeTabela = anotacaoTabela.value();
				}else{
					nomeTabela = cl.getSimpleName().toUpperCase();
				}
				sql.append("CREATE TABLE ").append(nomeTabela).append(" (");
			}

			Field[] atributos = cl.getDeclaredFields();


			//Declaração das colunas
			{

				for(int i = 0; i < atributos.length; i++){

					Field field = atributos[i];

					String nomeColuna;
					String tipoColuna;

					if (field.isAnnotationPresent(Coluna.class)) {
						Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

						if (anotacaoColuna.nome().isEmpty()) {
							nomeColuna = field.getName().toUpperCase();
						} else {
							nomeColuna = anotacaoColuna.nome();
						}

					} else {
						nomeColuna = field.getName().toUpperCase();
					}

					Class<?> tipoParametro = field.getType();

					if (tipoParametro.equals(String.class)) {
						tipoColuna = "VARCHAR(100)";

					} else if (tipoParametro.equals(int.class)) {
						tipoColuna = "INT";

					} else {
						tipoColuna = "DESCONHECIDO";
					}

					if (i > 0) {
						sql.append(",");
					}

					sql.append("\n\t").append(nomeColuna).append(' ').append(tipoColuna);
				}
			}

			// Declaração das chaves primárias
			{

				sql.append(",\n\tPRIMARY KEY( ");

				for (int i = 0, achou = 0; i < atributos.length; i++) {

					Field field = atributos[i];

					if (field.isAnnotationPresent(Coluna.class)) {

						Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

						if (anotacaoColuna.pk()) {

							if (achou > 0) {
								sql.append(", ");
							}

							if (anotacaoColuna.nome().isEmpty()) {
								sql.append(field.getName().toUpperCase());
							} else {
								sql.append(anotacaoColuna.nome());
							}

							achou++;
						}

					}
				}

				sql.append(" )");
			}

			sql.append("\n);");

			System.out.println(sql);

		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}
		return sql.toString();
	}

	@Override
	protected String getDropTable(Connection con, Object obj) {
		String sql = "DELETE FROM "+obj;
		System.out.println(sql);
		try(PreparedStatement ps = con.prepareStatement(sql)){
			int res = ps.executeUpdate();
			System.out.println(res + " registrados apagados");
		}catch(SQLException e){
			System.out.println("Erro ao realizar a operação de removoção da tabela " +obj);
		}
		return null;
	}

	@Override
	protected PreparedStatement getSqlInsert(Connection con, Object obj) {
		Class<? extends Object> cl = obj.getClass();

		StringBuilder sb = new StringBuilder();

		// Declaração da tabela.
		{
			String nomeTabela;
			if (cl.isAnnotationPresent(Tabela.class)) {

				Tabela anotacaoTabela = cl.getAnnotation(Tabela.class);
				nomeTabela = anotacaoTabela.value();

			} else {
				nomeTabela = cl.getSimpleName().toUpperCase();

			}
			sb.append("INSERT INTO ").append(nomeTabela).append(" (");
		}

		Field[] atributos = cl.getDeclaredFields();

		// Declaração das colunas
		{
			for (int i = 0; i < atributos.length; i++) {

				Field field = atributos[i];

				String nomeColuna;

				if (field.isAnnotationPresent(Coluna.class)) {
					Coluna anotacaoColuna = field.getAnnotation(Coluna.class);

					if (anotacaoColuna.nome().isEmpty()) {
						nomeColuna = field.getName().toUpperCase();
					} else {
						nomeColuna = anotacaoColuna.nome();
					}

				} else {
					nomeColuna = field.getName().toUpperCase();
				}

				if (i > 0) {
					sb.append(", ");
				}

				sb.append(nomeColuna);
			}
		}

		sb.append(") VALUES (");

		for (int i = 0; i < atributos.length; i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append('?');
		}
		sb.append(')');

		String strSql = sb.toString();
		System.out.println(strSql);

		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(strSql);

			for (int i = 0; i < atributos.length; i++) {
				Field field = atributos[i];

				// importante não esquecer
				field.setAccessible(true);

				if (field.getType().equals(int.class)) {
					ps.setInt(i + 1, field.getInt(obj));

				} else if (field.getType().equals(String.class)) {
					ps.setString(i + 1, String.valueOf(field.get(obj)));

				} else {
					throw new RuntimeException("Tipo não suportado, falta implementar.");

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected PreparedStatement getSqlSelectAll(Connection con, Object obj) {

		Statement st = null;
		ResultSet result = null;
		StringBuilder sql = new StringBuilder();

		Class<Object> cl = (Class<Object>) obj.getClass();

		sql.append("SELECT * FROM ").append(cl.getSimpleName());
		System.out.println(sql);

		try {
			try {

				st = con.createStatement();
				result = st.executeQuery(""+sql);

				while (result.next()) {
					int id = result.getInt(1);
					String nome = result.getString("nome");
					System.out.println(id + " " + nome);
				}
			} finally {
				if (st != null) st.close();
				if (result != null) result.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}


		return null;
	}

	@Override
	protected PreparedStatement getSqlSelectById(Connection con, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected PreparedStatement getSqlUpdateById(Connection con, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected PreparedStatement getSqlDeleteById(Connection con, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
