package constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import constants.Constants.ROLE;
import constants.Constants.TASK_PRIORITY;
import constants.Constants.TASK_STATUS;
import model.GroupBean;
import model.GroupUserRelationBean;
import model.TaskBean;
import model.UserBean;

public final class InitialData {
	public final static List<UserBean> userList = Collections.unmodifiableList(
			Arrays.asList(
					new UserBean("sa", "sa", "システム管理者", "yoshida_mitsuru@ymail.ne.jp", ROLE.ADMIN.ordinal()),
					new UserBean("user", "user", "吉田　満", "yoshida_mitsuru@ymail.ne.jp", ROLE.USER.ordinal()),
					new UserBean("user1", "111", "富山　太郎", "", ROLE.USER.ordinal()),
					new UserBean("user2", "222", "立山　花子", "", ROLE.USER.ordinal()),
					new UserBean("user3", "333", "石川　次郎", "", ROLE.USER.ordinal()),
					new UserBean("user4", "444", "田中　翔平", "", ROLE.USER.ordinal())));

	public final static List<GroupBean> groupList = Collections.unmodifiableList(
			Arrays.asList(
					new GroupBean(1, "システム", "テスト用グループです"),
					new GroupBean(2, "HOGE プロジェクトグループ", "HOGE プロジェクト"),
					new GroupBean(3, "FUGA プロジェクトグループ", "FUGA プロジェクト")));

	public final static List<GroupUserRelationBean> groupUserRelation = Collections.unmodifiableList(
			Arrays.asList(
					new GroupUserRelationBean(1, "sa"),
					new GroupUserRelationBean(2, "sa"),
					new GroupUserRelationBean(3, "sa"),
					new GroupUserRelationBean(1, "user"),
					new GroupUserRelationBean(2, "user"),
					new GroupUserRelationBean(2, "user1"),
					new GroupUserRelationBean(3, "user"),
					new GroupUserRelationBean(3, "user2")));

	public final static List<TaskBean> taskList = Collections.unmodifiableList(
			Arrays.asList(
					new TaskBean(
							1,
							"件名1",
							"内容1",
							"user1",
							null,
							null,
							1,
							TASK_STATUS.NOT_STARTED.ordinal(),
							TASK_PRIORITY.NORMAL.ordinal()),
					new TaskBean(
							2,
							"件名2",
							"内容2",
							"user1",
							null,
							null,
							2,
							TASK_STATUS.IN_PROGRESS.ordinal(),
							TASK_PRIORITY.HIGH.ordinal()),
					new TaskBean(
							3,
							"件名3",
							"内容3",
							"user2",
							null,
							null,
							2,
							TASK_STATUS.SUSPENDED.ordinal(),
							TASK_PRIORITY.URGENT.ordinal()),
					new TaskBean(
							4,
							"件名4",
							"内容4",
							"user3",
							null,
							null,
							3,
							TASK_STATUS.COMPLETED.ordinal(),
							TASK_PRIORITY.LOW.ordinal())));

	// コンストラクタをプライベートにしてインスタンス化を防止
	private InitialData() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}
}
