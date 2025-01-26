package model;

import java.io.Serializable;
import java.util.Objects;

import constants.Constants;
import constants.Constants.ROLE;

public class UserBean implements Serializable {
	private String id;			// ID
	private String password;		// パスワード
	private String name;			// ユーザー名
	private String email;		// EMail
	private int role;			// 権限

	public UserBean() { }
	public UserBean(String id, String password, String name, String email, int role) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.email = email;
		this.role = role;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public String getId() { return id; }
	public String getPassword() { return password; }
	public String getName() { return name; }
	public String getEmail() { return email; }
	public int getRole() { return role; }
	public String getRoleName() { return Constants.ROLE_NAME.get(role); }
	public boolean isAdmin() { return role == ROLE.ADMIN.ordinal(); }

	public boolean isPasswordCorrect(String password) {
		return this.password.equals(password);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserBean that = (UserBean) o;
		return id.equals(that.id)
			&& password.equals(that.password)
			&& name.equals(that.name)
			&& email.equals(that.email)
			&& role == that.role;
	}

	@Override
	public int hashCode() {
		  return Objects.hash(id, password, name, email, role);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{" +
			"id='" + id + '\'' +
			", password='" + password + '\'' +
			", name='" + name + '\'' +
			", email='" + email + '\'' +
			", role='" + role + '\'' +
			'}';
	}
}
