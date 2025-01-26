package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.GroupUserRelationDAO;
import dao.TransactionManager;
import dao.UserTableDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.GroupWithRelationBean;
import model.UserBean;

@WebServlet("/userEdit")
public class UserEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");
		try (TransactionManager trans = new TransactionManager()) {
			UserTableDAO userTableDAO = new UserTableDAO(trans);
			UserBean user = userTableDAO.find(id);
			// ユーザー情報をリクエストスコープに保存
			request.setAttribute("editUser", user);

			GroupUserRelationDAO relationDAO = new GroupUserRelationDAO(trans);
			List<GroupWithRelationBean> groupList = relationDAO.getGroupsWithRelationByUserId(user.getId());
			// グループリストをリクエストスコープに保存
			request.setAttribute("groupList", groupList);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException("対象のユーザーが存在しません");
		}

		// ユーザー編集画面にフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/userEdit.jsp");
		dispatcher.forward(request, response);
	}
}