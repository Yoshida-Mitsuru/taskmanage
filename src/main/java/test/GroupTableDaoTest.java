package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dao.GroupTableDAO;
import dao.TransactionManager;
import model.GroupBean;

public class GroupTableDaoTest {
	private TransactionManager trans = null;
	private GroupTableDAO target = null;

	@BeforeEach
	public void setUp() throws SQLException, IOException {
		trans = new TransactionManager();
		target = new GroupTableDAO(trans);
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
	void 存在するデータ取得() throws Exception {
		GroupBean expected = new GroupBean("hogehoge", "テスト");
		int id = target.create(expected);
		expected.setId(id);
		GroupBean actual = target.find(id);
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
		//追加
		GroupBean group = new GroupBean("hogehoge", "テスト");
		List<GroupBean> expected = target.findAll();
		int id = target.create(group);
		group.setId(id);
		expected.add(group);
		List<GroupBean> actual = target.findAll();
		assertEquals(expected.size(), actual.size());
		assertIterableEquals(expected, actual);

		//削除
		expected.remove(expected.size() - 1);
		assertTrue(target.delete(id));
		actual = target.findAll();
		assertEquals(expected.size(), actual.size());
		assertIterableEquals(expected, actual);
	}

	@Test
	void 削除対象なし() throws Exception {
		assertFalse(target.delete(-1));
	}

	@Test
	void テーブルクリア() throws Exception {
		// ID指定追加のテスト兼ねる

		//現データを取得（TRUNCATEはロールバックできないため）
		List<GroupBean> expected = target.findAll();

		//クリア
		assertTrue(target.truncate(expected.size()+1));
		List<GroupBean> actual = target.findAll();
		assertEquals(0, actual.size());

		//データ復元
		for (GroupBean group : expected) {
			assertEquals(group.getId(), target.create(group));
		}
		trans.commit();

		actual = target.findAll();
		assertIterableEquals(expected, actual);
	}
}
