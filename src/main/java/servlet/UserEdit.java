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

@WebServlet("/userEdit")
public class UserEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");
		UserBean userBean = null;
		try (TransactionManager trans = new TransactionManager()) {
			UsersTableDAO usersTableDAO = new UsersTableDAO(trans);
			userBean = usersTableDAO.find(id);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException("対象のユーザーが存在しません");
		}

		// ユーザー情報をリクエストスコープに保存
		request.setAttribute("editUser", userBean);

		// ユーザー編集画面にフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/userEdit.jsp");
		dispatcher.forward(request, response);
	}
}