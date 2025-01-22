package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import constants.Constants;
import model.UserBean;

public class UserTableDAO {
	private final String USER_TABLE = Constants.USER_TABLE;
	private final TransactionManager trans;

	public UserTableDAO(TransactionManager trans) {
		super();
		this.trans = trans;
	}

	public List<UserBean> findAll() throws SQLException, IllegalStateException {
		List<UserBean> userList = new ArrayList<UserBean>();
		try {
			String sql = "SELECT ID,PASSWORD,NAME,EMAIL,ROLE FROM "+USER_TABLE+" ORDER BY ROLE";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
			  	ResultSet rs = pStmt.executeQuery();
				while (rs.next()) {
					UserBean user = new UserBean(
						rs.getString("ID"),
						rs.getString("PASSWORD"),
						rs.getString("NAME"),
						rs.getString("EMAIL"),
						rs.getInt("ROLE")
					);
					userList.add(user);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return userList ;
	}

	public UserBean find(String id) throws SQLException {
		String sql = "SELECT ID,PASSWORD,NAME,EMAIL,ROLE FROM "+USER_TABLE+" WHERE ID=?";
		try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
		  	pStmt.setString(1, id);
		  	ResultSet rs = pStmt.executeQuery();
		  	if (rs.next()) {
		  		return new UserBean(
			  			rs.getString("ID"),
			  			rs.getString("PASSWORD"),
			  			rs.getString("NAME"),
			  			rs.getString("EMAIL"),
			  			rs.getInt("ROLE")
			  		);
		  	} else {
		  		throw new SQLException("データが存在しません");
		  	}
		}
	}

	public UserBean find(String id, String password) throws SQLException {
		UserBean user = find(id);
		if (!user.isPasswordCorrect(password)) throw new SQLException("パスワードが違います");
		return user;
	}

	public boolean create(UserBean user) throws SQLException {
		try {
			String sql = "INSERT INTO "+USER_TABLE+"(ID,PASSWORD,NAME,EMAIL,ROLE) VALUES(?, ?, ?, ?, ?)";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
			  	pStmt.setString(1, user.getId());
			  	pStmt.setString(2, user.getPassword());
			  	pStmt.setString(3, user.getName());
			  	pStmt.setString(4, user.getEmail());
			  	pStmt.setInt(5, user.getRole());
				int affectedRows = pStmt.executeUpdate();
				if (affectedRows != 1) {
					return false;
				}
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new SQLException("すでに存在するデータです");
		}
		return true;
	}

	public boolean delete(String id) throws SQLException {
		try {
			String sql = "DELETE FROM "+USER_TABLE+" WHERE ID = ?";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
				pStmt.setString(1, id);
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

	public boolean update(UserBean user) throws SQLException {
		try {
			String sql = "UPDATE "+USER_TABLE+" SET PASSWORD=?, NAME=?, EMAIL=?, ROLE=? WHERE ID=?";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
			  	pStmt.setString(1, user.getPassword());
			  	pStmt.setString(2, user.getName());
			  	pStmt.setString(3, user.getEmail());
			  	pStmt.setInt(4, user.getRole());
			  	pStmt.setString(5, user.getId());
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
//TODO テーブル名　外部定数化
	public boolean truncate() throws SQLException {
		try {
			// 外部キー制約のためTRUNCATE不可
			String sql = "DELETE FROM "+USER_TABLE;
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