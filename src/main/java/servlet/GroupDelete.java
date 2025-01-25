package servlet;

import java.io.IOException;
import java.sql.SQLException;

import dao.GroupTableDAO;
import dao.GroupUserRelationDAO;
import dao.TransactionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/groupDelete")
public class GroupDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = "";
		int id = -1;
		try {
			// リクエストパラメータの取得
			request.setCharacterEncoding("UTF-8");
			id = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new ServletException("エラーが発生しました");
		}

		try (TransactionManager trans = new TransactionManager()) {
			// 関係テーブルから消す
			GroupUserRelationDAO relationDAO = new GroupUserRelationDAO(trans);
			GroupTableDAO groupTableDAO = new GroupTableDAO(trans);
			if(relationDAO.delete(id) && groupTableDAO.delete(id)) {
				message = "正常に削除されました";
				trans.commit();
			} else {
				message = "削除に失敗しました";
				trans.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException("エラーが発生しました");
		}

		// メッセージをセッションスコープに保存
		HttpSession session = request.getSession();
		session.setAttribute("message", message);
		// グループ一覧画面にリダイレクト
		response.sendRedirect("groupList");
	}
}