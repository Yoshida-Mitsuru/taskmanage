package servlet;

import java.io.IOException;
import java.sql.SQLException;

import dao.GroupTableDAO;
import dao.TransactionManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.GroupBean;

@WebServlet("/groupEdit")
public class GroupEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		try (TransactionManager trans = new TransactionManager()) {
			int id = Integer.parseInt(request.getParameter("id"));
			GroupTableDAO groupTableDAO = new GroupTableDAO(trans);
			GroupBean group = groupTableDAO.find(id);
			// ユーザー情報をリクエストスコープに保存
			request.setAttribute("editGroup", group);
		} catch (SQLException|NumberFormatException e) {
			e.printStackTrace();
			throw new ServletException("対象のグループが存在しません");
		}

		// グループ編集画面にフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/groupEdit.jsp");
		dispatcher.forward(request, response);
	}
}