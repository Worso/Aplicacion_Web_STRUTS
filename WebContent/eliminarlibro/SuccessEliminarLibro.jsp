<%@ page language="java" 
		 contentType="text/html; charset=utf-8" 
		 pageEncoding="utf-8"
		 import="com.grupoatrium.modelo.*"%>

<!DOCTYPE html>

<html lang = "es">

<head>
	<meta charset = "utf-8">
	<title>Éxito al Eliminar un Libro</title>
	<!-- Hoja de estilos -->
	<link href = "./css/eliminarLibro.css" rel = "stylesheet" type = "text/css">
	<!-- Archivo JavaScript que se carga al abrir la página: buscarAutor.js -->
	<script src = "" type = "text/javascript" ></script>
</head>

<body>
	<header>
		<h1>SE HA ELIMINADO UN LIBRO EN LA BASE DE DATOS</h1>
	</header>
	<div id="main">
		<h2>El libro con ID: ${requestScope.libro}, se ha eliminado de la base de datos.</h2>
	</div>
	<article>

	</article>
	<div id = "separacion"></div>
	<a id="index" href = "./index.jsp"><<<  Inicio</a>
	
	<footer> El tiempo de procesamiento de su petición ha sido de ${requestScope.tiempoPeticion} milisegundos.</footer>
</body>
</html>