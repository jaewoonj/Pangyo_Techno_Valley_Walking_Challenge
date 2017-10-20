package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MemberDAO;


public class ReserveController implements Controller{

	@Override
	public ModelAndView HandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		int selectedTime =Integer.parseInt(request.getParameter("selectedTime"));
		
		String userId = request.getParameter("userId");
		String change = request.getParameter("change");
		String path =null;
		int attendace = 1;
		
		
		
		boolean isReserve=MemberDAO.getInstance().isReservation(userId);
		System.out.println(" reserveController selectedTime:"+selectedTime);
		System.out.println("userId:"+userId);
		System.out.println("리저브 컨트롤러 change값:"+change);
		System.out.println("isReserve:"+isReserve);
		//기존 예약자면  메인페이지 이동 , 최초 예약자는 로그에 넣고 성공페이지 이동, 예약변경자는 로그 없이 성공페이지 이동
		
		//예약이 안됬다면..
		if(isReserve == false ){
			MemberDAO.getInstance().chooseTimeSlot(selectedTime,userId);
			MemberDAO.getInstance().setReserveDate(userId,attendace);
			path = "rsuccess.jsp";
			

		//기존 예약자라면..
		}else{
			System.out.println("리저브 컨트롤러 내부 change값:"+change);
			// 예약 변경이라면
			if(change == null){
				path ="reserve.jsp?reserve=true";
				
			}else if(change.equals("true")){
				MemberDAO.getInstance().chooseTimeSlot(selectedTime, userId);
				path = "rsuccess.jsp?change=true";
			}
			
			
			
			

		}



		return new ModelAndView(path);
	}

}
