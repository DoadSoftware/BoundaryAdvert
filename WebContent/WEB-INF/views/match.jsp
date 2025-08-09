<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
  <sec:csrfMetaTags/>
  <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
  <title>BoundaryAdvert</title>
<script type="text/javascript" src="<c:url value="/webjars/jquery/1.9.1/jquery.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/webjars/bootstrap/3.3.6/js/bootstrap.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/javascript/index.js"/>"></script>
<link rel="stylesheet" href="<c:url value="/webjars/bootstrap/3.3.6/css/bootstrap.min.css"/>"/>
<!-- Add this in <head> -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body onload="afterPageLoad('MATCH');">
<form:form name="BoundaryAdvert_form" autocomplete="off" action="match" method="POST" 
	modelAttribute="session_match" enctype="multipart/form-data">
<div class="content py-5">
  <div class="container my-4">
	<div class="row">
	 <div class="col-12">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
	          </div>
           </div>
          <div class="card-body">
          <div id="select_graphic_options_div" style="display:none;"></div>
			  <div class="panel-group" id="match_configuration">
			    <div class="panel panel-default">
			      <div class="panel-heading">
			        <h1 class="panel-title">
			          <a data-toggle="collapse" data-parent="#match_configuration" 
			          		href="#load_setup_match">Output</a>
			        </h1>
			      </div>
			      <div id="load_setup_match" class="panel-collapse collapse">
					<div class="panel-body">
					    <div class="col-sm-6 col-md-6">
						    <label for="select_matrix" class="col-sm-5 col-form-label text-left">Select Matrix</label>
						      <select id="select_matrix" name="select_matrix" class="browser-default custom-select custom-select-sm">
									<c:forEach items = "${matrices}" var = "matrix">
							          <option value="${matrix.description}">${matrix.description}</option>
									</c:forEach>
						      </select>
						    <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
						  		name="process_matrix_btn" id="process_matrix_btn" onclick="processBoundaryAdvertProcedures('PROCESS_MATRIX')">
						  		<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true" style="display:none"></span>
						  		<i class="fas fa-download"></i> Load Match</button>
					    </div>
				    </div>
			      </div>
			    </div>
			  </div> 
			  <div class="form-group row ml-2" style="margin-bottom:0px; padding-bottom:0px;">
			  <div id="boundaryAlert_div" style="display:none;"></div>
           </div>
          </div>
         </div>
       </div>
    </div>
  </div>
 </div>
 <input type="hidden" id="selected_player_id" name="selected_player_id"></input>
 <input type="hidden" name="selectedBroadcaster" id="selectedBroadcaster" value="${session_selected_broadcaster}"/>
</form:form>
</body>
</html>