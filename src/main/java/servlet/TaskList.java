package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.TaskTableDAO;
import dao.TransactionManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.TaskWithNameBean;
import model.UserBean;

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
		UserBean user = (UserBean) request.getSession().getAttribute("loginUser");

		try (TransactionManager trans = new TransactionManager()) {
			TaskTableDAO taskTableDAO = new TaskTableDAO(trans);
			List<TaskWithNameBean> taskList = taskTableDAO.findByUserId(user.getId());
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