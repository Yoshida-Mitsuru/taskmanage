package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import constants.Constants;
import dao.GroupUserRelationDAO;
import dao.TransactionManager;
import dao.UserTableDAO;
import model.UserBean;

public class UserTableDaoTest {
	private TransactionManager trans = null;
	private GroupUserRelationDAO relationDAO = null;
	private UserTableDAO target = null;
	private UserBean testUser = null;
	final String TEST_USER_ID = "testdata99";

	@BeforeEach
	public void setUp() throws SQLException, IOException {
		trans = new TransactionManager();
		relationDAO = new GroupUserRelationDAO(trans);
		target = new UserTableDAO(trans);
		testUser = new UserBean(TEST_USER_ID, "hogehoge", "テスト", "test@example.com", 1);
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
		assertTrue(target.create(testUser));
	}

	@Test
	void 取得１件_存在するデータ() throws Exception {
		追加();
		UserBean actual = target.find(TEST_USER_ID);
		assertEquals(testUser, actual);
	}

	@Test
	void 取得１件_存在しないデータ() throws Exception {
		SQLException exception = assertThrows(SQLException.class, () -> {
			target.find(TEST_USER_ID);
		});
		assertEquals("データが存在しません", exception.getMessage());
	}

	@Test
	void パスワード違い() throws Exception {
		追加();
		SQLException exception = assertThrows(SQLException.class, () -> {
			target.find(TEST_USER_ID, "ng");
		});
		assertEquals("パスワードが違います", exception.getMessage());
	}

	@Test
	void 全件取得と削除() throws Exception {
		List<UserBean> expected = target.findAll();
		expected.add(testUser);
		追加();
		List<UserBean> actual = target.findAll();
		assertIterableEquals(expected, actual);

		//削除
		expected.remove(expected.size() - 1);
		assertTrue(target.delete(TEST_USER_ID));
		actual = target.findAll();
		assertIterableEquals(expected, actual);
	}

	@Test
	void 追加_主キー被り() throws Exception {
		追加();
		SQLException exception = assertThrows(SQLException.class, () -> {
			target.create(testUser);
		});
		assertEquals("すでに存在するデータです", exception.getMessage());
	}

	@Test
	void 削除対象なし() throws Exception {
		assertFalse(target.delete(TEST_USER_ID));
	}

	@Test
	void 更新() throws Exception {
		追加();
		UserBean expected = testUser;
		expected.setPassword("newpass");
		expected.setName("fugafuga");
		expected.setEmail("testuser@example.com");
		expected.setRole(Constants.ROLE.SYSTEM.ordinal());
		assertTrue(target.update(expected));
		UserBean actual = target.find(TEST_USER_ID);
		assertEquals(expected, actual);
	}

	@Test
	void テーブルクリア() throws Exception {
		// キー制約のため連携テーブルクリア
		relationDAO.truncate();

		assertTrue(target.truncate());
		List<UserBean> actual = target.findAll();
		assertEquals(0, actual.size());
	}
}
