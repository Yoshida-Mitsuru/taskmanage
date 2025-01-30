package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.GroupUserRelationDAO;
import dao.TransactionManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.GroupBean;
import model.GroupUserRelationBean;

@WebServlet("/userMembershipsSubmit")
public class UserMembershipsSubmit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = "";
		// リクエストパラメータの取得
		request.setCharacterEncoding("UTF-8");
		String userId = request.getParameter("id");
		// チェックボックスの値を受け取る
		String[] membershipValues = request.getParameterValues("membership");
		List<Integer> memberships = new ArrayList<>();
		if(membershipValues != null) {
			for(String membershipValue : membershipValues) {
				memberships.add(Integer.parseInt(membershipValue));
			}
		}
		// TODO logicの分離
		try (TransactionManager trans = new TransactionManager()) {
			GroupUserRelationDAO relationDAO = new GroupUserRelationDAO(trans);
			List<GroupBean> groups = relationDAO.findGroupsByUserId(userId);
			// 追加（membershipにあり、関係テーブルにない）
			for (int groupId : memberships) {
				boolean contains = groups.stream()
					.anyMatch(group -> group.getId() == groupId);
				if(!contains) {
					GroupUserRelationBean relation = new GroupUserRelationBean(groupId, userId);
					relationDAO.create(relation);
				}
			}
			// 削除（関係テーブルにあり、membershipにない）
			for (GroupBean group : groups) {
				boolean contains = memberships.stream()
					.anyMatch(membership -> membership == group.getId());
				if(!contains) {
					relationDAO.delete(group.getId(), userId);
				}
			}
			trans.commit();
			message = "所属グループが正常に更新されました";
		} catch (SQLException|NumberFormatException e) {
			e.printStackTrace();
			throw new ServletException("エラーが発生しました");
		}

		// メッセージをリクエストスコープに保存
		request.setAttribute("message", message);

		// ユーザー編集画面にフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("userEdit");
		dispatcher.forward(request, response);
	}
}