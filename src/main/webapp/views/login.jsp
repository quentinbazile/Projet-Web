<%@page contentType="text/html" pageEncoding="UTF-8" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Please login</title>
        <link rel="stylesheet" href="<c:url value="views/css/login.css"/>">
    </head>
    <body background="<c:url value='views/images/bgd.jpg'/>">
        <div class="loginBox">
            <img src="<c:url value="views/images/user.png"/>" class="user">
            <h2>Log In Here</h2>
            <form action="<c:url value="/" />" method="POST">
                <p>Email</p>
                <input type='email' name='loginParam' placeholder='Enter Email' required>
                <p>Password</p>
                <input type='password' name='passwordParam' placeholder='••••••' required>
                <input type='submit' name='action' value='Sign In'>
            </form>
        </div>
    </body>
</html>
