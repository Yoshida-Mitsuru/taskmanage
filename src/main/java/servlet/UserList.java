package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.TransactionManager;
import dao.UsersTableDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.UserBean;

@WebServlet("/userList")
public class UserList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		try (TransactionManager trans = new TransactionManager()) {
			UsersTableDAO usersTableDAO = new UsersTableDAO(trans);
			List<UserBean> userList = usersTableDAO.findAll();
			// ユーザーリストをリクエストスコープに保存
			request.setAttribute("userList", userList);
		} catch (SQLException e) {
	  		throw new ServletException();
		}

		// メインメニュー画面にフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/userList.jsp");
		dispatcher.forward(request, response);
	}
}