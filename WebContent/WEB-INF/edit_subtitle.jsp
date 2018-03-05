<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Editer les sous-titres</title>
</head>
<body>
	 
	 <form method="post" action="edit" enctype="multipart/form-data">
        <p>
            <label for="description">Description du fichier : </label>
            <input type="text" name="description" id="description" />
        </p>
        <p>
            <label for="fileToUpload">Fichier à envoyer : </label>
            <input type="file" name="fileToUpload" id="fileToUpload" />
            <input type="submit" value="Upload" name="upload"/>
        </p>
          
	<c:if test="${ !empty fileToUpload }"><p><c:out value="Le fichier ${ fileToUpload } (${ description }) a été uploadé !" /></p></c:if>
    
        <input type="submit" style="position:fixed; top: 10px; right: 10px;" value="Sauvegarder" name="saveDB" disabled="${boutonInactif }" />
	    <table>
	        <c:forEach items="${subtitles}" var="line" varStatus="status">
	        	<tr>
	        		<td style="text-align:right;"><c:out value="${ line.numeroSousTitre }" /></td>
	        		<td style="text-align:right;"><c:out value="${ line.debutSequence }" /></td>
	        		<td style="text-align:right;"><c:out value="${ line.finSequence }" /></td>
	        		<td style="text-align:right;"><c:out value="${ line.sousTitreOriginal }" /></td>
	        		<td><input type="text" name="line${ status.index }" id="line${ status.index }" size="35" value="${ line.sousTitreTraduit }" /></td>
	        	</tr>
	    	</c:forEach>
	    </table>
    </form>
</body>
</html>