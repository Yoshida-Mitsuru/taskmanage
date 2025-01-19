package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dao.TransactionManager;
import dao.UsersTableDAO;
import model.UserBean;

public class UsersTableDaoTest {
	private TransactionManager trans = null;
	private UsersTableDAO target = null;
	final String TEST_USER_ID = "testdata99";

	@BeforeEach
	public void setUp() throws SQLException, IOException {
		trans = new TransactionManager();
		target = new UsersTableDAO(trans);
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
		UserBean expected = new UserBean(TEST_USER_ID, "hogehoge", "テスト", "test@example.com", 1);
		assertTrue(target.create(expected));
		UserBean actual = target.find(TEST_USER_ID);
		assertEquals(expected, actual);
	}

	@Test
	void 存在しないID() throws Exception {
		SQLException exception = assertThrows(SQLException.class, () -> {
			target.find(TEST_USER_ID);
		});

		// 例外のメッセージを確認
		assertEquals("データが存在しません", exception.getMessage());
	}

	@Test
	void パスワード違い() throws Exception {
		SQLException exception = assertThrows(SQLException.class, () -> {
			target.find("sa", "ng");
		});

		// 例外のメッセージを確認
		assertEquals("パスワードが違います", exception.getMessage());
	}

	@Test
	void 追加と削除_正常() throws Exception {
		//追加
		UserBean user = new UserBean(TEST_USER_ID, "hogehoge", "テスト", "test@example.com", 1);
		List<UserBean> expected = target.findAll();
		expected.add(user);
		assertTrue(target.create(user));
		List<UserBean> actual = target.findAll();
		assertEquals(expected.size(), actual.size());
		assertIterableEquals(expected, actual);

		//削除
		expected.remove(expected.size() - 1);
		assertTrue(target.delete(TEST_USER_ID));
		actual = target.findAll();
		assertEquals(expected.size(), actual.size());
		assertIterableEquals(expected, actual);
	}

	@Test
	void 追加_主キー被り() throws Exception {
		UserBean user = new UserBean(TEST_USER_ID, "hogehoge", "テスト", "test@example.com", 1);
		assertTrue(target.create(user));
		SQLException exception = assertThrows(SQLException.class, () -> {
			target.create(user);
		});
		// 例外のメッセージを確認
		assertEquals("すでに存在するデータです", exception.getMessage());
	}

	@Test
	void 削除対象なし() throws Exception {
		assertFalse(target.delete(TEST_USER_ID));
	}

	@Test
	void テーブルクリア() throws Exception {
		//現データを取得（TRUNCATEはロールバックできないため）
		List<UserBean> expected = target.findAll();

		//クリア
		assertTrue(target.truncate());
		List<UserBean> actual = target.findAll();
		assertEquals(0, actual.size());

		//データ復元
		for (UserBean user : expected) {
			assertTrue(target.create(user));
		}
		trans.commit();

		actual = target.findAll();
		assertIterableEquals(expected, actual);
	}
}
