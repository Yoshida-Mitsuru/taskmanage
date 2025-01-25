package model;

import java.io.Serializable;
import java.util.Objects;

public class GroupBean implements Serializable {
	private int id;				// ID
	private String name;			// ユーザー名
	private String description;	// 説明

	public GroupBean() { }
	public GroupBean(String name, String description) {
		this(0, name, description);
	}
	public GroupBean(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getId() { return id; }
	public String getName() { return name; }
	public String getDescription() { return description; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GroupBean groupBean = (GroupBean) o;
		return id == groupBean.id
			&& name.equals(groupBean.name)
			&& description.equals(groupBean.description);
	}

	@Override
	public int hashCode() {
		  return Objects.hash(id, name, description);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{" +
			"id='" + id + '\'' +
			", name='" + name + '\'' +
			", description='" + description + '\'' +
			'}';
	}
}
