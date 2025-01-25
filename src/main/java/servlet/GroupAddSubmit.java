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
import jakarta.servlet.http.HttpSession;
import model.GroupBean;

@WebServlet("/groupAddSubmit")
public class GroupAddSubmit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = "";
		// リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		GroupBean group = new GroupBean(name, description);
		boolean isSuccess = false;
		try (TransactionManager trans = new TransactionManager()) {
			GroupTableDAO groupTableDAO = new GroupTableDAO(trans);
			if(isSuccess = groupTableDAO.create(group)) {
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
			response.sendRedirect("groupList");
		} else {
			// メッセージをリクエストスコープに保存
			request.setAttribute("message", message);
			// ユーザー情報をリクエストスコープに保存
			request.setAttribute("editUser", group);
			// ユーザー一覧画面にフォワード
			RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/groupAdd.jsp");
			dispatcher.forward(request, response);
		}
	}
}