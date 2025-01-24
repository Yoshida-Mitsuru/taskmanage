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

	@BeforeEach
	public void setUp() throws SQLException, IOException {
		trans = new TransactionManager();
		relationDAO = new GroupUserRelationDAO(trans);
		target = new GroupTableDAO(trans);
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
	void 存在するデータ取得() throws Exception {
		GroupBean expected = new GroupBean("hogehoge", "テスト");
		assertTrue(target.create(expected));
		GroupBean actual = target.find(expected.getId());
		assertNotEquals(0, actual.getId());
		assertEquals(expected, actual);
	}

	@Test
	void 存在しないID() throws Exception {
		SQLException exception = assertThrows(SQLException.class, () -> {
			target.find(-1);
		});

		// 例外のメッセージを確認
		assertEquals("データが存在しません", exception.getMessage());
	}

	@Test
	void 追加と削除_正常() throws Exception {
		// 追加
		GroupBean group = new GroupBean("hogehoge", "テスト");
		List<GroupBean> expected = target.findAll();
		assertTrue(target.create(group));
		expected.add(group);
		List<GroupBean> actual = target.findAll();
		assertEquals(expected.size(), actual.size());
		assertIterableEquals(expected, actual);

		// 削除
		expected.remove(expected.size() - 1);
		assertTrue(target.delete(group.getId()));
		actual = target.findAll();
		assertEquals(expected.size(), actual.size());
		assertIterableEquals(expected, actual);
	}

	@Test
	void 追加_ID指定() throws Exception {
		List<GroupBean> expected = target.findAll();
		OptionalInt maxId = expected.stream()
				.mapToInt(GroupBean::getId)
				.max(); // 最大値を求める
		int id = 1;
		if(maxId.isPresent()) {
			id = maxId.getAsInt()+100;
		}
		GroupBean group = new GroupBean(id, "hogehoge", "テスト");
		assertTrue(target.create(group));
		assertEquals(id, group.getId());
		expected.add(group);
		List<GroupBean> actual = target.findAll();
		assertIterableEquals(expected, actual);
	}

	@Test
	void 削除対象なし() throws Exception {
		assertFalse(target.delete(-1));
	}

	@Test
	void テーブルクリア() throws Exception {
		// 現データを取得
		List<GroupBean> expected = target.findAll();

		// キー制約のため連携テーブルクリア
		relationDAO.truncate();

		// クリア
		assertTrue(target.truncate(expected.size()+1));
		List<GroupBean> actual = target.findAll();
		assertEquals(0, actual.size());

		// 自動採番のリセット確認
		GroupBean group = new GroupBean("hogehoge", "テスト");
		assertTrue(target.create(group));
		assertEquals(expected.size()+1, group.getId());
		actual = target.findAll();
		assertEquals(group, actual.get(0));
	}
}
