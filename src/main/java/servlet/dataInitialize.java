package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

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
		List<UserBean> userList = Arrays.asList(
			new UserBean("sa", "sa", "システム管理者", "yoshida_mitsuru@ymail.ne.jp", 0),
			new UserBean("user", "user", "吉田　満", "yoshida_mitsuru@ymail.ne.jp", 1),
			new UserBean("user1", "111", "富山　太郎", "", 1),
			new UserBean("user2", "222", "立山　花子", "", 1),
			new UserBean("user3", "333", "石川　次郎", "", 1),
			new UserBean("user4", "444", "田中　翔平", "", 1)
		);
		try (TransactionManager trans = new TransactionManager()) {
			UsersTableDAO usersTableDAO = new UsersTableDAO(trans);
			usersTableDAO.truncate();
			for (UserBean user : userList) {
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