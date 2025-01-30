package model;

import java.sql.Date;
import java.util.Objects;

public class TaskWithNameBean extends TaskBean {
	private String userName; // ユーザー名
	private String groupName; // グループ名

	public TaskWithNameBean(int id, String subject, String content, String postUserId, Date expectDate, Date endDate,
			int groupId, int status, int priority, String userName, String groupName) {
		super(id, subject, content, postUserId, expectDate, endDate, groupId, status, priority);
		this.userName = userName;
		this.groupName = groupName;
	}

	public TaskWithNameBean(TaskBean task, String userName, String groupName) {
		super(task.getId(), task.getSubject(), task.getContent(), task.getPostUserId(), task.getExpectDate(),
				task.getEndDate(), task.getGroupId(), task.getStatus(), task.getPriority());
		this.userName = userName;
		this.groupName = groupName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof TaskWithNameBean))
			return false;
		if (!super.equals(o))
			return false;
		TaskWithNameBean that = (TaskWithNameBean) o;
		return Objects.equals(userName, that.userName) &&
				Objects.equals(groupName, that.groupName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), userName, groupName);
	}

	@Override
	public String toString() {
		return super.toString() + ", userName=" + userName + ", groupName=" + groupName + '}';
	}
}
