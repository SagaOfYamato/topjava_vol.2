<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="/WEB-INF/jsp/fragments/headTag.jsp"/>

<head>
    <title>Meal</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/fragments/bodyHeader.jsp"/>

<section>
    <hr>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post" action="${meal.id == null ? 'create' : 'meals-update'}">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="mealForm.dateTime"/></dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="mealForm.description"/></dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="mealForm.calories"/></dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><spring:message code="mealForm.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="mealForm.cancel"/></button>
    </form>
</section>
<jsp:include page="/WEB-INF/jsp/fragments/footer.jsp"/>
</body>
</html>
