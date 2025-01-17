package servlet;

import java.io.IOException;
import java.sql.SQLException;

import dao.TransactionManager;
import dao.UsersTableDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.UserBean;

@WebServlet("/userAddSubmit")
public class UserAddSubmit extends HttpServlet {
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
		UserBean user = new UserBean(id, password, name, email, role);
		RequestDispatcher dispatcher = null;
		try (TransactionManager trans = new TransactionManager()) {
			UsersTableDAO usersTableDAO = new UsersTableDAO(trans);
			if(usersTableDAO.create(user)) {
				trans.commit();
				message = "正常に登録されました";
				// ユーザー一覧画面にフォワード
				dispatcher = request.getRequestDispatcher("userList");
			} else {
				trans.rollback();
				message = "すでに存在するIDです";
				// ユーザー情報をリクエストスコープに保存
				request.setAttribute("editUser", user);
				// ユーザー一覧画面にフォワード
				dispatcher = request.getRequestDispatcher("userAdd");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException("エラーが発生しました");
		}

		// メッセージをリクエストスコープに保存
		request.setAttribute("message", message);

		// ユーザー編集画面にフォワード
		dispatcher.forward(request, response);
	}
}