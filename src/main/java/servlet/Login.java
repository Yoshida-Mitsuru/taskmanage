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

@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		UserBean user = null;
		try (TransactionManager trans = new TransactionManager()) {
			UsersTableDAO usersTableDAO = new UsersTableDAO(trans);
			user = usersTableDAO.find(id, pass);
		} catch (SQLException e) {
			String message = "";
			if(e.getMessage().equals("データが存在しません")) {
				message = "IDが違います";
			} else if(e.getMessage().equals("パスワードが違います")) {
				message = "パスワードが違います";
			}
			// メッセージをリクエストスコープに保存
			request.setAttribute("message", message);
		}

		if(user != null) {
			// ユーザー情報をセッションスコープに保存
			HttpSession session = request.getSession();
			session.setAttribute("loginUser", user);
	
			// メインメニュー画面にリダイレクト
			response.sendRedirect("mainMenu");
		} else {
			// ログイン失敗画面にフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/login.jsp");
			dispatcher.forward(request, response);
		}
	}
}