package servlet;

import java.io.IOException;
import java.sql.SQLException;

import constants.InitialData;
import dao.TransactionManager;
import dao.UsersTableDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserBean;

@WebServlet("/dataInitialize")
public class dataInitialize extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = "";
		try (TransactionManager trans = new TransactionManager()) {
			//ユーザーテーブル
			UsersTableDAO usersTableDAO = new UsersTableDAO(trans);
			usersTableDAO.truncate();
			for (UserBean user : InitialData.userList) {
				usersTableDAO.create(user);
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