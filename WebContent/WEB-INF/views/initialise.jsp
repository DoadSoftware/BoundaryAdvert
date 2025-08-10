<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Initialise</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  <script src="<c:url value="/webjars/jquery/1.9.1/jquery.min.js"/>"></script>
  <script src="<c:url value="/resources/javascript/index.js"/>"></script>
</head>
<body>
	<div class="container">
		<div class="form-container">
			<h3 class="word-art">Initialise</h3>
			<form name="initialise_form" autocomplete="off" action="match" method="POST">
				<div class="mb-3">
					<label class="form-label" for="select_configuration_file">
						<span class="word-artist">Select Configuration File</span>
					</label>
					<select id="select_configuration_file" name="select_configuration_file" class="form-select" onchange="processUserSelection(this)">
						<option value="">Select Config</option>
						<c:forEach items="${configuration_files}" var="config">
							<option value="${config.name}">${config.name}</option>
						</c:forEach>
					</select>
				</div>
				<div class="mb-3">
					<label class="form-label" for="configuration_file_name">
						<span class="word-artist">Configuration File Name</span>
					</label>
					<input type="text" id="configurationFileName" name="configurationFileName" class="form-control" placeholder="Selected Config File">
				</div>
				<div class="mb-3">
					<label class="form-label" for="selectedBroadcaster">
						<span class="word-artist">Select Broadcaster</span>
					</label>
					<select id="selectedBroadcaster" name="selectedBroadcaster" class="form-select">
						<option value="doad_boundary_advert">Doad Boundary Advert</option>
					</select>
				</div>
				<div id="vizPortNumber_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
					<label for="vizPortNumber" class="col-sm-4 col-form-label text-left">Viz Port Number 
					</label>
					<div class="col-sm-6 col-md-6">
						<input type="text" id="vizPortNumber" name="vizPortNumber" value="${session_Configurations.portNumber}" class="form-control form-control-sm floatlabel"/>
					</div>
				</div>
				<div id="vizIPAddress_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
					<label for="vizIPAddress" class="col-sm-4 col-form-label text-left">Viz IP Address 
					</label>
					<div class="col-sm-6 col-md-6">
						<input type="text" id="vizIPAddress" name="vizIPAddress" value="${session_Configurations.ipAddress}" class="form-control form-control-sm floatlabel" value="localhost"/>
					</div>
				</div>
				<div id="vizScene_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
					<label for="vizScene" class="col-sm-4 col-form-label text-left">Viz Scene 
					</label>
					<select id="vizScene" name="vizScene" class="form-select">
						<c:forEach items="${session_viz_scenes}" var="scene">
							<option value="${scene.name}">${scene.name}</option>
						</c:forEach>
					</select>
				</div>
			</form>
		</div>
		<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button" 
			name="load_scene_btn" id="load_scene_btn" onclick="processUserSelection(this)">Load Scene</button>
	</div>
</body>
</html>
