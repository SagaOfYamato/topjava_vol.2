<%--@elvariable id="mealTo" type="ru.javawebinar.topjava.model.MealTo"--%>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>Добавить/редактировать</title>
</head>
<body>
<h2><a href="index.html">Home</a></h2>
<h1>Edit meal</h1>
<form method="post" action="meals">
    <label>
        <input type="hidden" name="id" value="${mealTo.id}">
    </label>
    <pre><label>Дата/время: <input type="datetime-local" name="date_time" value="${mealTo.dateTime}" placeholder="${mealTo.dateTime}"></label><br></pre>
    <pre><label>Описание: <input type="text" name="description" value="${mealTo.description}" placeholder="${mealTo.description}"></label><br></pre>
    <pre><label>Калории: <input type="number" name="calories" value="${mealTo.calories}" placeholder="${mealTo.calories}"></label><br></pre>
    <input type="submit" value="Сохранить" name="save">
    <input type="button" value="Отмена" onclick="window.history.back()">
</form>
</body>
</html>
