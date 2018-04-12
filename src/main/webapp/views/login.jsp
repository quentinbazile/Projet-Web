<%@page contentType="text/html" pageEncoding="UTF-8" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bienvenue</title>
        <link rel="stylesheet" href="<c:url value="views/css/login.css"/>">
        <link rel="stylesheet" href="<c:url value="https://fonts.googleapis.com/css?family=Oswald:400,500|Roboto:300,400,500,700"/>">
    </head>
    <body>
        <div class="loginBox">
            <img src="<c:url value="views/images/lock.png"/>" class="lock">
            <h2>Connectez-vous</h2>
            <form action="<c:url value="/" />" method="POST">
                <p>Nom d'utilisateur</p>
                <input type='text' name='loginParam' required>
                <p>Mot de passe</p>
                <input type='password' name='passwordParam' required>
                <input type='submit' name='action' value='Connexion'>
            </form>
        </div>
    </body>
</html>
