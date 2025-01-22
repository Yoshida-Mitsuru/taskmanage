package constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import constants.Constants.ROLE;
import model.GroupBean;
import model.UserBean;

public final class InitialData {
	public final static List<UserBean> userList = Collections.unmodifiableList(
		Arrays.asList(
			new UserBean("sa", "sa", "システム管理者", "yoshida_mitsuru@ymail.ne.jp", ROLE.ADMIN.ordinal()),
			new UserBean("user", "user", "吉田　満", "yoshida_mitsuru@ymail.ne.jp", ROLE.USER.ordinal()),
			new UserBean("user1", "111", "富山　太郎", "", ROLE.USER.ordinal()),
			new UserBean("user2", "222", "立山　花子", "", ROLE.USER.ordinal()),
			new UserBean("user3", "333", "石川　次郎", "", ROLE.USER.ordinal()),
			new UserBean("user4", "444", "田中　翔平", "", ROLE.USER.ordinal())
		)
	);

	public final static List<GroupBean> groupList = Collections.unmodifiableList(
			Arrays.asList(
				new GroupBean(1, "システム", "テスト用グループです"),
				new GroupBean(2, "HOGE プロジェクトグループ", "HOGE プロジェクト"),
				new GroupBean(3, "FUGA プロジェクトグループ", "FUGA プロジェクト")
			)
		);

	// コンストラクタをプライベートにしてインスタンス化を防止
	private InitialData() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}
}
