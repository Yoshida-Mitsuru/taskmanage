package constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Constants {
	public static final String APP_NAME = "タスクマネージャー";

	public static final int ROLE_ADMIN = 0;
	public static final int ROLE_USER = 1;
	public static final int ROLE_SYSTEM = 9;
	// 定数マップの作成
	private static final Map<Integer, String> TEMP_MAP = new HashMap<>();
	static {
		TEMP_MAP.put(ROLE_ADMIN,  "管理者");
		TEMP_MAP.put(ROLE_USER,   "ユーザー");
		TEMP_MAP.put(ROLE_SYSTEM, "システム");
	}
	public static final Map<Integer, String> ROLE_NAME = Collections.unmodifiableMap(TEMP_MAP);

	// コンストラクタをプライベートにしてインスタンス化を防止
	private Constants() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}
}
