package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.OptionalInt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dao.GroupTableDAO;
import dao.GroupUserRelationDAO;
import dao.TransactionManager;
import model.GroupBean;

public class GroupTableDaoTest {
	private TransactionManager trans = null;
	private GroupUserRelationDAO relationDAO = null;
	private GroupTableDAO target = null;
	private GroupBean testGroup = null;

	@BeforeEach
	public void setUp() throws SQLException, IOException {
		trans = new TransactionManager();
		relationDAO = new GroupUserRelationDAO(trans);
		target = new GroupTableDAO(trans);
		testGroup = new GroupBean("hogehoge", "テスト");
	}

	@AfterEach
	public void tearDown() throws SQLException {
		relationDAO = null;
		target = null;
		// トランザクションのロールバック
		if (trans != null) {
			trans.rollback();
			trans.close();
		}
	}

	@Test
	void 追加_自動採番() throws Exception {
		assertTrue(target.create(testGroup));
		assertNotEquals(0, testGroup.getId());
	}

	@Test
	void 取得１件_存在するデータ() throws Exception {
		追加_自動採番();
		GroupBean actual = target.find(testGroup.getId());
		assertNotEquals(0, actual.getId());
		assertEquals(testGroup, actual);
	}

	@Test
	void 取得１件_存在しないデータ() throws Exception {
		SQLException exception = assertThrows(SQLException.class, () -> {
			target.find(-1);
		});
		assertEquals("データが存在しません", exception.getMessage());
	}

	@Test
	void 全件取得と削除() throws Exception {
		List<GroupBean> expected = target.findAll();
		expected.add(testGroup);
		追加_自動採番();
		List<GroupBean> actual = target.findAll();
		assertIterableEquals(expected, actual);

		// 削除
		expected.remove(expected.size() - 1);
		assertTrue(target.delete(testGroup.getId()));
		actual = target.findAll();
		assertIterableEquals(expected, actual);
	}

	@Test
	void 追加_ID指定() throws Exception {
		List<GroupBean> expected = target.findAll();
		OptionalInt maxId = expected.stream()
				.mapToInt(GroupBean::getId)
				.max(); // 最大値を求める
		int id = 0;
		if (maxId.isPresent()) {
			id = maxId.getAsInt() + 100;
		} else {
			id = 1;
		}
		testGroup.setId(id);
		assertTrue(target.create(testGroup));
		assertEquals(id, testGroup.getId());
		expected.add(testGroup);
		List<GroupBean> actual = target.findAll();
		assertIterableEquals(expected, actual);
	}

	@Test
	void 削除対象なし() throws Exception {
		assertFalse(target.delete(-1));
	}

	@Test
	void 更新() throws Exception {
		追加_自動採番();
		testGroup.setName("fugafuga");
		testGroup.setDescription("abcdefgh");
		assertTrue(target.update(testGroup));
		GroupBean actual = target.find(testGroup.getId());
		assertEquals(testGroup, actual);
	}

	@Test
	void テーブルクリア() throws Exception {
		// 現データを取得
		List<GroupBean> expected = target.findAll();

		// キー制約のため連携テーブルクリア
		relationDAO.truncate();

		// クリア
		assertTrue(target.truncate(expected.size() + 1));
		List<GroupBean> actual = target.findAll();
		assertEquals(0, actual.size());

		// 自動採番のリセット確認
		assertTrue(target.create(testGroup));
		assertEquals(expected.size() + 1, testGroup.getId());
		actual = target.findAll();
		assertEquals(testGroup, actual.get(0));
	}
}
