package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.GroupTableDAO;
import dao.TransactionManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.GroupBean;

@WebServlet("/groupList")
public class GroupList extends HttpServlet {
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
		// セッションからデータを取得
		String message = (String) request.getSession().getAttribute("message");
		request.getSession().removeAttribute("message");

		try (TransactionManager trans = new TransactionManager()) {
			GroupTableDAO groupTableDAO = new GroupTableDAO(trans);
			List<GroupBean> groupList = groupTableDAO.findAll();
			// ユーザーリストをリクエストスコープに保存
			request.setAttribute("groupList", groupList);
		} catch (SQLException e) {
	  		throw new ServletException();
		}

		// メッセージをリクエストスコープに保存
		request.setAttribute("message", message);
		// メインメニュー画面にフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/groupList.jsp");
		dispatcher.forward(request, response);
	}
}