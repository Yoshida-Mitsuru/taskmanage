package servlet;

import java.io.IOException;
import java.sql.SQLException;

import constants.InitialData;
import dao.GroupTableDAO;
import dao.GroupUserRelationDAO;
import dao.TransactionManager;
import dao.UserTableDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.GroupBean;
import model.GroupUserRelationBean;
import model.UserBean;

@WebServlet("/dataInitialize")
public class dataInitialize extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = "";
		try (TransactionManager trans = new TransactionManager()) {
			// グループユーザー関係テーブルクリア
			GroupUserRelationDAO groupUserRelationDAO = new GroupUserRelationDAO(trans);
			groupUserRelationDAO.truncate();
			// ユーザーテーブル
			UserTableDAO userTableDAO = new UserTableDAO(trans);
			userTableDAO.truncate();
			for (UserBean user : InitialData.userList) {
				userTableDAO.create(user);
			}
			// グループテーブル
			GroupTableDAO groupTableDAO = new GroupTableDAO(trans);
			groupTableDAO.truncate(InitialData.groupList.size()+1);
			for (GroupBean group : InitialData.groupList) {
				groupTableDAO.create(group);
			}
			// グループユーザー関係テーブル
			for (GroupUserRelationBean relation : InitialData.groupUserRelation) {
				groupUserRelationDAO.create(relation);
			}

			trans.commit();
			message = "正常に初期化されました";
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException("エラーが発生しました");
		}

		// セッションスコープを破棄
		HttpSession session = request.getSession();
		session.invalidate();

		// メッセージをリクエストスコープに保存
		request.setAttribute("message", message);
		// 結果画面にフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/dataInitialize.jsp");
		dispatcher.forward(request, response);
	}
}