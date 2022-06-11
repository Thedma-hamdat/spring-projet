<%@page import="com.gsnotes.web.models.PersonModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>


<jsp:include page="../fragments/adminHeader.jsp" />

      
 	<div align="center">
    <h1>Create New Filiere</h1>
    <br />
     <div class="col-sm-4">
    <form action="saveModule" method="post" modelAttribute="module">

      
 	<div alight="left">
            <tr>
                <label class="form-label" >Filiere Name</label>
                <td>
	                <input type="hidden" th:field="*{idFiliere}" />
	                <input type="text" th:field="*{titreFiliere}" class="form-control" placeholder="Filiere Name" />
                </td>
            </tr>
         </div>   
         
         
          <div alight="left">
            <tr>
                <label class="form-label" >Code</label>
                
                <td>
                 <input type="hidden" th:field="*{idFiliere}" />
                 <input type="text" th:field="*{codeFiliere}" class="form-control" placeholder="Code" />
                </td>
            </tr>
         </div>  
         
	<br>
            <tr>
            <td colspan="2"><button type="submit" class="btn btn-info">Save</button> </td>
            </tr>

    </form>
</div>

	<jsp:include page="../fragments/adminfooter.jsp" />