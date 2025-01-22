package constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Constants {
	public static final String APP_NAME = "タスクマネージャー";

	public enum ROLE {
		ADMIN(0),
		USER(1),
		SYSTEM(9);

		@SuppressWarnings("unused")
		private int id;

		private ROLE(int id) {
			this.id = id;
		}
	}
	// 定数マップの作成
	private static final Map<Integer, String> TEMP_MAP = new HashMap<>();
	static {
		TEMP_MAP.put(ROLE.ADMIN.ordinal(),  "管理者");
		TEMP_MAP.put(ROLE.USER.ordinal(),   "ユーザー");
		TEMP_MAP.put(ROLE.SYSTEM.ordinal(), "システム");
	}
	public static final Map<Integer, String> ROLE_NAME = Collections.unmodifiableMap(TEMP_MAP);

	// テーブル名
	public static final String GROUP_TABLE = "TBL_GROUP";
	public static final String USER_TABLE = "TBL_USER";

	// コンストラクタをプライベートにしてインスタンス化を防止
	private Constants() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}
}
