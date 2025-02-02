package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import constants.Constants.ROLE;
import constants.Constants.TASK_PRIORITY;
import constants.Constants.TASK_STATUS;
import dao.GroupTableDAO;
import dao.GroupUserRelationDAO;
import dao.TaskTableDAO;
import dao.TransactionManager;
import dao.UserTableDAO;
import model.GroupBean;
import model.GroupUserRelationBean;
import model.TaskBean;
import model.TaskWithNameBean;
import model.UserBean;

public class TaskTableDaoTest {
	private TransactionManager trans = null;
	private TaskTableDAO target = null;
	private GroupUserRelationDAO relationDao = null;
	private UserTableDAO userDao = null;
	private GroupTableDAO groupDao = null;
	private List<TaskBean> tasks = null;
	private List<GroupBean> groups = null;
	private List<UserBean> users = null;
	private List<GroupUserRelationBean> relations = null;

	@BeforeEach
	public void setUp() throws SQLException, IOException {
		trans = new TransactionManager();
		target = new TaskTableDAO(trans);
		relationDao = new GroupUserRelationDAO(trans);
		userDao = new UserTableDAO(trans);
		groupDao = new GroupTableDAO(trans);

		// テストデータ
		users = new ArrayList<>(Arrays.asList(
				new UserBean("testuser1", "111", "富山　太郎", "", ROLE.USER.ordinal()),
				new UserBean("testuser2", "222", "立山　花子", "", ROLE.USER.ordinal()),
				new UserBean("testuser3", "333", "石川　次郎", "", ROLE.USER.ordinal())));
		for (UserBean user : users) {
			relationDao.delete(user.getId());
			userDao.delete(user.getId());
			userDao.create(user);
		}
		groups = new ArrayList<>(Arrays.asList(
				new GroupBean("TEST1グループ", "TEST1"),
				new GroupBean("TEST2グループ", "TEST2"),
				new GroupBean("TEST3グループ", "TEST3")));
		for (GroupBean group : groups) {
			groupDao.create(group);
		}
		relations = new ArrayList<>(Arrays.asList(
				new GroupUserRelationBean(groups.get(0).getId(), users.get(0).getId()),
				new GroupUserRelationBean(groups.get(1).getId(), users.get(0).getId()),
				new GroupUserRelationBean(groups.get(2).getId(), users.get(0).getId()),
				new GroupUserRelationBean(groups.get(2).getId(), users.get(1).getId()),
				new GroupUserRelationBean(groups.get(2).getId(), users.get(2).getId())));
		for (GroupUserRelationBean relation : relations) {
			relationDao.create(relation);
		}
		tasks = new ArrayList<>(Arrays.asList(
				new TaskBean(
						"件名1",
						"内容1",
						"testuser1",
						null,
						null,
						groups.get(0).getId(),
						TASK_STATUS.NOT_STARTED.ordinal(),
						TASK_PRIORITY.NORMAL.ordinal()),
				new TaskBean(
						"件名2",
						"内容2",
						"testuser1",
						null,
						null,
						groups.get(1).getId(),
						TASK_STATUS.IN_PROGRESS.ordinal(),
						TASK_PRIORITY.HIGH.ordinal()),
				new TaskBean(
						"件名3",
						"内容3",
						"testuser1",
						null,
						null,
						groups.get(2).getId(),
						TASK_STATUS.SUSPENDED.ordinal(),
						TASK_PRIORITY.URGENT.ordinal()),
				new TaskBean(
						"件名4",
						"内容4",
						"testuser1",
						null,
						null,
						groups.get(2).getId(),
						TASK_STATUS.COMPLETED.ordinal(),
						TASK_PRIORITY.LOW.ordinal()),
				new TaskBean(
						"件名5",
						"内容5",
						"testuser1",
						null,
						null,
						1000,
						TASK_STATUS.COMPLETED.ordinal(),
						TASK_PRIORITY.LOW.ordinal())));
	}

	@AfterEach
	public void tearDown() throws SQLException {
		target = null;
		if (trans != null) {
			try {
				trans.rollback(); // トランザクションのロールバック
			} catch (SQLException e) {
				System.err.println("ロールバック中にエラーが発生しました: " + e.getMessage());
			} finally {
				trans.close(); // コネクションを閉じる
				trans = null;
			}
		}
	}

	@Test
	void 追加() throws Exception {
		for (TaskBean task : tasks) {
			assertTrue(target.create(task));
		}
	}

	@Test
	void 取得１件_ID引き() throws Exception {
		追加();
		TaskWithNameBean actual = target.find(tasks.get(0).getId());
		TaskWithNameBean expect = new TaskWithNameBean(tasks.get(0), users.get(0).getName(), groups.get(0).getName());
		expect.setId(actual.getId());
		assertEquals(expect, actual);
	}

	@Test
	void 取得_ユーザーID引き() throws Exception {
		追加();
		List<TaskWithNameBean> actual = target.findByUserId(users.get(1).getId());
		List<TaskBean> filtedTasks = tasks.stream()
				.filter(task -> task.getGroupId() == groups.get(2).getId())
				.collect(Collectors.toList());
		List<TaskWithNameBean> expect = new ArrayList<TaskWithNameBean>();
		for (TaskBean task : filtedTasks) {
			expect.add(new TaskWithNameBean(task, "富山　太郎", "TEST3グループ"));
		}
		assertEquals(expect, actual);
		assertEquals(2, actual.size());
	}

	@Test
	void 取得１件_存在しないデータ() throws Exception {
		SQLException exception = assertThrows(SQLException.class, () -> {
			target.find(-1);
		});
		assertEquals("データが存在しません", exception.getMessage());
	}

	@Test
	void 件数取得() throws Exception {
		追加();
		List<TaskBean> expected = target.findAll();
		assertEquals(expected.size(), target.getListCount());
	}

	@Test
	void 追加_自動採番() throws Exception {
		assertTrue(target.create(tasks.get(0)));
		assertNotEquals(0, tasks.get(0).getId());
		assertTrue(target.create(tasks.get(1)));
		assertEquals(tasks.get(0).getId() + 1, tasks.get(1).getId());
	}

	@Test
	void 追加_ID指定() throws Exception {
		追加();
		List<TaskBean> expected = target.findAll();
		OptionalInt maxId = expected.stream()
				.mapToInt(TaskBean::getId)
				.max(); // 最大値を求める
		int id = 1;
		if (maxId.isPresent()) {
			id += maxId.getAsInt();
		}
		tasks.get(0).setId(id);
		assertTrue(target.create(tasks.get(0)));
		assertEquals(id, tasks.get(0).getId());
		expected.add(tasks.get(0));
		List<TaskBean> actual = target.findAll();
		assertIterableEquals(expected, actual);
	}

	@Test
	void 全件取得と削除() throws Exception {
		List<TaskBean> expected = target.findAll();
		assertTrue(target.create(tasks.get(0)));
		expected.add(tasks.get(0));
		List<TaskBean> actual = target.findAll();
		assertIterableEquals(expected, actual);

		// 削除
		expected.remove(expected.size() - 1);
		assertTrue(target.delete(tasks.get(0).getId()));
		actual = target.findAll();
		assertIterableEquals(expected, actual);
	}

	@Test
	void 削除対象なし() throws Exception {
		assertFalse(target.delete(-1));
	}

	@Test
	void テーブルクリア() throws Exception {
		// クリア
		assertTrue(target.truncate());
		List<TaskBean> actual = target.findAll();
		assertEquals(0, actual.size());
	}
}
