package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import constants.Constants;
import model.TaskBean;
import model.TaskWithNameBean;

public class TaskTableDAO {
	private final String TASK_TABLE = Constants.TASK_TABLE;
	private final String USER_TABLE = Constants.USER_TABLE;
	private final String GROUP_TABLE = Constants.GROUP_TABLE;
	private final String TABLE_COLUMNS = "ID, SUBJECT, CONTENT, POSTUSER_ID, EXPECT_DATE, END_DATE, GROUP_ID, STATUS, PRIORITY";
	private final TransactionManager trans;

	public TaskTableDAO(TransactionManager trans) {
		super();
		this.trans = trans;
	}

	public List<TaskBean> findAll() throws SQLException, IllegalStateException {
		List<TaskBean> taskList = new ArrayList<TaskBean>();
		try {
			String sql = "SELECT " + TABLE_COLUMNS + " FROM " + TASK_TABLE + " ORDER BY ID";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
				ResultSet rs = pStmt.executeQuery();
				// SELECT文の結果をArrayListに格納
				while (rs.next()) {
					TaskBean task = new TaskBean(
							rs.getInt("ID"),
							rs.getString("SUBJECT"),
							rs.getString("CONTENT"),
							rs.getString("POSTUSER_ID"),
							rs.getDate("EXPECT_DATE"),
							rs.getDate("END_DATE"),
							rs.getInt("GROUP_ID"),
							rs.getInt("STATUS"),
							rs.getInt("PRIORITY"));
					taskList.add(task);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return taskList;
	}

	public TaskWithNameBean find(int id) throws SQLException {
		String sql = "SELECT T." + TABLE_COLUMNS + ", U.NAME AS POSTUSER_NAME, G.NAME AS GROUP_NAME"
				+ " FROM " + TASK_TABLE + " T"
				+ " LEFT JOIN " + USER_TABLE + " U ON U.ID = T.POSTUSER_ID"
				+ " LEFT JOIN " + GROUP_TABLE + " G ON G.ID = T.GROUP_ID"
				+ " WHERE T.ID = ? ORDER BY T.ID";
		try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
			pStmt.setInt(1, id);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				return new TaskWithNameBean(
						rs.getInt("ID"),
						rs.getString("SUBJECT"),
						rs.getString("CONTENT"),
						rs.getString("POSTUSER_ID"),
						rs.getDate("EXPECT_DATE"),
						rs.getDate("END_DATE"),
						rs.getInt("GROUP_ID"),
						rs.getInt("STATUS"),
						rs.getInt("PRIORITY"),
						rs.getString("POSTUSER_NAME"),
						rs.getString("GROUP_NAME"));
			} else {
				throw new SQLException("データが存在しません");
			}
		}
	}

	public boolean create(TaskBean task) throws SQLException {
		try {
			if (task.getId() == 0) {
				// ID自動採番
				String sql = "INSERT INTO " + TASK_TABLE
						+ "(SUBJECT, CONTENT, POSTUSER_ID, EXPECT_DATE, END_DATE, GROUP_ID, STATUS, PRIORITY)"
						+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
				try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS)) {
					pStmt.setString(1, task.getSubject());
					pStmt.setString(2, task.getContent());
					pStmt.setString(3, task.getPostUserId());
					pStmt.setDate(4, task.getExpectDate());
					pStmt.setDate(5, task.getEndDate());
					pStmt.setInt(6, task.getGroupId());
					pStmt.setInt(7, task.getStatus());
					pStmt.setInt(8, task.getPriority());
					int affectedRows = pStmt.executeUpdate();
					if (affectedRows > 0) {
						try (ResultSet res = pStmt.getGeneratedKeys()) {
							if (res.next()) {
								task.setId(res.getInt(1));
								return true;
							}
						}
					}
				}
			} else {
				// ID指定
				String sql = "INSERT INTO " + TASK_TABLE
						+ "(ID, SUBJECT, CONTENT, POSTUSER_ID, EXPECT_DATE, END_DATE, GROUP_ID, STATUS, PRIORITY)"
						+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
				try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
					pStmt.setInt(1, task.getId());
					pStmt.setString(2, task.getSubject());
					pStmt.setString(3, task.getContent());
					pStmt.setString(4, task.getPostUserId());
					pStmt.setDate(5, task.getExpectDate());
					pStmt.setDate(6, task.getEndDate());
					pStmt.setInt(7, task.getGroupId());
					pStmt.setInt(8, task.getStatus());
					pStmt.setInt(9, task.getPriority());
					int affectedRows = pStmt.executeUpdate();
					if (affectedRows > 0) {
						return true;
					}
				}
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("すでに存在するデータです");
		}
	}

	public boolean delete(int id) throws SQLException {
		try {
			String sql = "DELETE FROM " + TASK_TABLE + " WHERE ID = ?";
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

	public boolean truncate() throws SQLException {
		return truncate(1);
	}

	public boolean truncate(int initialId) throws SQLException {
		try {
			// 外部キー制約のためTRUNCATE不可
			String sql = "DELETE FROM " + TASK_TABLE;
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
				pStmt.executeUpdate();
			}
			// 自動採番リセット
			sql = "ALTER TABLE " + TASK_TABLE + " ALTER COLUMN ID INT AUTO_INCREMENT(" + initialId + ")";
			try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
				pStmt.executeUpdate();
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public int getListCount() throws SQLException {
		String sql = "SELECT COUNT(*) AS COUNT FROM " + TASK_TABLE;
		try (PreparedStatement pStmt = trans.getConnection().prepareStatement(sql)) {
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("COUNT");
			} else {
				throw new SQLException();
			}
		}
	}
}