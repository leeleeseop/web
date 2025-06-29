package com.webjjang.image.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.webjjang.image.vo.ImageVO;
import com.webjjang.main.dao.DAO;
import com.webjjang.util.db.DB;
import com.webjjang.util.page.PageObject;

public class ImageDAO extends DAO{

	// 필요한 객체 선언 - 상속
	// 접속 정보 - DB 사용 - connection을 가져오게 하는 메서드만 이용
	
	// 1-1. 리스트 처리
	// ImageController - (Execute) - ImageListService - [ImageDAO.list()]
	public List<ImageVO> list(PageObject pageObject) throws Exception{
		// 결과를 저장할 수 있는 변수 선언.
		List<ImageVO> list = null;
		
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
			if(rs != null) {
				while(rs.next()) {
					// rs - > vo -> list
					// list가 null이면 생성해서 저장할 수 있게 해줘야 한다.
					if(list == null) list = new ArrayList<ImageVO>();
					// rs -> vo
					ImageVO vo = new ImageVO();
					vo.setNo(rs.getLong("no"));
					vo.setTitle(rs.getString("title"));
					vo.setId(rs.getString("id"));
					vo.setName(rs.getString("name"));
					vo.setWriteDate(rs.getString("writeDate"));
					vo.setFileName(rs.getString("fileName"));
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
	// ImageController - (Execute) - ImageListService - [ImageDAO.getTotalRow()]
	public Long getTotalRow(PageObject pageObject) throws Exception{
		// 결과를 저장할 수 있는 변수 선언.
		Long totalRow = null;
		try {
			// 1. 드라이버 확인 - DB
			// 2. 연결
			con = DB.getConnection();
			// 3. sql - 아래 TOTALROW
			System.out.println("ImageDAO.getTotalRow().sql="
			+ TOTALROW + getSearch(pageObject, true));
			// 4. 실행 객체 & 데이터 세팅
			pstmt = con.prepareStatement(TOTALROW 
					// 전체 데이터 개수 쿼리인 경우 검색 조건이 있으면 where를 붙여라:true
					+ getSearch(pageObject, true));
			int idx = 0;
			idx = setSearchData(pageObject, pstmt, idx);
			// 5. 실행
			rs = pstmt.executeQuery();
			// 6. 표시 또는 담기
			if(rs != null && rs.next()) {
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

  
	// 2. 이미지 보기 처리
	// ImageController - (Execute) - ImageViewService - [ImageDAO.view()]
	public ImageVO view(Long no) throws Exception{
		// 결과를 저장할 수 있는 변수 선언.
		ImageVO vo = null;
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
			if(rs != null && rs.next()) {
				// rs -> vo
				vo = new ImageVO();
				vo.setNo(rs.getLong("no"));
				vo.setTitle(rs.getString("title"));
				vo.setContent(rs.getString("content"));
				vo.setId(rs.getString("id"));
				vo.setName(rs.getString("name"));
				vo.setWriteDate(rs.getString("writeDate"));
				vo.setFileName(rs.getString("fileName"));
			} // end of if
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			// 7. 닫기
			DB.close(con, pstmt, rs);
		} // end of try ~ catch ~ finally
		// 결과 데이터를 리턴해 준다.
		return vo;
	} // end of view()

  
	// 3. 등록 처리
	// ImageController - (Execute) - ImageWriteService - [ImageDAO.write()]
	public int write(ImageVO vo) throws Exception{
		// 결과를 저장할 수 있는 변수 선언.
		int result = 0;
		
		try {
			// 1. 드라이버 확인 - DB
			// 2. 연결
			con = DB.getConnection();
			// 3. sql
			// 4. 실행 객체 & 데이터 세팅
			pstmt = con.prepareStatement(WRITE);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setString(3, vo.getId());
			pstmt.setString(4, vo.getFileName());
			// 5. 실행 - update : executeUpdate() -> int 결과가 나옴.
			result = pstmt.executeUpdate();
			// 6. 표시 또는 담기
			System.out.println();
			System.out.println("*** 등록이 완료 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			// 그외 처리 중 나타나는 오류에 대해서 사용자가 볼수 있는 예외로 만들어 전달한다.
			throw new Exception("예외 발생 : 이미지 게시판 등록 DB 처리 중 예외가 발생했습니다.");
		} finally {
			// 7. 닫기
			DB.close(con, pstmt);
		}
		
		// 결과 데이터를 리턴해 준다.
		return result;
	} // end of write()
	
	
	// 4. 글수정 처리
	// ImageController - (Execute) - ImageUpdateService - [ImageDAO.update()]
	public int update(ImageVO vo) throws Exception{
		// 결과를 저장할 수 있는 변수 선언.
		int result = 0;
		
		try {
			// 1. 드라이버 확인 - DB
			// 2. 연결
			con = DB.getConnection();
			// 3. sql
			// 4. 실행 객체 & 데이터 세팅
			pstmt = con.prepareStatement(UPDATE);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setLong(3, vo.getNo());
			pstmt.setString(4, vo.getId());
			// 5. 실행 - update : executeUpdate() -> int 결과가 나옴.
			result = pstmt.executeUpdate();
			// 6. 표시 또는 담기
			if(result == 0) { // 글번호가 존재하지 않는다. -> 예외로 처리한다.
				throw new Exception("예외 발생 : 번호가 맞지 않거나 본인 글이 아닙니다. 정보를 확인해 주세요.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 특별한 예외는 그냥 전달한다.
			if(e.getMessage().indexOf("예외 발생") >= 0) throw e;
			// 그외 처리 중 나타나는 오류에 대해서 사용자가 볼수 있는 예외로 만들어 전달한다.
			else throw new Exception("예외 발생 : 이미지 게시판 수정 DB 처리 중 예외가 발생했습니다.");
		} finally {
			// 7. 닫기
			DB.close(con, pstmt);
		}
		
		// 결과 데이터를 리턴해 준다.
		return result;
	} // end of update()
	
	
	// 5. 글삭제 처리
	// ImageController - (Execute) - ImageDeleteService - [ImageDAO.delete()]
	public int delete(ImageVO vo) throws Exception{
		// 결과를 저장할 수 있는 변수 선언.
		int result = 0;
		
		try {
			// 1. 드라이버 확인 - DB
			// 2. 연결
			con = DB.getConnection();
			// 3. sql 
			// 4. 실행 객체 & 데이터 세팅
			pstmt = con.prepareStatement(DELETE);
			pstmt.setLong(1, vo.getNo());
			pstmt.setString(2, vo.getId());
			// 5. 실행 - update : executeUpdate() -> int 결과가 나옴.
			result = pstmt.executeUpdate();
			// 6. 표시 또는 담기
			if(result == 0) { // 글번호가 존재하지 않거나 비번 틀림. -> 예외로 처리한다.
				throw new Exception("예외 발생 : 번호 맞지 않거나 본인 글이 아닙니다. 정보를 확인해 주세요.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 특별한 예외는 그냥 전달한다.
			if(e.getMessage().indexOf("예외 발생") >= 0) throw e;
			// 그외 처리 중 나타나는 오류에 대해서 사용자가 볼수 있는 예외로 만들어 전달한다.
			else throw new Exception("예외 발생 : 이미지 삭제 DB 처리 중 예외가 발생했습니다.");
		} finally {
			// 7. 닫기
			DB.close(con, pstmt);
		}
		
		// 결과 데이터를 리턴해 준다.
		return result;
	} // end of delete()
	
	
	// 6. 이미지 변경 처리
	// ImageController - (Execute) - ImageChangeService - [ImageDAO.changeImage()]
	public int changeImage(ImageVO vo) throws Exception{
		// 결과를 저장할 수 있는 변수 선언.
		int result = 0;
		
		try {
			// 1. 드라이버 확인 - DB
			// 2. 연결
			con = DB.getConnection();
			// 3. sql - 아래 UPDATE
			// 4. 실행 객체 & 데이터 세팅
			pstmt = con.prepareStatement(CHANGEIMAGE);
			pstmt.setString(1, vo.getFileName());
			pstmt.setLong(2, vo.getNo());
			// 5. 실행 - update : executeUpdate() -> int 결과가 나옴.
			result = pstmt.executeUpdate();
			// 6. 표시 또는 담기
			if(result == 0) { // 글번호가 존재하지 않는다. -> 예외로 처리한다.
				throw new Exception("예외 발생 : 번호가 맞지 않습니다. 정보를 확인해 주세요.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 특별한 예외는 그냥 전달한다.
			if(e.getMessage().indexOf("예외 발생") >= 0) throw e;
			// 그외 처리 중 나타나는 오류에 대해서 사용자가 볼수 있는 예외로 만들어 전달한다.
			else throw new Exception("예외 발생 : 이미지 바꾸기 DB 처리 중 예외가 발생했습니다.");
		} finally {
			// 7. 닫기
			DB.close(con, pstmt);
		}
		
		// 결과 데이터를 리턴해 준다.
		return result;
	} // end of changeImage()
	

	
	// 실행할 쿼리를 정의해 놓은 변수 선언.
	
	// 리스트의 페이지 처리 절차 - 원본 -> 순서 번호 -> 해당 페이지 데이터만 가져온다.
	final String LIST = "" 
		+ " select no, title, id, name, writeDate, fileName "
		+ " from ( "
			+ " select rownum rnum, no, title, id, name, "
			+ " 	writeDate, fileName "
			+ " from ( "
				+ " select i.no, i.title, i.id, m.name, "
				+ " to_char(i.writeDate, 'yyyy-mm-dd') writeDate, "
				+ " i.fileName "
				+ " from image i , member m "
				// where  1=1 and (일반조건) and (조인조건)
				+ " where 1=1 ";

	// 검색이 있는 경우 TOTALROW + search문
	final String TOTALROW = "select count(*) from image ";

	// LIST에 검색을 처리해서 만들지는 sql문 작성 메서드
	private String getListSQL(PageObject pageObject) {
		String sql = LIST; 
		String word = pageObject.getWord();
		// 검색 쿼리 추가 - where를 추가 안한다. : false
		sql += getSearch(pageObject, false);
		sql += " and (m.id = i.id) ";
		sql += " order by no desc "
				+ " ) "
				+ " ) where rnum between ? and ? ";
		return sql;
	}
	
	// 리스트의 검색만 처리하는 쿼리 - where
	// list(), getTotalRow()에서 사용한다. list는 where 반드시 넣는다. 이미 있다.
	// getTotalRow() where가 없다. 그래서 검색 있는 경우 where 추가해야
	private String getSearch(PageObject pageObject, boolean isWhere) {
		String sql = "";
		String key = pageObject.getKey();
		String word = pageObject.getWord();
		if(word != null && !word.equals("")) {
			// where 붙이기 처리
			if(isWhere) sql += " where 1=1 ";
			sql += " and ( 1=0 ";
		// key안에 t가 포함되어 있으면 title로 검색을 한다.
			if(key.indexOf("t") >= 0) sql += " or title like ? ";
			if(key.indexOf("c") >= 0) sql += " or content like ? ";
			if(key.indexOf("f") >= 0) sql += " or fileName like ? ";
			sql += " ) ";
		}
		return sql;
	}
	
	// 검색 쿼리의 ? 데이터를 세팅하는 메서드
	private int setSearchData(PageObject pageObject, 
			PreparedStatement pstmt, int idx) throws SQLException {
		String key = pageObject.getKey();
		String word = pageObject.getWord();
		if(word != null && !word.equals("")) {
			// key안에 t가 포함되어 있으면 title로 검색을 한다.
			if(key.indexOf("t") >= 0) pstmt.setString(++idx, "%" + word + "%");
			if(key.indexOf("c") >= 0) pstmt.setString(++idx, "%" + word + "%");
			if(key.indexOf("f") >= 0) pstmt.setString(++idx, "%" + word + "%");
		}
		return idx;
	}
	
	final String INCREASE = "update board set hit = hit + 1 "
			+ " where no = ?"; 
  
	final String VIEW= "select i.no, i.title, i.content, i.id, m.name, "
			+ " to_char(i.writeDate, 'yyyy-mm-dd') writeDate, i.fileName "
			+ " from image i, member m "
			+ " where (i.no = ?) and (m.id = i.id)";
  
	final String WRITE = "insert into image "
			+ " (no, title, content, id, fileName) "
			+ " values(image_seq.nextval, ?, ?, ?, ?)"; 
  
	final String UPDATE= "update image "
			+ " set title = ?, content = ? "
			+ " where no = ? and id = ?"; 
  
	final String DELETE= "delete from image "
			+ " where no = ? and id = ?"; 
	
	final String CHANGEIMAGE= "update image "
			+ " set fileName = ? "
			+ " where no = ? "; 
}
