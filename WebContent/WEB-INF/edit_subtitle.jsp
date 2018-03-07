<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
    	<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>Editer les sous-titres</title>
	    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
	  	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    	<!--[if lt IE 9]>
			<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
			<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    	<![endif]-->
    	<style type="text/css">
    		body{
    			background-color:#EEEEEE
    		}
    		
    		h1{
    			margin-bottom:40px;
    		}
    		
    		.upload{
    			margin-top : 10px;
    		}
    		
    		.message{
    			color:green;
    			font-size:20px;
    			margin-top:30px;
    			margin-bottom:30px;
    		}
    		
    		.tableauSousTitre{
    			width:80%;
    			margin:auto;
    			margin-bottom:50px;
    		}
    	</style>
	</head>
	<body>
	
		<h1 class="text-center">Editeur de sous-titres</h1>
		<form method="post" action="edit" enctype="multipart/form-data">
		
			<div class="row">
				<div class="col-lg-offset-3 col-lg-2">
					<div class="panel panel-primary">
						<div class="panel-heading">
							<h2 class="text-center panel-title">Gestion des imports</h2>
						</div>
						<div class="panel-body">
							<p>
								<label for="description">Description du fichier : </label>
								<input type="text" name="description" id="description" />
							</p>
							<p>
								<label for="fileToUpload">Fichier à envoyer : </label>
								<input type="file" name="fileToUpload" id="fileToUpload" />
								<input type="submit" value="Upload" name="upload" class="upload btn btn-primary"/>
							</p>
						</div>
					</div>
				</div>
		       <div class="col-lg-2">
		       		<div class="panel panel-primary">
			       		<div class="panel-heading">
			       			<h2 class="text-center panel-title">Gestion de la sauvegarde</h2>
		       			</div>
		       			<div class="panel-body">
							<p class="text-center">
								<c:choose>
										<c:when test="${boutonInactif }"><input type="submit" value="Sauvegarder en base de données" name="saveDB" disabled class="btn btn-primary" /></c:when>
										<c:otherwise><input type="submit" value="Sauvegarder en base de données" name="saveDB" class="btn btn-primary"/></c:otherwise>
								</c:choose>
							</p>
						</div>
					</div>
				</div>
				<div class="col-lg-2">
					<div class="panel panel-primary">
						<div class="panel-heading">
							<h2 class="text-center panel-title">Gestion des exports</h2>
						</div>
						<div class="panel-body">
							<p class="text-center">
								<c:choose>
									<c:when test="${boutonInactif }"><input type="submit" value="Export de la traduction" name="exportDB" disabled class="btn btn-primary"/></c:when>
									<c:otherwise><input type="submit" value="Export de la traduction" name="exportDB" class="btn btn-primary"/></c:otherwise>
								</c:choose>
							</p>
						</div>
					</div>
				</div>
			</div>
			
			<c:if test="${ !empty fileToUpload }"><p class="message text-center"><c:out value="Le fichier ${ fileToUpload } (${ description }) a été uploadé dans le tableau ci-dessous !" /></p></c:if>
			<c:if test="${ !empty saveDB }"><p class="message text-center"><c:out value="Le fichier en cours de traitement a été sauvegardé en base de données!" /></p></c:if>
			<c:if test="${ !empty exportDB }"><p class="message text-center"><c:out value="Le fichier en cours de traitement a été sauvegardé en base de données et exporté en fichier .srt !" /></p></c:if>
			
			<c:if test="${ !empty fileToUpload || !empty saveDB || !empty exportDB }">
				<div class="panel panel-primary tableauSousTitre">
					<table class="table table-striped table-condensed">
						<div class="panel-heading"> 
	     					<h2 class="panel-title text-center">Tableau de sous-titres</h2>
	    				</div>
	    				
						<c:forEach items="${subtitles}" var="line" varStatus="status">
							<tr>
								<td style="text-align:center;"><c:out value="${ line.numeroSousTitre }" /></td>
								<td style="text-align:center;"><c:out value="${ line.debutSequence }" /></td>
								<td style="text-align:center;"><c:out value="${ line.finSequence }" /></td>
								<td style="text-align:center;"><c:out value="${ line.sousTitreOriginal }" /></td>
								<td><input type="text" name="line${ status.index }" id="line${ status.index }" size="35" value="${ line.sousTitreTraduit }" /></td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</c:if>
			
		</form>
	   
	</body>
</html>