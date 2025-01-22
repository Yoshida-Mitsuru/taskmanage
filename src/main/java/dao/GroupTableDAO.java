package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import constants.Constants;
import model.GroupBean;

public class GroupTableDAO {
	private final String GROUP_TABLE = Constants.GROUP_TABLE;
	private final String TABLE_COLUMNS = "ID,NAME,DESCRIPTION";
	private final TransactionManager trans;

	public GroupTableDAO(TransactionManager trans) {
		super();
		this.trans = trans;
	}

	public List<GroupBean> findAll() throws SQLException, IllegalStateException {
		List<GroupBean> groupList = new ArrayList<GroupBean>();
		try {
			String sql = "SELECT "+TABLE_COLUMNS+" FROM "+GROUP_TABLE+" ORDER BY ID";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
			  	ResultSet rs = pStmt.executeQuery();
			  	// SELECT文の結果をArrayListに格納
				while (rs.next()) {
					GroupBean group = new GroupBean(
			  			rs.getInt("ID"),
			  			rs.getString("NAME"),
			  			rs.getString("DESCRIPTION")
			  		);
					groupList.add(group);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return groupList ;
	}

	public GroupBean find(int id) throws SQLException {
		String sql = "SELECT "+TABLE_COLUMNS+" FROM "+GROUP_TABLE+" WHERE ID=?";
		try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
		  	pStmt.setInt(1, id);
		  	ResultSet rs = pStmt.executeQuery();
		  	if (rs.next()) {
		  		return new GroupBean(
			  			rs.getInt("ID"),
			  			rs.getString("NAME"),
			  			rs.getString("DESCRIPTION")
			  		);
		  	} else {
		  		throw new SQLException("データが存在しません");
		  	}
		}
	}

	public int create(GroupBean group) throws SQLException {
		try {
			if(group.getId() == 0) {
				// ID自動採番
				String sql = "INSERT INTO " + GROUP_TABLE + "(NAME, DESCRIPTION) VALUES(?, ?)";
				try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
					pStmt.setString(1, group.getName());
					pStmt.setString(2, group.getDescription());
					int affectedRows = pStmt.executeUpdate();
					if (affectedRows > 0) {
						try (ResultSet res = pStmt.getGeneratedKeys()) {
							if (res.next()) {
								return res.getInt(1);
							}
						}
					}
				}
			} else {
				// ID指定
				String sql = "INSERT INTO " + GROUP_TABLE + "(ID, NAME, DESCRIPTION) VALUES(?, ?, ?)";
				try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
					pStmt.setInt(1, group.getId());
					pStmt.setString(2, group.getName());
					pStmt.setString(3, group.getDescription());
					int affectedRows = pStmt.executeUpdate();
					if (affectedRows > 0) {
						return group.getId();
					}
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
			String sql = "DELETE FROM "+GROUP_TABLE+" WHERE ID = ?";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
				pStmt.setInt(1, id);
				int affectedRows = pStmt.executeUpdate();
				if (affectedRows != 1) {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean update(GroupBean user) throws SQLException {
		try {
			String sql = "UPDATE "+GROUP_TABLE+" SET NAME=?, DESCRIPTION=? WHERE ID=?";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
				pStmt.setString(1, user.getName());
				pStmt.setString(2, user.getDescription());
				pStmt.setInt(3, user.getId());
				int affectedRows = pStmt.executeUpdate();
				if (affectedRows != 1) {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean truncate(int initialId) throws SQLException {
		try {
			// 外部キー制約のためTRUNCATE不可
			String sql = "DELETE FROM "+GROUP_TABLE;
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
				pStmt.executeUpdate();
			}
			// 自動採番リセット
			sql = "ALTER TABLE "+GROUP_TABLE+" ALTER COLUMN ID INT AUTO_INCREMENT("+initialId+")";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
				pStmt.executeUpdate();
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}