package iqaa.xxzh.msl.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import iqaa.xxzh.msl.bean.Lexicon;

@Repository("lexiconDao")
public class LexiconDaoImpl extends BaseDao<Lexicon> implements LexiconDao{

	@Override
	public List<String> getLexicons() {
		String sql = "select word from _lexicon";
		Connection conn=null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			List<String> list= new LinkedList<>();
			while(rs.next()){
				String word = rs.getString("word");
				list.add(word);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (rs != null)
				try {rs.close();	} catch (SQLException e) {}
			if (st != null)
				try{st.close();}catch(Exception e){}
			if(conn != null)
				try{conn.close();}catch(Exception e){}
		}
	}
	
	@Resource(name="dataSource")
	private DataSource dataSource;

	@SuppressWarnings("unchecked")
	@Override
	public List<Lexicon> getWords() {
		List<?> list = getHibernateTemplate().find("from Lexicon");
		return (List<Lexicon>) list;
	}

}

