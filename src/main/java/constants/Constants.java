package constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Constants {
	public static final String APP_NAME = "タスクマネージャー";

	// ユーザーのロール値
	public enum ROLE {
		ADMIN(0),
		USER(1),
		SYSTEM(9);

		@SuppressWarnings("unused")
		private int role;

		private ROLE(int role) {
			this.role = role;
		}
	}
	private static final Map<Integer, String> TEMP_MAP_ROLE = new HashMap<>();
	static {
		TEMP_MAP_ROLE.put(ROLE.ADMIN.ordinal(),  "管理者");
		TEMP_MAP_ROLE.put(ROLE.USER.ordinal(),   "ユーザー");
		TEMP_MAP_ROLE.put(ROLE.SYSTEM.ordinal(), "システム");
	}
	public static final Map<Integer, String> ROLE_NAME = Collections.unmodifiableMap(TEMP_MAP_ROLE);

	// タスクの状態
	public enum TASK_STATUS {
		NOT_STARTED(0),
		IN_PROGRESS(1),
		SUSPENDED(2),
		COMPLETED(3);

		@SuppressWarnings("unused")
		private int status;

		private TASK_STATUS(int status) {
			this.status = status;
		}
	}
	private static final Map<Integer, String> TEMP_MAP_STATUS = new HashMap<>();
	static {
		TEMP_MAP_STATUS.put(TASK_STATUS.NOT_STARTED.ordinal(), "未着手");
		TEMP_MAP_STATUS.put(TASK_STATUS.IN_PROGRESS.ordinal(), "進行中");
		TEMP_MAP_STATUS.put(TASK_STATUS.SUSPENDED.ordinal(),   "中断中");
		TEMP_MAP_STATUS.put(TASK_STATUS.COMPLETED.ordinal(),   "完了");
	}
	public static final Map<Integer, String> STATUS_NAME = Collections.unmodifiableMap(TEMP_MAP_STATUS);

	// タスクの優先度
	public enum TASK_PRIORITY {
		LOW(0),
		NORMAL(1),
		HIGH(2),
		URGENT(3);

		@SuppressWarnings("unused")
		private int priority;

		private TASK_PRIORITY(int priority) {
			this.priority = priority;
		}
	}
	private static final Map<Integer, String> TEMP_MAP_PRIORITY = new HashMap<>();
	static {
		TEMP_MAP_PRIORITY.put(TASK_PRIORITY.LOW.ordinal(),    "低");
		TEMP_MAP_PRIORITY.put(TASK_PRIORITY.NORMAL.ordinal(), "通常");
		TEMP_MAP_PRIORITY.put(TASK_PRIORITY.HIGH.ordinal(),   "高");
		TEMP_MAP_PRIORITY.put(TASK_PRIORITY.URGENT.ordinal(), "緊急");
	}
	public static final Map<Integer, String> PRIORITY_NAME = Collections.unmodifiableMap(TEMP_MAP_PRIORITY);

	// テーブル名
	public static final String GROUP_TABLE = "TBL_GROUP";
	public static final String USER_TABLE = "TBL_USER";
	public static final String USER_GROUP_RELATION = "REL_GROUP_USER";
	public static final String TASK_TABLE = "TBL_TASK";

	// コンストラクタをプライベートにしてインスタンス化を防止
	private Constants() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}
}
