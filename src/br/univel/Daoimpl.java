package br.univel;

import java.lang.reflect.Field;
import java.util.List;

import br.univel.Dao;

public class DaoImpl<T, Integer> implements Dao<T, Integer>{

	private Class<T> classe;

	//Vetor que será utilizado para o salvamento dos três clientes (objetos).
	private Object[] vetor = new Object[100];

	//Contador de itens guardados no vetor acima;
	private int idx = 0;

	@Override
	public void Salvar(T t) {

		int i = 0;
		vetor[idx++] = t;

		Class<?> aClass = t.getClass();
		String nomeClasse = t.getClass().getSimpleName();
		classe = (Class<T>) aClass;

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ").append(nomeClasse).append(" (");


		Field[] campos = aClass.getDeclaredFields();


		for (Field field : campos) {

			try {

				field.setAccessible(true);

				String nome = field.getName();

				if(i == campos.length-1){
					sql.append(nome).append(")");
				}else{
					sql.append(nome).append(", ");
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

			i++;
		}

		i = 0;

		sql.append(" VALUES (");
		for (Field field : campos) {

			try {

				field.setAccessible(true);

				Object valor = field.get(t);

				if(i == campos.length-1){
					sql.append(valor).append(")");
				}else{
					sql.append(valor).append(", ");
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			i++;
		}

	}

	@Override
	public T buscar(Integer k) {

		String cl = vetor[(int) k].getClass().getSimpleName();
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT FROM ").append(cl).append(" WHERE ID = ").append(k);

		return null;
	}

	@Override
	public void atualizar(T t) {

		int i = 0;
		Class<?> aClass = t.getClass();
		String nomeClasse = t.getClass().getSimpleName();
		classe = (Class<T>) aClass;

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ").append(nomeClasse).append(" SET ");

		Field[] campos = aClass.getDeclaredFields();

		try {

			for (Field field : campos) {

				try {

					field.setAccessible(true);

					Object valor = field.get(t);
					String nome = field.getName();


					if(i != campos.length-1){
						sql.append(nome).append("='").append(valor).append("', ");
					}else{
						sql.append(nome).append("='").append(valor).append("';");
					}

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

				i++;
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void excluir(Integer k) {

		String cl = vetor[(int) k].getClass().getSimpleName();
		StringBuilder sql = new StringBuilder();

		sql.append("DELETE FROM ").append(cl).append(" WHERE ID = ").append(k);

	}

	@Override
	public List<T> listarTodos() {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT * FROM ").append(classe.getSimpleName());

		return null;
	}

	@Override
	public void salvar(T t) {
		// TODO Auto-generated method stub

	}

}
