package model;

import java.io.Serializable;
import java.util.Objects;

import constants.Constants;

public class UserBean implements Serializable {
	private String userId;		// ID
	private String password;		// パスワード
	private String name;			// ユーザー名
	private String email;		// EMail
	private int role;			// 権限

	public UserBean() { }
	public UserBean(String userId, String password, String name, String email, int role) {
		this.userId = userId;
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
	public String getId() { return userId; }
	public String getPassword() { return password; }
	public String getName() { return name; }
	public String getEmail() { return email; }
	public int getRole() { return role; }
	public String getRoleName() { return Constants.ROLE_NAME.get(role); }
	public boolean isAdmin() { return role == Constants.ROLE_ADMIN; }

	public boolean isPasswordCorrect(String password) {
		return this.password.equals(password);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserBean userBean = (UserBean) o;
		return userId.equals(userBean.userId)
			&& password.equals(userBean.password)
			&& name.equals(userBean.name)
			&& email.equals(userBean.email)
			&& role == userBean.role;
	}

	@Override
	public int hashCode() {
		  return Objects.hash(userId, password, name, email, role);
	}
}
