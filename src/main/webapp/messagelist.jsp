<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Application Integration</title>
<style type="text/css" media="screen">
@IMPORT url("styles/main.css");

@IMPORT url("styles/jquery-ui.css");
</style>
<script src="script/jquery.js"></script>
<script src="script/jquery-ui.min.js"></script>
<script>
	$(function() {
		$("#date").datepicker();
		$("#startDate").datepicker();
		$("#endDate").datepicker();
		$("#bydaterange").hide();
		$("#searchBy").change(function() {
			if (this.value == 'date') {
				$("#bydate").show();
				$("#bydaterange").hide();
			} else {
				$("#bydaterange").show();
				$("#bydate").hide();
			}
		});
		
		$("#search").click(function() {
			$("#searchForm").submit();
		});
		

	});
</script>
</head>
<body>
	<header> Application Integration </header>
	<br />
	<nav>
		<a href="/home/">Home</a> <a href="/message">Audit Log</a> <a
			href="/routeconfig/">Route Configuration</a> <a href="/statistics">Statistics</a>
	</nav>
	<br />
	<div class="container">
		<form action="/messagelist" id="searchForm" method="post">
			<label for="serviceName">Service Name</label> 
			<input type="text" id="serviceName" name="serviceName"> 
			
			<label for="searchBy">Search by</label> 
			<select id="searchBy" name="searchBy" id="searchBy">
				<option value="date" selected>Date</option>
				<option value="dateRange">Date range</option>
			</select> 
			<span id="bydate">
				<label for="date">Date</label> 
				<input class="date" type="text"	name="date" id="date">
			</span>
			<span id="bydaterange">
				<label for="startDate">Start Date</label> 
				<input class="date"	type="text" name="startDate" id="startDate"> 
				
				<label	for="endDate">End Date</label> 
				<input class="date" type="text" name="endDate" id="endDate">
			</span>
			<button id="search">Search</button>
		</form>
	</div>
	
		<table  align="left">	 
		<thead>
			<tr>
				<th>Id</th>
				<th>Time stamp</th>
				<th>Mime Headers</th>
				<th>Status</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${messageList != null}" >
			<c:forEach var="message" items="${messageList}">
			<tr class="odd">
				<td>${message.id}</td>
				<td>${message.timeStamp}</td>
				<td>${message.mimeHeaders}</td>
				<td>New</td>
				</tr>
			</c:forEach>
			
			
			</c:if>			
		</tbody>			
		</table>
	

</body>
</html>