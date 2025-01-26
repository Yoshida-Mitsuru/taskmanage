package model;

import java.util.Objects;

public class GroupWithRelationBean extends GroupBean {
    private boolean membership;	// relationに存在しているか

	public GroupWithRelationBean(int id, String name, String description, boolean membership) {
		super(id, name, description);
		this.membership = membership;
	}
	public GroupWithRelationBean(GroupBean group, boolean membership) {
		super(group.getId(), group.getName(), group.getDescription());
		this.membership = membership;
	}
	public boolean isMembership() {
		return membership;
	}
	public void setMembership(boolean membership) {
		this.membership = membership;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GroupWithRelationBean)) return false;
		if (!super.equals(o)) return false;
        GroupWithRelationBean that = (GroupWithRelationBean) o;
        return membership == that.membership;
	}

	@Override
	public int hashCode() {
		  return Objects.hash(super.hashCode(), membership);
	}

	@Override
	public String toString() {
		return super.toString() + ", membership=" + membership + '}';
	}
}
