package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

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

	@BeforeEach
	public void setUp() throws SQLException, IOException {
		trans = new TransactionManager();
		target = new TaskTableDAO(trans);
		relationDao = new GroupUserRelationDAO(trans);
		userDao = new UserTableDAO(trans);
		groupDao = new GroupTableDAO(trans);
		relationDao.truncate();

		// テストデータ
		users = new ArrayList<>(Arrays.asList(
				new UserBean("testuser1", "111", "富山　太郎", "", ROLE.USER.ordinal()),
				new UserBean("testuser2", "222", "立山　花子", "", ROLE.USER.ordinal()),
				new UserBean("testuser3", "333", "石川　次郎", "", ROLE.USER.ordinal())));
		userDao.truncate();
		for (UserBean user : users) {
			userDao.create(user);
		}
		groups = new ArrayList<>(Arrays.asList(
				new GroupBean("TEST1グループ", "TEST1"),
				new GroupBean("TEST2グループ", "TEST2"),
				new GroupBean("TEST3グループ", "TEST3")));
		groupDao.truncate();
		for (GroupBean group : groups) {
			groupDao.create(group);
		}
		tasks = new ArrayList<>(Arrays.asList(
			new TaskBean(
				"件名1",
				"内容1",
				"testuser1",
				null,
				null,
				2,
				TASK_STATUS.NOT_STARTED.ordinal(),
				TASK_PRIORITY.NORMAL.ordinal()
			),
			new TaskBean(
				"件名2",
				"内容2",
				"testuser1",
				null,
				null,
				2,
				TASK_STATUS.IN_PROGRESS.ordinal(),
				TASK_PRIORITY.HIGH.ordinal()
			),
			new TaskBean(
				"件名3",
				"内容3",
				"testuser1",
				null,
				null,
				2,
				TASK_STATUS.SUSPENDED.ordinal(),
				TASK_PRIORITY.URGENT.ordinal()
			),
			new TaskBean(
				"件名4",
				"内容4",
				"testuser1",
				null,
				null,
				2,
				TASK_STATUS.COMPLETED.ordinal(),
				TASK_PRIORITY.LOW.ordinal()
			)
		));
	}

	@AfterEach
	public void tearDown() throws SQLException {
		target = null;
		// トランザクションのロールバック
		if (trans != null) {
			trans.rollback();
			trans.close();
		}
	}

	@Test
	void 追加() throws Exception {
		for (TaskBean task : tasks) {
			assertTrue(target.create(task));
		}
	}

	@Test
	void 取得１件_存在するデータ() throws Exception {
		追加();
		TaskWithNameBean actual = target.find(tasks.get(0).getId());
		assertNotEquals(0, actual.getId());
		TaskWithNameBean expect = new TaskWithNameBean(tasks.get(0), users.get(0).getName(), groups.get(1).getName());
		expect.setId(actual.getId());
		assertEquals(expect, actual);
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
		List<TaskBean> expected = target.findAll();
		assertEquals(expected.size(), target.getListCount());
	}

	@Test
	void 追加_自動採番() throws Exception {
		追加();
		assertNotEquals(0, tasks.get(0).getId());
		assertEquals(tasks.get(0).getId()+1, tasks.get(1).getId());
	}

	@Test
	void 追加_ID指定() throws Exception {
		List<TaskBean> expected = target.findAll();
		OptionalInt maxId = expected.stream()
				.mapToInt(TaskBean::getId)
				.max(); // 最大値を求める
		int id = 0;
		if(maxId.isPresent()) {
			id = maxId.getAsInt()+100;
		} else {
			id = 1;
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
		// 現データを取得
		List<TaskBean> expected = target.findAll();

		// クリア
		assertTrue(target.truncate(expected.size()+1));
		List<TaskBean> actual = target.findAll();
		assertEquals(0, actual.size());

		// 自動採番のリセット確認
		assertTrue(target.create(tasks.get(0)));
		assertEquals(expected.size()+1, tasks.get(0).getId());
		actual = target.findAll();
		assertEquals(tasks.get(0), actual.get(0));
	}
}
