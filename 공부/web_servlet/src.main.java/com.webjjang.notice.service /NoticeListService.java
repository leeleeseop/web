package com.webjjang.notice.service;

import java.util.List;

import com.webjjang.main.dao.DAO;
import com.webjjang.main.service.Service;
import com.webjjang.notice.dao.NoticeDAO;
import com.webjjang.notice.vo.NoticeVO;
import com.webjjang.util.page.PageObject;

public class NoticeListService implements Service {
	private NoticeDAO dao;

	@Override
	public List<NoticeVO> service(Object obj) throws Exception {
		PageObject pageObject = (PageObject) obj              
		//전체 데이터의 개수 
		pageObject.setTotalRow(dao.getTotalRow(pageObject));
		// DB notice에서 리스트 쿼리 실행해서 데이터 가져오기 - 리턴
		// DB 처리는 DAO에서 처리 - NoticeDAO.list()
		// NoticeController - (Execute) - [NoticeListService] - NoticeDAO.list()
		return  dao.list(pageObject) ;
	}

	@Override
	public void setDAO(DAO dao) {
		this.dao=(NoticeDAO)dao;
	}

}
