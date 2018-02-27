<%@page contentType="text/html" pageEncoding="UTF-8" session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Please login</title>
	</head>
	<body>
		<div>
			Cet exemple illustre :
			<ul>
				<li>La <a href="https://openclassrooms.com/courses/creez-votre-application-web-avec-java-ee/la-session-connectez-vos-clients" target="_blank">gestion des sessions</a></li>
				<li>Le <a href="https://www.ibm.com/support/knowledgecenter/en/SSZLC2_8.0.0/com.ibm.commerce.admin.doc/tasks/tseurlrewrite.htm" target="_blank">'URL rewriting' pour faire des sessions sans cookies </a></li>
				<li>La <a href="https://stackoverflow.com/questions/14665037/getting-the-init-parameters-in-a-servlet" target="_blank"> configuration des servlets et des sessions dans "web.xml"</a></li>
				<li>Les <a href="http://blog.paumard.org/cours/servlet/chap03-servlet-listener.html" target="_blank">listeners </a>(ici, compter le nombre d'utilisateurs connectés)</li>
				<li>Les <a href="http://blog.paumard.org/cours/servlet/chap04-filtre-mise-en-place.html" target="_blank">filtres </a>(ici, interdire l'accès à un répertoire)</li>
			</ul>
			<hr>
		</div>

		<h1>Bienvenue dans notre application</h1>
		Pour avoir accès aux fichiers dans le répertoire 
		"<a href="protected/protectedPage.html">protected</a>", merci de vous identifier.<br>
		<%--
		La servlet fait : request.setAttribute("errorMessage", "Login/Password incorrect");
		La JSP récupère cette valeur dans ${errorMessage}
		--%>
		<div style="color:red">${errorMessage}</div>

		<form action="<c:url value="/" />" method="POST"> <!-- l'action par défaut est l'URL courant, qui va rappeler la servlet -->
			login (untel) : <input name='loginParam'><br>
			password (ABCD): <input name='passwordParam' type='password'><br>
			<input type='submit' name='action' value='login'>
		</form>
		<!-- On montre le nombre d'utilisateurs connectés -->
		<!-- Cette information est stockée dans le scope "application" par le listener -->
		<h3>Il y a actuellement ${applicationScope.numberConnected} utilisateurs connectés</h3>
	</body>
</html>
