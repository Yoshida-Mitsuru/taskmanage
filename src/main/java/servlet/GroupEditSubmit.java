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

@WebServlet("/groupEditSubmit")
public class GroupEditSubmit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = "";
		GroupBean group = null;
		try {
			// リクエストパラメータの取得
			request.setCharacterEncoding("UTF-8");
			int id = Integer.parseInt(request.getParameter("id"));
			String name = request.getParameter("name");
			String description = request.getParameter("description");
			group = new GroupBean(id, name, description);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new ServletException("エラーが発生しました");
		}

		try (TransactionManager trans = new TransactionManager()) {

			GroupTableDAO groupTableDAO = new GroupTableDAO(trans);
			if(groupTableDAO.update(group)) {
				message = "正常に登録されました";
				trans.commit();
			} else {
				message = "登録に失敗しました";
				trans.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException("エラーが発生しました");
		}

		// メッセージをリクエストスコープに保存
		request.setAttribute("message", message);

		// グループ編集画面にフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("groupEdit");
		dispatcher.forward(request, response);
	}
}