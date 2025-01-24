package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import constants.Constants;
import model.GroupBean;
import model.GroupUserRelationBean;
import model.UserBean;

public class GroupUserRelationDAO {
	private final String USER_GROUP_RELATION = Constants.USER_GROUP_RELATION;
	private final String USER_TABLE = Constants.USER_TABLE;
	private final String GROUP_TABLE = Constants.GROUP_TABLE;
	private final TransactionManager trans;

	public GroupUserRelationDAO(TransactionManager trans) {
		super();
		this.trans = trans;
	}

	public List<GroupUserRelationBean> findAll() throws SQLException {
		List<GroupUserRelationBean> relationList = new ArrayList<GroupUserRelationBean>();
		try {
			String sql = "SELECT GROUP_ID, USER_ID FROM " + USER_GROUP_RELATION
					+ " ORDER BY GROUP_ID, USER_ID";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
			  	ResultSet rs = pStmt.executeQuery();
				while (rs.next()) {
			  		GroupUserRelationBean relation = new GroupUserRelationBean(
			  			rs.getInt("GROUP_ID"),
			  			rs.getString("USER_ID")
			  		);
			  		relationList.add(relation);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return relationList;
	}

	public List<GroupBean> findGroupsByUserId(String userId) throws SQLException {
		List<GroupBean> groupList = new ArrayList<GroupBean>();
		try {
			String sql = "SELECT G.ID, G.NAME, G.DESCRIPTION FROM "+GROUP_TABLE+" G"
					+ " JOIN "+USER_GROUP_RELATION+" UG ON G.ID = UG.GROUP_ID"
					+ " WHERE UG.USER_ID = ? ORDER BY G.ID";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
			  	pStmt.setString(1, userId);
			  	ResultSet rs = pStmt.executeQuery();
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
		return groupList;
	}

	public List<UserBean> findUsersByGroupId(int groupId) throws SQLException {
		List<UserBean> userList = new ArrayList<UserBean>();
		try {
			String sql = "SELECT U.ID, U.PASSWORD, U.NAME, U.EMAIL, U.ROLE"
					+ " FROM "+USER_TABLE+" U"
					+ " JOIN "+USER_GROUP_RELATION+" UG ON U.ID = UG.USER_ID"
					+ " WHERE UG.GROUP_ID = ? ORDER BY U.ID";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
			  	pStmt.setInt(1, groupId);
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
		return userList;
	}

	public boolean create(GroupUserRelationBean relation) throws SQLException {
		try {
			String sql = "INSERT INTO "+USER_GROUP_RELATION+"(GROUP_ID,USER_ID) VALUES(?, ?)";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
			  	pStmt.setInt(1, relation.getGroupId());
			  	pStmt.setString(2, relation.getUserId());
				int affectedRows = pStmt.executeUpdate();
				if (affectedRows != 1) {
					return false;
				}
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new SQLException("キー制約違反です");
		}
		return true;
	}

	// １件削除
	public boolean delete(int groupId, String userID) throws SQLException {
		try {
			String sql = "DELETE FROM "+USER_GROUP_RELATION+" WHERE GROUP_ID = ? AND USER_ID = ?";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
				pStmt.setInt(1, groupId);
				pStmt.setString(2, userID);
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

	// グループ指定削除
	public boolean delete(int groupId) throws SQLException {
		try {
			String sql = "DELETE FROM "+USER_GROUP_RELATION+" WHERE GROUP_ID = ?";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
				pStmt.setInt(1, groupId);
				pStmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// ユーザー指定削除
	public boolean delete(String userId) throws SQLException {
		try {
			String sql = "DELETE FROM "+USER_GROUP_RELATION+" WHERE USER_ID = ?";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
				pStmt.setString(1, userId);
				pStmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean truncate() throws SQLException {
		try {
			// TRUNCATEでは復元できないためDELETE
			// String sql = "TRUNCATE TABLE "+USER_GROUP_RELATION;
			String sql = "DELETE FROM "+USER_GROUP_RELATION;
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