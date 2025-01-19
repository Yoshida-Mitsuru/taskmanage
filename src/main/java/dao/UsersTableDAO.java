package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.UserBean;

public class UsersTableDAO {
	private final String TABLE_NAME = "USERS_TABLE";
	private final TransactionManager trans;

	public UsersTableDAO(TransactionManager trans) {
		super();
		this.trans = trans;
	}

	public List<UserBean> findAll() throws SQLException, IllegalStateException {
		List<UserBean> userList = new ArrayList<UserBean>();
		try {
			// SELECT文の準備
			String sql = "SELECT USER_ID,PASSWORD,NAME,EMAIL,ROLE FROM "+TABLE_NAME+" ORDER BY ROLE";
		  	PreparedStatement pStmt = trans.getConnection().prepareStatement(sql);

		  	// SELECTを実行
		  	ResultSet rs = pStmt.executeQuery();

		  	// SELECT文の結果をArrayListに格納
			while (rs.next()) {
				String id = rs.getString("USER_ID");
				String password = rs.getString("PASSWORD");
				String name = rs.getString("NAME");
				String email = rs.getString("EMAIL");
				int role = rs.getInt("ROLE");
				UserBean user = new UserBean(id, password, name, email, role);
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return userList ;
	}

	public UserBean find(String id) throws SQLException {
		// SELECT文の準備
		String sql = "SELECT USER_ID,PASSWORD,NAME,EMAIL,ROLE FROM "+TABLE_NAME+" WHERE USER_ID=?";
	  	PreparedStatement pStmt = trans.getConnection().prepareStatement(sql);
	  	pStmt.setString(1, id);

	  	// SELECTを実行
	  	ResultSet rs = pStmt.executeQuery();

	  	if (!rs.next()) {
	  		throw new SQLException("データが存在しません");
	  	} else {
	  		return new UserBean(
	  			rs.getString("USER_ID"),
	  			rs.getString("PASSWORD"),
	  			rs.getString("NAME"),
	  			rs.getString("EMAIL"),
	  			rs.getInt("ROLE")
	  		);
	  	}
	}

	public UserBean find(String id, String password) throws SQLException {
		UserBean user = find(id);
		if (!user.isPasswordCorrect(password)) throw new SQLException("パスワードが違います");
		return user;
	}

	public boolean create(UserBean user) throws SQLException {
		try {
			// INSERT文の準備
			String sql = "INSERT INTO "+TABLE_NAME+"(USER_ID,PASSWORD,NAME,EMAIL,ROLE) VALUES(?, ?, ?, ?, ?)";
		  	PreparedStatement pStmt = trans.getConnection().prepareStatement(sql);
		  	pStmt.setString(1, user.getId());
		  	pStmt.setString(2, user.getPassword());
		  	pStmt.setString(3, user.getName());
		  	pStmt.setString(4, user.getEmail());
		  	pStmt.setInt(5, user.getRole());

		  	// INSERTを実行
			int result = pStmt.executeUpdate();
			if (result != 1) {
				return false;
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new SQLException("すでに存在するデータです");
		}
		return true;
	}

	public boolean delete(String id) throws SQLException {
		try {
			// DELETE文の準備
			String sql = "DELETE FROM "+TABLE_NAME+" WHERE USER_ID = ?";
		  	PreparedStatement pStmt = trans.getConnection().prepareStatement(sql);
		  	pStmt.setString(1, id);

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

	public boolean update(UserBean user) throws SQLException {
		try {
			// UPDATE文の準備
			String sql = "UPDATE "+TABLE_NAME+" SET PASSWORD=?, NAME=?, EMAIL=?, ROLE=? WHERE USER_ID=?";
		  	PreparedStatement pStmt = trans.getConnection().prepareStatement(sql);
		  	pStmt.setString(1, user.getPassword());
		  	pStmt.setString(2, user.getName());
		  	pStmt.setString(3, user.getEmail());
		  	pStmt.setInt(4, user.getRole());
		  	pStmt.setString(5, user.getId());

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

	public boolean truncate() throws SQLException {
		try {
			// SQL文の準備
			String sql = "TRUNCATE TABLE "+TABLE_NAME;
		  	PreparedStatement pStmt = trans.getConnection().prepareStatement(sql);

		  	// UPDATEを実行
			pStmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}