package servlet;

import java.io.IOException;
import java.sql.SQLException;

import dao.TransactionManager;
import dao.UserTableDAO;
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
		UserBean user = null;
		try {
			// リクエストパラメータの取得
			request.setCharacterEncoding("UTF-8");
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			int role = Integer.parseInt(request.getParameter("role"));
			user = new UserBean(id, password, name, email, role);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new ServletException("エラーが発生しました");
		}
		boolean isSuccess = false;
		try (TransactionManager trans = new TransactionManager()) {
			UserTableDAO userTableDAO = new UserTableDAO(trans);
			if(isSuccess = userTableDAO.create(user)) {
				trans.commit();
				message = "正常に登録されました";
			}
		} catch (SQLException e) {
			if(e.getMessage() == "すでに存在するデータです") {
				message = "すでに存在するデータです";
			} else {
				e.printStackTrace();
				throw new ServletException("エラーが発生しました");
			}
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
			RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/userAdd.jsp");
			dispatcher.forward(request, response);
		}
	}
}