package com.webjjang.message.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.webjjang.main.controller.Init;
import com.webjjang.member.vo.LoginVO;
import com.webjjang.message.vo.MessageVO;
import com.webjjang.util.exe.Execute;
import com.webjjang.util.page.PageObject;

// Message Module 에 맞는 메뉴 선택 , 데이터 수집(기능별), 예외 처리
public class MessageController {
	public String execute(HttpServletRequest request) {
		System.out.println("MessageController.execute() --------------------------");
		
    // uri
		String uri = request.getRequestURI();
		Object result = null;
		String jsp = null;
		HttpSession session = request.getSession();
		LoginVO loginVO = (LoginVO) session.getAttribute("login");
		
    // 로그인이 되어 있어야 메시지 메뉴가 보이고 처리가 가능하다.
		String id = loginVO.getId();
		
    // 입력 받는 데이터 선언
		Long no = 0L;
		
		try { // 정상 처리
		
			// 메뉴 처리 : CRUD DB 처리 - Controller - Service - DAO
			switch (uri) {
			
			case "/message/list.do":
				// [MessageController] - (Execute) - MessageListService - MessageDAO.list()
				System.out.println("1.메세지 리스트");
				// 페이지 처리를 위한 객체
				// getInstance - 기본 값이 있고 넘어오는 페이지와 검색 정보를 세팅 처리
				PageObject pageObject = PageObject.getInstance(request);
				// message의 mode 정보 담기
				String strMode = request.getParameter("mode");
				// mode 데이터가 넘어 오는 경우의 처리 기본은 1이다
				if(strMode  !=null && !strMode.equals(""))
					pageObject.setAcceptMode(Integer.parseInt(strMode));
				// 메시지의 주인공 아이디 세팅
				pageObject.setAccepter(id);
				// DB에서 데이터 가져오기 - 가져온 데이터는 List<MessageVO>
				result = Execute.execute(Init.get(uri), pageObject);
				// pageObject 데이터 확인
				System.out.println("MessageController.execute().pageObject = " + pageObject);
				// 가져온 데이터 request에 저장 -> jsp까지 전달된다.
				request.setAttribute("list", result);
				// pageObject 담기
				request.setAttribute("pageObject", pageObject);
				// /WEB-INF/views/ + Message/list + .jsp
				jsp = "message/list";
				break;
			
			
			case "/message/view.do":
				System.out.println("2.일반게시판 글보기");
				// 1. 조회수 1증가(글보기), 2. 일반게시판 글보기 데이터 가져오기 : 글보기, 글수정
				// 넘어오는 글번호와 조회수 1증가를 수집한다.(request에 들어 있다.)
				String strNo = request.getParameter("no");
				no = Long.parseLong(strNo);
				//String strInc = request.getParameter("inc");
				//Long inc = Long.parseLong(strInc);
				MessageVO vo = new MessageVO();
				vo.setNo(no);
				// 받은 메시지인지 확인하는 처리
				if(request.getParameter("accept").equals("1"))
					vo.setAccepterId(id);
				result = Execute.execute(Init.get(uri), vo);
				// 가져온 데이터를 JSP로 보내기 위해서 request에 담는다.
				request.setAttribute("vo", result);
				//JSP에서 보여지는 새로운 메시지를 다시 불러와서 세션에 넣는다.
				loginVO.setNewMsgCnt((Long) Execute.execute(Init.get("/ajax/getNewMsgCnt.do"), no));
				//페이지 정보 - 리스트로 돌아갈때 필
				request.setAttribute("pageObject",PageObject.getInstance(request));
				jsp = "message/view";
				break;
			
			//writeForm은 리스트의 모달 창으로 대신 작성했다						
			case "/message/write.do":
				System.out.println("3.메시지 등록 처리");
				// 데이터 수집(사용자->서버 : form - input - name)
				String accepterId = request.getParameter("accepterId");
				String content = request.getParameter("content");
				// 변수 - vo 저장하고 Service
			  vo = new MessageVO();
				vo.setContent(content);
				vo.setAccepterId(accepterId);
				vo.setSenderId(id); //보내는 사람은 본인이다
				// [MessageController] - MessageWriteService - MessageDAO.write(vo)
				Execute.execute(Init.get(uri), vo);
				session.setAttribute("msg", "메시지가 [" + accepterId + "]님에게 전송되었습니다");
				// jsp 정보 앞에 "redirect:"가 붙어 있어 redirect를
				// 아니면 jsp로 forward로 시킨다.
				jsp = "redirect:list.do?perPageNum=" + request.getParameter("perPageNum");
				break;
			
			case "/Message/updateForm.do":
				System.out.println("4-1.일반게시판 글수정 폼");
				// 사 -> 서버 : 글번호
				no = Long.parseLong(request.getParameter("no"));
				// no맞는 데이터 DB에서 가져온다. MessageViewService
				result = Execute.execute(Init.get("/Message/view.do"),new Long[]{no, 0L});
				// 가져온 데이터를 JSP로 보내기 위해서 request에 담는다.
				request.setAttribute("vo", result);
				// jsp 정보
				jsp = "Message/updateForm";
				break;
			default:
				jsp = "error/404";
				break;
			} // end of switch
		} catch (Exception e) {
			 e.printStackTrace();
			// 예외객체를 jsp에서 사용하기 위해 request에 담는다.
			request.setAttribute("e", e);
			jsp = "error/500";
		} // end of try~catch
		return jsp;
	} // end of execute()
} // end of class
