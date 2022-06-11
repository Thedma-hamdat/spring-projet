<%@page import="com.gsnotes.web.models.PersonModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>


<jsp:include page="../fragments/adminHeader.jsp" />


<div class="container">

	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<div class="container-fluid">

			<jsp:include page="../fragments/menu.jsp" />

		</div>
	</nav>


<div>
		<h3>
		Formulaire d'ajout d'une Filière
		</h3>
	</div>
	<div>


		<tr>
	<div align = "left" >
	<h3><a  href="/admin/addmodule">Add Filiere</a>	   
</div>
	
	</tr>

	<div class="col-sm-8" align = "center">
        <div class="panel-body" align = "center" >
                 
                 
  	<table class="table">
 	<thead class="thead-dark">
       <tr>
      	     <th>Filiere ID</th>
            <th>Filiere Name</th>
            <th>Code</th>
             <th>edit</th>
             <th>delete</th>
   	</tr>
  </thead>
  <tbody>
      
        <c:forEach items="${listfiliere}" var="filiere">
        <tr>
        <td><c:out value="${filiere.titreFiliere}" /></td>
        <td><c:out value="${niveau.codeFiliere}" /></td>
        

		<td>
			<a href="${pageContext.request.contextPath}/admin/editFiliere/${niveau.idFiliere}}">Edit</a>
		</td>							    
		<td>
			<a href="${pageContext.request.contextPath}/admin/deleteFiliere/${niveau.idFiliere}}">Delete</a>
		</td>		    
		</tr> 
		</c:forEach>
  </tbody>
  </table>        
  
  </div>

	<jsp:include page="../fragments/adminfooter.jsp" />