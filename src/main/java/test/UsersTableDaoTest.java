package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import dao.UsersTableDAO;
import model.UserBean;

public class UsersTableDaoTest {
	static final String TEST_USER_ID = "testdata99";
	static UsersTableDAO target = new UsersTableDAO();

	@Test
	void 全データ取得() throws Exception {
    		List<UserBean> expected = Arrays.asList(
    			new UserBean("sa", "sa1234", "システム管理者", "yoshida_mitsuru@ymail.ne.jp", 0),
    			new UserBean("user", "1234", "吉田　満", "yoshida_mitsuru@ymail.ne.jp", 1)
    		);
    		List<UserBean> actual = target.findAll();
    		assertEquals(expected.size(), actual.size());
    		assertIterableEquals(expected, actual);
	}

	@Test
	void 存在するデータ取得() throws Exception {
    		UserBean expected = new UserBean("sa", "sa1234", "システム管理者", "yoshida_mitsuru@ymail.ne.jp", 0);
    		UserBean actual = target.find("sa","sa1234");
    		assertEquals(expected, actual);
	}

	@Test
	void 存在しないID() throws Exception {
		SQLException exception = assertThrows(SQLException.class, () -> {
			target.find("not_exist");
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
	void 追加と更新と削除() throws Exception {
		//事前確認
		assertFalse(target.delete(TEST_USER_ID));
		全データ取得();

		try {
			//追加
			UserBean user = new UserBean(TEST_USER_ID, "hogehoge", "テスト", "test@example.com", 1);
			assertTrue(target.create(user));
			List<UserBean> expected = Arrays.asList(
				new UserBean("sa", "sa1234", "システム管理者", "yoshida_mitsuru@ymail.ne.jp", 0),
				new UserBean("user", "1234", "吉田　満", "yoshida_mitsuru@ymail.ne.jp", 1),
				user
			);
			List<UserBean> actual = target.findAll();
			assertEquals(expected.size(), actual.size());
			assertIterableEquals(expected, actual);

			//更新
			user.setName("fugafuga");
			assertTrue(target.update(user));
			actual = target.findAll();
			assertEquals(expected.size(), actual.size());
			assertIterableEquals(expected, actual);

			//削除
			assertTrue(target.delete(TEST_USER_ID));
			全データ取得();
		} finally {
			target.delete(TEST_USER_ID);
		}
	}

	@Test
	void ID被り() throws Exception {
		全データ取得();
		UserBean user = new UserBean("user", "hogehoge", "テスト", "test@example.com", 1);
		assertFalse(target.create(user));
	}

	@Test
	void 削除対象なし() throws Exception {
		全データ取得();
		assertFalse(target.delete(TEST_USER_ID));
	}
}
