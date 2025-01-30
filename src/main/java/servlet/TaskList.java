package servlet;

import java.io.IOException;
import java.sql.SQLException;

import dao.TaskTableDAO;
import dao.TransactionManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.TaskWithNameBean;

@WebServlet("/taskList")
public class TaskList extends HttpServlet {
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
			TaskTableDAO taskTableDAO = new TaskTableDAO(trans);
			TaskWithNameBean taskList = taskTableDAO.find(1);
			// タスクリストをリクエストスコープに保存
			request.setAttribute("taskList", taskList);
		} catch (SQLException e) {
	  		throw new ServletException();
		}

		// メッセージをリクエストスコープに保存
		request.setAttribute("message", message);
		// タスク一覧画面にフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/taskList.jsp");
		dispatcher.forward(request, response);
	}
}