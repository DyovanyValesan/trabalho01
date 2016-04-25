package br.univel;

import java.util.List;

public class Daoimpl<T, K> implements Dao<T, K> {

	@Override
	public void salvar(T t) {
		SqlGen s = new SqlGenImpl();
		s.getSqlInsert(con, obj);
	}
	@Override
	public void atualizar(T t) {
		SqlGen s = new SqlGenImpl();
		s.getSqlUpdateById(con, obj);
	}

	@Override
	public void excluir(K k) {
		SqlGen s = new SqlGenImpl();
		s.getDropTable(con, obj)
	}

	@Override
	public List<T> listarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T buscar(K k) {
		SqlGen s = new SqlGenImpl();
		s.getSqlSelectAll(con, obj);
	}
	public static void main(String[] args) {
		SqlGen s = new SqlGenImpl();
	}
	@Override
	public void create(T t) {
		SqlGen s = new SqlGenImpl();
		s.getCreateTable(con, obj);
	}
	@Override
	public void delete(T t) {
		SqlGen s = new SqlGenImpl();
		s.getSqlDeleteById(con, obj);
	}

}
