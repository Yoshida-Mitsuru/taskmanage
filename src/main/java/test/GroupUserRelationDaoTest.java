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
import dao.GroupTableDAO;
import dao.GroupUserRelationDAO;
import dao.TransactionManager;
import dao.UserTableDAO;
import model.GroupBean;
import model.UserBean;

public class GroupUserRelationDaoTest {
	private TransactionManager trans = null;
	private GroupUserRelationDAO target = null;
	private UserTableDAO userDao = null;
	private GroupTableDAO groupDao = null;
	private List<GroupBean> groups = null;
	private List<UserBean> users = null;

	@BeforeEach
	public void setUp() throws SQLException, IOException {
		trans = new TransactionManager();
		target = new GroupUserRelationDAO(trans);
		userDao = new UserTableDAO(trans);
		groupDao = new GroupTableDAO(trans);
		// テストデータ追加
		users = new ArrayList<>(Arrays.asList(
				new UserBean("testuser1", "111", "富山　太郎", "", ROLE.USER.ordinal()),
				new UserBean("testuser2", "222", "立山　花子", "", ROLE.USER.ordinal()),
				new UserBean("testuser3", "333", "石川　次郎", "", ROLE.USER.ordinal())
			));
		groups = new ArrayList<>(Arrays.asList(
				new GroupBean("TEST1グループ", "TEST1"),
				new GroupBean("TEST2グループ", "TEST2"),
				new GroupBean("TEST3グループ", "TEST3")
			));
		for (GroupBean group : groups) {
			int groupId = groupDao.create(group);
			group.setId(groupId);
		}
		for (UserBean user : users) {
			userDao.create(user);
		}
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
		for (GroupBean group : groups) {
			assertTrue(target.create(group.getId(), users.get(1).getId()));
		}
		List<String> expected = groups.stream()
			.map(GroupBean::getName)
			.collect(Collectors.toList()); 
		List<String> actual = target.findGroupsByUserId(users.get(1).getId());
		assertIterableEquals(expected, actual);
	}

	@Test
	void 削除_1件() throws Exception {
		追加();

		assertTrue(target.delete(groups.get(1).getId(), users.get(1).getId()));
		groups.remove(1);
		List<String> expected = groups.stream()
			.map(GroupBean::getName)
			.collect(Collectors.toList()); 
		List<String> actual = target.findGroupsByUserId(users.get(1).getId());
		assertIterableEquals(expected, actual);
	}

	@Test
	void 削除_グループ指定() throws Exception {
		List<String> expected = new ArrayList<String>();
		for (UserBean user : users) {
			assertTrue(target.create(groups.get(1).getId(), user.getId()));
			expected.add(user.getName());
		}
		List<String> actual = target.findUsersByGroupId(groups.get(1).getId());
		assertIterableEquals(expected, actual);

		assertTrue(target.delete(groups.get(1).getId()));
		actual = target.findUsersByGroupId(groups.get(1).getId());
		assertEquals(0, actual.size());
	}

	@Test
	void 削除_ユーザー指定() throws Exception {
		追加();

		assertTrue(target.delete(users.get(1).getId()));
		List<String> actual = target.findGroupsByUserId(users.get(1).getId());
		assertEquals(0, actual.size());
	}

	@Test
	void キー制約違反_グループ() throws Exception {
		OptionalInt maxId = groups.stream()
			.mapToInt(GroupBean::getId) // IDをintストリームに変換
			.max(); // 最大値を求める
		SQLException exception = assertThrows(SQLException.class, () -> {
			target.create(maxId.getAsInt()+1, users.get(1).getId());
		});
		assertEquals("キー制約違反です", exception.getMessage());
	}

	@Test
	void キー制約違反_ユーザー() throws Exception {
		SQLException exception = assertThrows(SQLException.class, () -> {
			target.create(groups.get(1).getId(), "fugafuga");
		});
		assertEquals("キー制約違反です", exception.getMessage());
	}

	@Test
	void テーブルクリア() throws Exception {
		追加();
		//クリア
		assertTrue(target.truncate());
		List<String> actual = target.findGroupsByUserId(users.get(1).getId());
		assertEquals(0, actual.size());
		actual = target.findUsersByGroupId(groups.get(1).getId());
		assertEquals(0, actual.size());
	}
}
