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

import constants.Constants.TASK_PRIORITY;
import constants.Constants.TASK_STATUS;
import dao.TaskTableDAO;
import dao.TransactionManager;
import model.TaskBean;

public class TaskTableDaoTest {
	private TransactionManager trans = null;
	private TaskTableDAO target = null;
	private List<TaskBean> tasks = null;

	@BeforeEach
	public void setUp() throws SQLException, IOException {
		trans = new TransactionManager();
		target = new TaskTableDAO(trans);

		// テストデータ
		tasks = new ArrayList<>(Arrays.asList(
			new TaskBean(
				"件名1",
				"内容1",
				"user1",
				null,
				null,
				2,
				TASK_STATUS.NOT_STARTED.ordinal(),
				TASK_PRIORITY.NORMAL.ordinal()
			),
			new TaskBean(
				"件名2",
				"内容2",
				"user1",
				null,
				null,
				2,
				TASK_STATUS.IN_PROGRESS.ordinal(),
				TASK_PRIORITY.HIGH.ordinal()
			),
			new TaskBean(
				"件名3",
				"内容3",
				"user2",
				null,
				null,
				2,
				TASK_STATUS.SUSPENDED.ordinal(),
				TASK_PRIORITY.URGENT.ordinal()
			),
			new TaskBean(
				"件名4",
				"内容4",
				"user3",
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
	void 取得１件_存在するデータ() throws Exception {
		追加();
		TaskBean actual = target.find(tasks.get(0).getId());
		assertNotEquals(0, actual.getId());
		assertEquals(tasks.get(0), actual);
	}

	@Test
	void 取得１件_存在しないデータ() throws Exception {
		SQLException exception = assertThrows(SQLException.class, () -> {
			target.find(-1);
		});
		assertEquals("データが存在しません", exception.getMessage());
	}

	@Test
	void 追加() throws Exception {
		for (TaskBean task : tasks) {
			assertTrue(target.create(task));
		}
	}

	@Test
	void 件数取得() throws Exception {
		assertEquals(0, target.getListCount());
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
