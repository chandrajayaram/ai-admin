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
			<textarea style="border:none; height: 476px; width: 1101px;" >${message.soapMessageAsString}</textarea>
	</div>
	

</body>
</html>