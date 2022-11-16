<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
    <title>Моя еда</title>
</head>
<body>
<h2><a href="index.html">Home</a></h2>
<h1>Meals</h1>
<p><a href="meals?action=add">Add Meal</a></p>
<style>
    table {
        border-collapse: collapse;
    }
    th, td {
        border: 2px solid black;
        padding: 5px;
    }
</style>
<table>
<thead>
    <tr>
        <th>Дата/время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th></th>
        <th></th>
    </tr>
</thead>
    <tbody>
    <%--@elvariable id="meals" type="java.util.List"--%>
    <c:forEach var="mealTo" items="${meals}">
        <tr style="color:${mealTo.excess == true ? 'red' : 'green'}">
            <input type="hidden" name="id" value="${mealTo.id}">
            <td><fmt:parseDate value="${mealTo.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"
                               type="both"/>
                <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${parsedDateTime}"/></td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td><a href="meals?action=edit&id=${mealTo.id}">Update</a></td>
            <td><a href="meals?action=delete&id=${mealTo.id}">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
