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
import dao.GroupTableDAO;
import dao.GroupUserRelationDAO;
import dao.TransactionManager;
import dao.UserTableDAO;
import model.GroupBean;
import model.GroupUserRelationBean;
import model.GroupWithRelationBean;
import model.UserBean;

public class GroupUserRelationDaoTest {
	private TransactionManager trans = null;
	private GroupUserRelationDAO target = null;
	private UserTableDAO userDao = null;
	private GroupTableDAO groupDao = null;
	private List<GroupBean> groups = null;
	private List<UserBean> users = null;
	private List<GroupUserRelationBean> relations = null;

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
				new UserBean("testuser3", "333", "石川　次郎", "", ROLE.USER.ordinal())));
		for (UserBean user : users) {
			target.delete(user.getId());
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
	}

	@AfterEach
	public void tearDown() throws SQLException {
		target = null;
		userDao = null;
		groupDao = null;
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
		for (GroupUserRelationBean relation : relations) {
			assertTrue(target.create(relation));
		}
	}

	@Test
	void 取得_全件() throws Exception {
		List<GroupUserRelationBean> expect = target.findAll();
		追加();
		expect.addAll(relations);
		List<GroupUserRelationBean> actual = target.findAll();
		assertIterableEquals(expect, actual);
	}

	@Test
	void ユーザーIDからグループ取得() throws Exception {
		追加();
		List<GroupBean> actual = target.findGroupsByUserId(users.get(0).getId());
		assertIterableEquals(groups, actual);
	}

	@Test
	void ユーザーIDから存在フラグ付きグループ取得() throws Exception {
		List<GroupWithRelationBean> expect = target.getGroupsWithRelationByUserId(users.get(2).getId());
		追加();
		for (GroupWithRelationBean group : expect) {
			if(group.getId() == groups.get(2).getId()) {
				group.setMembership(true);
			}
		}

		List<GroupWithRelationBean> actual = target.getGroupsWithRelationByUserId(users.get(2).getId());
		assertNotNull(actual);
		assertIterableEquals(expect, actual);
	}

	@Test
	void 削除_1件() throws Exception {
		List<GroupUserRelationBean> expect = target.findAll();
		追加();
		relations.remove(2);
		expect.addAll(relations);

		assertTrue(target.delete(groups.get(2).getId(), users.get(0).getId()));

		List<GroupUserRelationBean> actual = target.findAll();
		assertIterableEquals(expect, actual);
	}

	@Test
	void 削除_グループ指定() throws Exception {
		追加();

		assertTrue(target.delete(groups.get(1).getId()));
		List<UserBean> actual = target.findUsersByGroupId(groups.get(1).getId());
		assertEquals(0, actual.size());
	}

	@Test
	void 削除_ユーザー指定() throws Exception {
		追加();

		assertTrue(target.delete(users.get(1).getId()));
		List<GroupBean> actual = target.findGroupsByUserId(users.get(1).getId());
		assertEquals(0, actual.size());
	}

	@Test
	void キー制約違反_グループ() throws Exception {
		OptionalInt maxId = groups.stream()
				.mapToInt(GroupBean::getId) // IDをintストリームに変換
				.max(); // 最大値を求める
		GroupUserRelationBean relation = new GroupUserRelationBean(
				maxId.getAsInt() + 1,
				users.get(1).getId());
		SQLException exception = assertThrows(SQLException.class, () -> {
			target.create(relation);
		});
		assertEquals("キー制約違反です", exception.getMessage());
	}

	@Test
	void キー制約違反_ユーザー() throws Exception {
		GroupUserRelationBean relation = new GroupUserRelationBean(
				groups.get(1).getId(),
				"fugafuga");
		SQLException exception = assertThrows(SQLException.class, () -> {
			target.create(relation);
		});
		assertEquals("キー制約違反です", exception.getMessage());
	}

	@Test
	void テーブルクリア() throws Exception {
		追加();
		//クリア
		assertTrue(target.truncate());
		List<GroupBean> actual = target.findGroupsByUserId(users.get(1).getId());
		assertEquals(0, actual.size());
		List<UserBean> actual2 = target.findUsersByGroupId(groups.get(1).getId());
		assertEquals(0, actual2.size());
	}
}
