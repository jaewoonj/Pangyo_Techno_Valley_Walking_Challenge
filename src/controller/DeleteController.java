package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.MemberDAO;
import model.MemberVO;

public class DeleteController implements Controller {

	@Override
	public ModelAndView HandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("회원탈퇴 컨트롤러 입장");
		HttpSession session = request.getSession();
		
		String userId =request.getParameter("userId");
		String userPass = request.getParameter("userPass");

		System.out.println(userId + ", " + userPass);

		int result = MemberDAO.getInstance().deleteMember(userId, userPass);
		if (result == 1) {
			session.invalidate();

		}
		return new ModelAndView("deleteProc.jsp",true);
	}
}
