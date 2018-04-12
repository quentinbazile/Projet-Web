<%@page contentType="text/html" pageEncoding="UTF-8" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bienvenue</title>
        <!-- un CSS pour formatter la page web -->
        <link rel="stylesheet" href="<c:url value="views/css/login.css"/>">
        <!-- On charge les polices Google -->
        <link rel="stylesheet" href="<c:url value="https://fonts.googleapis.com/css?family=Oswald:400,500|Roboto:300,400,500,700"/>">
    </head>
    <body>
        <!-- La box du login -->
        <div class="loginBox">
            <!-- L'image cadenas -->
            <img src="<c:url value="views/images/lock.png"/>" class="lock">
            <h2>Connectez-vous</h2>
            <!-- Un formulaire pour renvoyer les paramètres saisis par l'utilisateur -->
            <form action="<c:url value="/" />" method="POST">
                <p>Nom d'utilisateur</p>
                <input type='text' name='loginParam' required>
                <p>Mot de passe</p>
                <input type='password' name='passwordParam' required>
                <!-- Le bouton Connexion -->
                <input type='submit' name='action' value='Connexion'>
            </form>
        </div>
    </body>
</html>