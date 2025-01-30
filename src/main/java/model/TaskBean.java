package model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class TaskBean implements Serializable {
	private int id; // ID
	private String subject; // 件名
	private String content; // 内容
	private String postUserId; // 投稿者ID
	private Date expectDate; // 完了予定日
	private Date endDate; // 完了日
	private int groupId; // 対象グループID
	private int status; // 状態
	private int priority; // 優先度

	public TaskBean() {
	}

	public TaskBean(String subject, String content, String postUserId, Date expectDate, Date endDate, int groupId,
			int status, int priority) {
		this(0, subject, content, postUserId, expectDate, endDate, groupId, status, priority);
	}

	public TaskBean(int id, String subject, String content, String postUserId, Date expectDate, Date endDate,
			int groupId, int status, int priority) {
		this.id = id;
		this.subject = subject;
		this.content = content;
		this.postUserId = postUserId;
		this.expectDate = expectDate;
		this.endDate = endDate;
		this.groupId = groupId;
		this.status = status;
		this.priority = priority;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setExpectDate(Date expectDate) {
		this.expectDate = expectDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getId() {
		return id;
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

	public String getPostUserId() {
		return postUserId;
	}

	public Date getExpectDate() {
		return expectDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public int getGroupId() {
		return groupId;
	}

	public int getStatus() {
		return status;
	}

	public int getPriority() {
		return priority;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		TaskBean that = (TaskBean) o;
		return id == that.id &&
				groupId == that.groupId &&
				status == that.status &&
				priority == that.priority &&
				Objects.equals(subject, that.subject) &&
				Objects.equals(content, that.content) &&
				Objects.equals(postUserId, that.postUserId) &&
				Objects.equals(expectDate, that.expectDate) &&
				Objects.equals(endDate, that.endDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, subject, content, postUserId, expectDate, endDate, groupId, status, priority);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{" +
				"id=" + id +
				", subject='" + subject + '\'' +
				", content='" + content + '\'' +
				", postUserId='" + postUserId + '\'' +
				", expectDate=" + expectDate +
				", endDate=" + endDate +
				", groupId=" + groupId +
				", status=" + status +
				", priority=" + priority +
				'}';
	}
}
