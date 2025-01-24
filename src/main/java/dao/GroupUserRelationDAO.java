package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import constants.Constants;
import model.GroupUserRelationBean;

public class GroupUserRelationDAO {
	private final String USER_GROUP_RELATION = Constants.USER_GROUP_RELATION;
	private final String USER_TABLE = Constants.USER_TABLE;
	private final String GROUP_TABLE = Constants.GROUP_TABLE;
	private final TransactionManager trans;

	public GroupUserRelationDAO(TransactionManager trans) {
		super();
		this.trans = trans;
	}

	public List<String> findGroupsByUserId(String userId) throws SQLException {
		List<String> groupList = new ArrayList<String>();
		try {
			String sql = "SELECT G.NAME AS GROUP_NAME FROM "+GROUP_TABLE+" G"
					+ " JOIN "+USER_GROUP_RELATION+" UG ON G.ID = UG.GROUP_ID"
					+ " WHERE UG.USER_ID = ? ORDER BY G.ID";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
			  	pStmt.setString(1, userId);
			  	ResultSet rs = pStmt.executeQuery();
				while (rs.next()) {
					groupList.add(rs.getString("GROUP_NAME"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return groupList;
	}

	public List<String> findUsersByGroupId(int groupId) throws SQLException {
		List<String> groupList = new ArrayList<String>();
		try {
			String sql = "SELECT U.NAME AS USER_NAME FROM "+USER_TABLE+" U"
					+ " JOIN "+USER_GROUP_RELATION+" UG ON U.ID = UG.USER_ID"
					+ " WHERE UG.GROUP_ID = ? ORDER BY U.ID";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
			  	pStmt.setInt(1, groupId);
			  	ResultSet rs = pStmt.executeQuery();
				while (rs.next()) {
					groupList.add(rs.getString("USER_NAME"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return groupList;
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