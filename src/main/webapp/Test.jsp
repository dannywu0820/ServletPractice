<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="BIG5">
<title>Hello World</title>
</head>
<body>
	<h1> Hello! <%= config.getInitParameter("defaultGuestName") %>!</h1>
	<form action="request" method="post">
        Username: <input type="text" name="user"><br> 
        Password: <input type="password" name="passwd"><br> 
        <input type="submit" name="login">
    </form>
    <form action="upload" method="post" enctype="multipart/form-data">
    	Photo: <input type="file" name="photo"><br>
    	<input type="submit" value="Upload" name="upload">
    </form>
</body>
</html>