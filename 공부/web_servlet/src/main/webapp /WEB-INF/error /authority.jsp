<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>404 Error</title>
</head>
<body>
<!-- container -->
<div class="container">
  <!-- card -->
  <div class="card">
    <!--card-header-->
    <div class="card-header">
    	<h3>
    		<i class="fa fa-times-circle"></i>
    		권한 오류
    	</h3>
    </div>
    <!--//card-header-->

    <!--card-body-->
    <div class="card-body" id="errorDiv">
    	<div class="text-center">
    		<i class="fa fa-times-circle" style="font-size:100px;color:red"></i>
    	</div>

      <div class="row">
    	  <div class="col-md-3"><i class="fa fa-check"></i> 요청 URI</div>
    	  <div class="col-md-9">${uri }</div>
    	</div>
  
      <div class="row">
    	  <div class="col-md-3"><i class="fa fa-check"></i> 오류 메시지</div>
    	  <div class="col-md-9">요청하신 페이지에 접근할 권한이 없습니다.</div>
    	</div>
  
      <div class="row">
    	  <div class="col-md-3"><i class="fa fa-check"></i> 조치 사항</div>
    	  <div class="col-md-9">
    	  	로그인 정보의 등급을 확인해 주세요. 오류가 있는 경우 관리자에게 연락해 주세요.
    	  </div>
    	</div>
    </div>
    <!--//card-body-->

    <div class="card-footer">
    	<a href="/board/list.do" class="btn btn-primary">일반 게시판으로 가기</a>
    </div>
    <!--card-footer-->
  </div>
   <!--//card-header-->
</div>
<!-- //container -->
</body>
</html>
