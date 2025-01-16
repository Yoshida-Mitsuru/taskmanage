package servlet;

import java.io.IOException;
import java.sql.SQLException;

import dao.UsersTableDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.UserBean;

@WebServlet("/userEditSubmit")
public class UserEditSubmit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = "";
		// リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		int role = Integer.parseInt(request.getParameter("role"));

		UsersTableDAO usersTableDAO = new UsersTableDAO();
		UserBean user = new UserBean(id, password, name, email, role);
		try {
			if(usersTableDAO.update(user)) {
				message = "正常に登録されました";
			} else {
				message = "登録に失敗しました";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException("エラーが発生しました");
		}

		// ユーザー情報をリクエストスコープに保存
		request.setAttribute("editUser", user);
		// メッセージをリクエストスコープに保存
		request.setAttribute("message", message);

		// ユーザー編集画面にフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/userEdit.jsp");
		dispatcher.forward(request, response);
	}
}