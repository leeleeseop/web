 <!-- 240712 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="pageNav" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 리스트</title>
<style type="text/css">
/* 이곳을 주석입니다. Ctrl + Shift + C로 자동 주석 가능. 그러나 푸는 건 안된다.
	선택자 {스타일 항목 : 스타일 값;스타일 항목 : 스타일 값;...}
	기본 선택자 : a - a tag, .a - a라는 클래스(여러개), #a - a라는 아이디(한개)
	다수 선택자 : ","로 선택. a, #a : a tag와 a라는 아이디
	상태 선택자 : ":". ":hover" - 마우스가 올라갔을 때.
				 "a:hover" - a tag 위에 마우스가 올라갔을 때
	선택의 상속 : a .data - a tag 안에 data class의 태그를 찾는다.
 */
.dataRow:hover {
	background: #ddd;
	cursor: pointer;
}
</style>

<script type="text/javascript">
	$(function() {
 		//태그 선택을 아이디로
    $("#${pageObject.period}").prop('checked', true); 
		//태그 선택을 속성으로 하였다
		//$("[value='${pageObject.period}']").prop('chcked', true); 
		// 이벤트 처리
		$(".dataRow").click(function() {
			// alert("click");
			// 글번호 필요 - 수집
			let no = $(this).find(".no").text();
			console.log("no = " + no);
      // location = "view.do?no=" + no + "&inc=1&${pageObject.pageQuery}";
			location="view.do?no=" + no;
		});

		// perPageNum 처리
		$("#perPageNum").change(function() {
			// alert("change perPageNum");
			// page는 1페이지 + 검색 데이터를 전부 보낸다.
			$("#searchForm").submit();
		});

		// 검색 데이터 세팅
		$("#key").val("${(empty pageObject.key)?'t':pageObject.key}");
		$("#perPageNum").val("${(empty pageObject.perPageNum)?'10':pageObject.perPageNum}");
		$(".noticeOption").change(function name() {
			if (this.optionList[0].checked) {
				location = "/notice/list.do?period=pre";
			} else if (this.optionList[1].checked) {
				location = "/notice/list.do?period=old";
			} else if (this.optionList[2].checked) {
				location = "/notice/list.do?period=res";
			} else if (this.optionList[3].checked) {
				location = "/notice/list.do?period=all";
			}
		});
    //end of change
	});
  //end of function
</script>

</head>
<body>
	<div class="container">
		<h1>공지사항 리스트</h1>
		<c:if test="${!empty login && login.gradeNo == 9  }">
				<form class="noticeOption">
					<div class="custom-control custom-radio custom-control-inline">
						<input type="radio" class="custom-control-input" id="pre" name="optionList" value="pre">
						 <label class="custom-control-label" for="pre">현재공지</label>
					</div>

					<div class="custom-control custom-radio custom-control-inline">
						<input type="radio" class="custom-control-input" id="old" name="optionList" value="old"> 
						<label class="custom-control-label" for="old" >이전공지</label>
					</div>

					<div class="custom-control custom-radio custom-control-inline">
						<input type="radio" class="custom-control-input" id="res" name="optionList" value="res"> 
						<label class="custom-control-label" for="res">예정공지</label>
					</div>
					
					<div class="custom-control custom-radio custom-control-inline">
						<input type="radio" class="custom-control-input" id="all" name="optionList" value="all"> 
						<label class="custom-control-label" for="all">모든공지</label>
					</div>
				</form>
		</c:if>

		<!-- 검색란의 시작 -->
		<form action="list.do" id="searchForm">
			<input name="page" value="1" type="hidden">
			<div class="row">
				<div class="col-md-8">
					<div class="input-group mb-3">
						<div class="input-group-prepend">
							<select name="key" id="key" class="form-control">
								<option value="t">제목</option>
								<option value="c">내용</option>
								<option value="c">내용</option>
								<option value="c">내용</option>
								<option value="w">게시일</option>
							</select>
						</div>

						<input type="text" class="form-control" placeholder="검색" id="word" name="word" value="${pageObject.word }">
						<div class="input-group-append">
							<button class="btn btn-outline-primary">
								<i class="fa fa-search"></i>
							</button>
						</div>
            <!--end of class="input-group-append"-->
					</div>
          <!--end of class="input-group mb-3"-->
				</div>
				<!-- end of class="col-md-8" : 검색 -->

				<div class="col-md-4">
					<!-- 너비를 조정하기 위한 div 추가. float-right : 오른쪽 정렬 -->
					<div style="width: 200px;" class="float-right">
						<div class="input-group mb-3">
							<div class="input-group-prepend">
								<span class="input-group-text">Rows/Page</span>
							</div>
							<select id="perPageNum" name="perPageNum" class="form-control">
								<option>10</option>
								<option>15</option>
								<option>20</option>
								<option>25</option>
							</select>
						</div>
            <!-- end of class="input-group mb-3" -->
					</div>
          <!-- end of class="float-right" -->
				</div>
				<!-- end of class="col-md-4"  : 한페이지당 표시 데이터 개수 -->
			</div>
      <!-- end of class="row" -->
		</form>
    <!-- end of id="searchForm" -->

		<table class="table">
			<!-- tr : table row - 테이블 한줄 -->
			<!-- 게시판 데이터의 제목 -->
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>게시일</th>
				<th>종료일</th>
				<th>수정일</th>
			</tr>

			<!-- 실제적인 데이터 표시 : 데이터가 있는 만큼 줄(tr)이 생긴다. -->
			<!-- JS로 글보기로 페이지 이동
			onclick : click 이벤트 핸들러 속성 -->
			<c:forEach items="${list }" var="vo">
				<tr class="dataRow">
					<!-- td : table data - 테이블 데이터 텍스트 -->
					<td class="no">${vo.no}</td>
					<td>${vo.title}</td>
					<td>${vo.startDate}</td>
					<td>${vo.endDate}</td>
					<td>${vo.updateDate}</td>
					<!-- a tag : 데이터를 클릭하면 href의 정보를 가져와서 페이지 이동시킨다. -->
				</tr>
			</c:forEach>
				<tr>
					<a href="writeForm.do?perPageNum=${pageObject.perPageNum }" class="btn btn-primary">등록</a>
				</tr>
		</table>
		<pageNav:pageNav listURI="list.do" pageObject="${pageObject }"></pageNav:pageNav>
	</div>
</body>
</html>
