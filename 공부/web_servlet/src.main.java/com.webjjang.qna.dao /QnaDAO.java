//20240715
package com.webjjang.qna.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.webjjang.main.dao.DAO;
import com.webjjang.qna.vo.QnaVO;
import com.webjjang.util.db.DB;
import com.webjjang.util.page.PageObject;

public class QnaDAO extends DAO {

	// 필요한 객체 선언 - 상속
	// 접속 정보 - DB 사용 - connection을 가져오게 하는 메서드만 이용

	// 1-1. 리스트 처리
	// QnaController - (Execute) - QnaListService - [QnaDAO.list()]
	public List<QnaVO> list(PageObject pageObject) throws Exception {
		// 결과를 저장할 수 있는 변수 선언.
		List<QnaVO> list = null;
    
		try {
			// 1. 드라이버 확인 - DB
			// 2. 연결
			con = DB.getConnection();
			// 3. sql
			// 4. 실행 객체 & 데이터 세팅
			// pstmt = con.prepareStatement(LIST);
			pstmt = con.prepareStatement(getListSQL(pageObject));
			// 검색에 대한 데이터 세팅 - list()만 사용
			int idx = 0; // pstmt의 순서번호 사용. 먼저 1 증가하고 사용한다.
			idx = setSearchData(pageObject, pstmt, idx);
			pstmt.setLong(++idx, pageObject.getStartRow()); // 기본 값 = 1
			pstmt.setLong(++idx, pageObject.getEndRow()); // 기본 값 = 10
			// 5. 실행
			rs = pstmt.executeQuery();
			// 6. 표시 또는 담기
			if (rs != null) {
				while (rs.next()) {
					// rs - > vo -> list
					// list가 null이면 생성해서 저장할 수 있게 해줘야 한다.
					if (list == null)
						list = new ArrayList<QnaVO>();
					// rs -> vo
					// 데이터 셋팅
					QnaVO vo = new QnaVO();
					vo.setNo(rs.getLong("no"));
					vo.setTitle(rs.getString("title"));
					vo.setId(rs.getString("id"));
					vo.setName(rs.getString("name"));
					vo.setWriteDate(rs.getString("writeDate"));
					vo.setLevNo(rs.getLong("levNo"));
					// vo -> list
					list.add(vo);
				} // end of while
			} // end of if
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			// 7. 닫기
			DB.close(con, pstmt, rs);
		} // end of try ~ catch ~ finally
		// 결과 데이터를 리턴해 준다.
		return list;
	} // end of list()

	// 1-2. 전체 데이터 개수 처리
	// QnaController - (Execute) - QnaListService - [QnaDAO.getTotalRow()]
	public Long getTotalRow(PageObject pageObject) throws Exception {
		// 결과를 저장할 수 있는 변수 선언.
		Long totalRow = null;
		try {
			// 1. 드라이버 확인 - DB
			// 2. 연결
			con = DB.getConnection();
			// 3. sql
			// 4. 실행 객체 & 데이터 세팅
			pstmt = con.prepareStatement(TOTALROW + getSearch(pageObject));
			int idx = 0;
			idx = setSearchData(pageObject, pstmt, idx);
			// 5. 실행
			rs = pstmt.executeQuery();
			// 6. 표시 또는 담기
			if (rs != null && rs.next()) {
				totalRow = rs.getLong(1);
			} // end of if
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			// 7. 닫기
			DB.close(con, pstmt, rs);
		} // end of try ~ catch ~ finally
		// 결과 데이터를 리턴해 준다.
		return totalRow;
	} // end of getTotalRow()

	// 2. 글보기 처리
	// QnaController - (Execute) - QnaViewService - [QnaDAO.view()]
	public QnaVO view(Long no) throws Exception {
		// 결과를 저장할 수 있는 변수 선언.
		QnaVO vo = null;
		try {
			// 1. 드라이버 확인 - DB
			// 2. 연결
			con = DB.getConnection();
			// 3. sql
			// 4. 실행 객체 & 데이터 세팅
			pstmt = con.prepareStatement(VIEW);
			pstmt.setLong(1, no);
			// 5. 실행
			rs = pstmt.executeQuery();
			// 6. 표시 또는 담기
			if (rs != null && rs.next()) {
				// rs -> vo
				vo = new QnaVO();
				vo.setNo(rs.getLong("no"));
				vo.setTitle(rs.getString("title"));
				vo.setContent(rs.getString("content"));
				vo.setId(rs.getString("id"));
				vo.setName(rs.getString("name"));
				vo.setWriteDate(rs.getString("writeDate"));
				vo.setRefNo(rs.getLong("refNo"));
				vo.setOrdNo(rs.getLong("ordNo"));
				vo.setLevNo(rs.getLong("levNo"));
			} // end of if
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("예외 발생 : Qna보기 DB 처리 중 오류 발생");
		} finally {
			// 7. 닫기
			DB.close(con, pstmt, rs);
		} // end of try ~ catch ~ finally
			// 결과 데이터를 리턴해 준다.
		return vo;
	} // end of view()

	// 3-1. 답변하기 현재 순서번호와 같거나 큰 순서 번호 1증가 처리
	// QnaController - (Execute) - QnaViewService - [QnaDAO.increase()]
	public int increaseOrdNo(QnaVO vo) throws Exception {
		// 결과를 저장할 수 있는 변수 선언.
		int result = 0;
		try {
			// 1. 드라이버 확인 - DB
			// 2. 연결
			con = DB.getConnection();
			// 3. sql
			// 4. 실행 객체 & 데이터 세팅
			pstmt = con.prepareStatement(INCREASEORDNO);
			pstmt.setLong(1, vo.getRefNo());
			pstmt.setLong(2, vo.getOrdNo());
			// 5. 실행 - update : executeUpdate() -> int 결과가 나옴.
			result = pstmt.executeUpdate();
			// 6. 표시 또는 담기
			System.out.println("QnaDAO.increaseOrdNo() - 순서본호 1증가 처리 완료");
		} catch (Exception e) {
			e.printStackTrace();
			// 특별한 예외는 그냥 전달한다.
			if (e.getMessage().indexOf("예외 발생") >= 0)
				throw e;
			// 그외 처리 중 나타나는 오류에 대해서 사용자가 볼수 있는 예외로 만들어 전달한다.
			else
				throw new Exception("예외 발생 : 글보기 조회수 DB 처리 중 예외가 발생했습니다.");
		} finally {
			// 7. 닫기
			DB.close(con, pstmt);
		}
		// 결과 데이터를 리턴해 준다.
		return result;
	} // end of increase()

	// 3-2. 질문 답변의 글번호 받아오기
	// QnaController - (Execute) - QnaListService - [QnaDAO.getTotalRow()]
	public Long getNo() throws Exception {
		// 결과를 저장할 수 있는 변수 선언.
		Long no = null;
		try {
			// 1. 드라이버 확인 - DB
			// 2. 연결
			con = DB.getConnection();
			// 3. sql
			// 4. 실행 객체 & 데이터 세팅
			pstmt = con.prepareStatement(NO);
			// 5. 실행
			rs = pstmt.executeQuery();
			// 6. 표시 또는 담기
			if (rs != null && rs.next()) {
				no = rs.getLong(1);
			} // end of if
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			// 7. 닫기
			DB.close(con, pstmt, rs);
		} // end of try ~ catch ~ finally
		// 결과 데이터를 리턴해 준다.
		return no;
	} // end of getNo()

	// 3-3. 질문 답변 등록 처리
	// QnaController - (Execute) - QnaWriteService - [QnaDAO.write()]
	public int write(QnaVO vo) throws Exception {
		// 결과를 저장할 수 있는 변수 선언.
		int result = 0;
		try {
			// 1. 드라이버 확인 - DB
			// 2. 연결
			con = DB.getConnection();
			// 3. sql 
			// 4. 실행 객체 & 데이터 세팅
			if (vo.isQuestion())
				pstmt = con.prepareStatement(QUESTION);
			else
				pstmt = con.prepareStatement(ANSWER);
        pstmt.setLong(1, vo.getNo());
        pstmt.setString(2, vo.getTitle());
        pstmt.setString(3, vo.getContent());
        pstmt.setString(4, vo.getId());
        pstmt.setLong(5, vo.getRefNo());
        pstmt.setLong(6, vo.getOrdNo());
        pstmt.setLong(7, vo.getLevNo());
			if (!vo.isQuestion())
				pstmt.setLong(8, vo.getParentNo());
			// 5. 실행 - update : executeUpdate() -> int 결과가 나옴.
			result = pstmt.executeUpdate();
			// 6. 표시 또는 담기
			System.out.println();
			System.out.println("***" + ((vo.isQuestion()) ? "질문" : "답변") + " 등록이 완료 되었습니다. ");
		} catch (Exception e) {
			e.printStackTrace();
			// 그외 처리 중 나타나는 오류에 대해서 사용자가 볼수 있는 예외로 만들어 전달한다.
			throw new Exception("예외 발생 : 게시판 글등록 DB 처리 중 예외가 발생했습니다.");
		} finally {
			// 7. 닫기
			DB.close(con, pstmt);
		}
		// 결과 데이터를 리턴해 준다.
		return result;
	} // end of increase()


	// 4. 글수정 처리
	// QnaController - (Execute) - QnaUpdateService - [QnaDAO.update()]
	public int update(QnaVO vo) throws Exception {
		// 결과를 저장할 수 있는 변수 선언.
		int result = 0;

		try {
			// 1. 드라이버 확인 - DB
			// 2. 연결
			con = DB.getConnection();
			// 3. sql - 아래 UPDATE
			// 4. 실행 객체 & 데이터 세팅
			pstmt = con.prepareStatement(UPDATE);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setLong(4, vo.getNo());
			// 5. 실행 - update : executeUpdate() -> int 결과가 나옴.
			result = pstmt.executeUpdate();
			// 6. 표시 또는 담기
			if (result == 0) { // 글번호가 존재하지 않는다. -> 예외로 처리한다.
				throw new Exception("예외 발생 : 글번호나 비밀번호가 맞지 않습니다. 정보를 확인해 주세요.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 특별한 예외는 그냥 전달한다.
			if (e.getMessage().indexOf("예외 발생") >= 0)
				throw e;
			// 그외 처리 중 나타나는 오류에 대해서 사용자가 볼수 있는 예외로 만들어 전달한다.
			else
				throw new Exception("예외 발생 : 게시판 글등록 DB 처리 중 예외가 발생했습니다.");
		} finally {
			// 7. 닫기
			DB.close(con, pstmt);
		}

		// 결과 데이터를 리턴해 준다.
		return result;
	} // end of update()

	// 5. 글삭제 처리
	// QnaController - (Execute) - QnaDeleteService - [QnaDAO.delete()]
	public int delete(QnaVO vo) throws Exception {
		// 결과를 저장할 수 있는 변수 선언.
		int result = 0;

		try {
			// 1. 드라이버 확인 - DB
			// 2. 연결
			con = DB.getConnection();
			// 3. sql - 아래 UPDATE
			// 4. 실행 객체 & 데이터 세팅
			pstmt = con.prepareStatement(DELETE);
			pstmt.setLong(1, vo.getNo());
			// 5. 실행 - update : executeUpdate() -> int 결과가 나옴.
			result = pstmt.executeUpdate();
			// 6. 표시 또는 담기
			if (result == 0) { // 글번호가 존재하지 않거나 비번 틀림. -> 예외로 처리한다.
				throw new Exception("예외 발생 : 글번호나 비밀번호가 맞지 않습니다. 정보를 확인해 주세요.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 특별한 예외는 그냥 전달한다.
			if (e.getMessage().indexOf("예외 발생") >= 0)
				throw e;
			// 그외 처리 중 나타나는 오류에 대해서 사용자가 볼수 있는 예외로 만들어 전달한다.
			else
				throw new Exception("예외 발생 : 게시판 글삭제 DB 처리 중 예외가 발생했습니다.");
		} finally {
			// 7. 닫기
			DB.close(con, pstmt);
		}

		// 결과 데이터를 리턴해 준다.
		return result;
	} // end of delete()

	
	// 실행할 쿼리를 정의해 놓은 변수 선언.
	// 리스트의 페이지 처리 절차 - 원본 -> 순서 번호 -> 해당 페이지 데이터만 가져온다.
	final String LIST = "" + " select no, title, id, name,  writeDate, levNo " + " from ( "
			+ " select rownum rnum, no, title, id, name,  writeDate, levNo " + " from ( "
			+ " select q.no, q.title, q.id, m.name, " + " to_char(q.writeDate, 'yyyy-mm-dd') writeDate, " + " q.levNo "
			+ " from qna q, member m " + " where 1=1 ";
	// 여기에 검색이 있어야 합니다.

	// 검색이 있는 경우 TOTALROW + search문
	final String TOTALROW = "select count(*) from qna q where 1=1 ";

	// LIST에 검색을 처리해서 만들지는 sql문 작성 메서드
	private String getListSQL(PageObject pageObject) {
		String sql = LIST;
		sql += getSearch(pageObject);
		sql += " and (m.id = q.id) ";
		sql += " order by q.refNo desc, ordNo" + " ) " + " ) where rnum between ? and ? ";
		return sql;
	}

	// 리스트의 검색만 처리하는 쿼리 - where
	private String getSearch(PageObject pageObject) {
		String sql = "";
		String key = pageObject.getKey();
		String word = pageObject.getWord();
		if (word != null && !word.equals("")) {
			sql += " and ( 1=0 ";
			// key안에 t가 포함되어 있으면 title로 검색을 한다.
			if (key.indexOf("t") >= 0)
				sql += " or q.title like ? ";
			if (key.indexOf("c") >= 0)
				sql += " or q.content like ? ";
			if (key.indexOf("i") >= 0)
				sql += " or q.id like ? ";

			sql += " ) ";
		}
		return sql;

	}

	// 검색 쿼리의 ? 데이터를 세팅하는 메서드
	private int setSearchData(PageObject pageObject, PreparedStatement pstmt, int idx) throws SQLException {
		String key = pageObject.getKey();
		String word = pageObject.getWord();
		if (word != null && !word.equals("")) {
			// key안에 t가 포함되어 있으면 title로 검색을 한다.
			if (key.indexOf("t") >= 0)
				pstmt.setString(++idx, "%" + word + "%");
			if (key.indexOf("c") >= 0)
				pstmt.setString(++idx, "%" + word + "%");
			if (key.indexOf("i") >= 0)
				pstmt.setString(++idx, "%" + word + "%");
		}
		return idx;
	}

	final String INCREASEORDNO = "update qna set ordNo = ordNo + 1 " 
	+ " where refNo = ? and ordNo >= ? ";

	// 질문답변 보기 + 수정 + 답변하기
	final String VIEW = " select q.no, q.title, q.content, q.id, m.name, "
			+ " to_char(q.writeDate, 'yyyy-mm-dd') writeDate, q.refNo, " 
			+ " q.ordNo, q.levNo "
			+ " from qna q, member m " 
			+ " where (q.no = ?) and (q.id = m.id) ";

	final String WRITE = "insert into qna " 
			+ " (no, title, content, id, refNo, oreNo, levNo, parentNo) "
			+ " values(?, ?, ?, ?, ?, ?, ?, ?)";

	final String QUESTION = "insert into qna " 
			+ " ( no, title, content, id, refNo, ordNo, levNo ) "
			+ " values( ?, ?, ?, ?, ?, ?, ? )";

	final String ANSWER = "insert into qna " 
			+ " (no, title, content, id, refNo, ordNo, levNo, parentNo) "
			+ " values(?, ?, ?, ?, ?, ?, ?, ?)";

	final String NO = " select qna_seq.nextval from dual ";

	final String UPDATE = "update Qna " + " set title = ?, content = ?, writer = ? " 
			+ " where no = ? and pw = ?";

	final String DELETE = "delete from Qna " + " where no = ? and pw = ?";

}
