package model;

import java.io.Serializable;
import java.util.Objects;

public class GroupUserRelationBean implements Serializable {
	private int groupId;			// グループID
	private String userId;		// ユーザーID

	public GroupUserRelationBean() { }
	public GroupUserRelationBean(int groupId, String userId) {
		this.groupId = groupId;
		this.userId = userId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getGroupId() { return groupId; }
	public String getUserId() { return userId; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GroupUserRelationBean groupBean = (GroupUserRelationBean) o;
		return groupId == groupBean.groupId
			&& userId.equals(groupBean.userId);
	}

	@Override
	public int hashCode() {
		  return Objects.hash(groupId, userId);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{" +
				"groupId='" + groupId + '\'' +
				", userId='" + userId + '\'' +
				'}';
	}
}
