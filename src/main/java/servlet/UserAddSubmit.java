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
import jakarta.servlet.http.HttpSession;
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
		boolean isSuccess = false;
		try (TransactionManager trans = new TransactionManager()) {
			UsersTableDAO usersTableDAO = new UsersTableDAO(trans);
			if(usersTableDAO.create(user)) {
				trans.commit();
				message = "正常に登録されました";
				isSuccess = true;
			} else {
				trans.rollback();
				message = "すでに存在するIDです";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException("エラーが発生しました");
		}

		if(isSuccess) {
			// メッセージをセッションスコープに保存
			HttpSession session = request.getSession();
			session.setAttribute("message", message);
			// ユーザー一覧画面にリダイレクト
			response.sendRedirect("userList");
		} else {
			// メッセージをリクエストスコープに保存
			request.setAttribute("message", message);
			// ユーザー情報をリクエストスコープに保存
			request.setAttribute("editUser", user);
			// ユーザー一覧画面にフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/userAdd");
			dispatcher.forward(request, response);
		}
	}
}