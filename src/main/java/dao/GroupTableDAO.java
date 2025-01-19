package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.GroupBean;

public class GroupTableDAO {
	private final String TABLE_NAME = "TBL_GROUP";
	private final String TABLE_COLUMNS = "ID,NAME,DESCRIPTION";
	private final TransactionManager trans;

	public GroupTableDAO(TransactionManager trans) {
		super();
		this.trans = trans;
	}

	public List<GroupBean> findAll() throws SQLException, IllegalStateException {
		List<GroupBean> groupList = new ArrayList<GroupBean>();
		try {
			// SELECT文の準備
			String sql = "SELECT "+TABLE_COLUMNS+" FROM "+TABLE_NAME+" ORDER BY ID";
		  	PreparedStatement pStmt = trans.getConnection().prepareStatement(sql);

		  	// SELECTを実行
		  	ResultSet rs = pStmt.executeQuery();

		  	// SELECT文の結果をArrayListに格納
			while (rs.next()) {
				int id = rs.getInt("ID");
				String name = rs.getString("NAME");
				String description = rs.getString("DESCRIPTION");
				GroupBean group = new GroupBean(id, name, description);
				groupList.add(group);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return groupList ;
	}

	public GroupBean find(int id) throws SQLException {
		// SELECT文の準備
		String sql = "SELECT "+TABLE_COLUMNS+" FROM "+TABLE_NAME+" WHERE ID=?";
	  	PreparedStatement pStmt = trans.getConnection().prepareStatement(sql);
	  	pStmt.setInt(1, id);

	  	// SELECTを実行
	  	ResultSet rs = pStmt.executeQuery();

	  	if (!rs.next()) {
	  		throw new SQLException("データが存在しません");
	  	} else {
	  		return new GroupBean(
	  			rs.getInt("ID"),
	  			rs.getString("NAME"),
	  			rs.getString("DESCRIPTION")
	  		);
	  	}
	}

	public int create(GroupBean group) throws SQLException {
		try {
			// INSERT文の準備
			PreparedStatement pStmt = null;
			if(group.getId() == 0) {
				//ID自動採番
				String sql = "INSERT INTO "+TABLE_NAME+"(NAME,DESCRIPTION) VALUES(?, ?)";
			  	pStmt = trans.getConnection().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			  	pStmt.setString(1, group.getName());
			  	pStmt.setString(2, group.getDescription());
			} else {
				//ID指定
				String sql = "INSERT INTO "+TABLE_NAME+"(ID,NAME,DESCRIPTION) VALUES(?, ?, ?)";
			  	pStmt = trans.getConnection().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			  	pStmt.setInt(1, group.getId());
			  	pStmt.setString(2, group.getName());
			  	pStmt.setString(3, group.getDescription());
			}

		  	// INSERTを実行
			int result = pStmt.executeUpdate();
			if (result > 0) {
				 ResultSet res = pStmt.getGeneratedKeys();
				if(res.next()){
					int id = res.getInt(1);
					return id;
				}
			}
			return -1;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("すでに存在するデータです");
		}
	}

	public boolean delete(int id) throws SQLException {
		try {
			// DELETE文の準備
			String sql = "DELETE FROM "+TABLE_NAME+" WHERE ID = ?";
		  	PreparedStatement pStmt = trans.getConnection().prepareStatement(sql);
		  	pStmt.setInt(1, id);

		  	// DELETEを実行
			int result = pStmt.executeUpdate();
			if (result != 1) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean update(GroupBean user) throws SQLException {
		try {
			// UPDATE文の準備
			String sql = "UPDATE "+TABLE_NAME+" SET NAME=?, DESCRIPTION=? WHERE ID=?";
		  	PreparedStatement pStmt = trans.getConnection().prepareStatement(sql);
		  	pStmt.setString(1, user.getName());
		  	pStmt.setString(2, user.getDescription());
		  	pStmt.setInt(3, user.getId());

		  	// UPDATEを実行
			int result = pStmt.executeUpdate();
			if (result != 1) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean truncate(int initialId) throws SQLException {
		try {
			// SQL文の準備
			String sql = "TRUNCATE TABLE "+TABLE_NAME;
		  	PreparedStatement pStmt = trans.getConnection().prepareStatement(sql);
		  	// UPDATEを実行
			pStmt.executeUpdate();

			// 自動採番リセット
			sql = "ALTER TABLE "+TABLE_NAME+" ALTER COLUMN ID INT AUTO_INCREMENT("+initialId+")";
		  	pStmt = trans.getConnection().prepareStatement(sql);
			pStmt.executeUpdate();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}