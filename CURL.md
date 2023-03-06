get All Users
curl -X GET --location "http://localhost:8080/topjava/rest/admin/users" \
-H "Accept: application/json"

get Users 100001
curl -X GET --location "http://localhost:8080/topjava/rest/admin/users/100001" \
-H "Accept: application/json"

get Users by email user@yandex.ru
curl -X GET --location "http://localhost:8080/topjava/rest/admin/users/by-email?email=user@yandex.ru" \
-H "Accept: application/json"

register User
curl -X POST --location "http://localhost:8080/topjava/rest/admin/users" \
-H "Content-Type: application/json" \
-d "{\"name\": \"New3\",
\"email\": \"new3@yandex.ru\",
\"password\": \"passwordNew\",
\"roles\": [\"USER\"]
}"

delete User
curl -X DELETE --location "http://localhost:8080/topjava/rest/admin/users/100001" \
-H "Content-Type: application/json" \
-d "{}"

update User
curl -X PUT --location "http://localhost:8080/topjava/rest/admin/users/100000" \
-H "Content-Type: application/json" \
-d "{\"name\": \"UserUpdated\",
\"email\": \"user@yandex.ru\",
\"password\": \"passwordNew\",
\"roles\": [\"USER\"]
}"

get Profile
curl -X GET --location "http://localhost:8080/topjava/rest/profile" \
-H "Accept: application/json"

update Profile
curl -X PUT --location "http://localhost:8080/topjava/rest/profile" \
-H "Content-Type: application/json" \
-d "{\"name\": \"New777\",
\"email\": \"new777@yandex.ru\",
\"password\": \"passwordNew\",
\"roles\": [\"USER\"]
}"

delete Profile
curl -X DELETE --location "http://localhost:8080/topjava/rest/profile" \
-H "Content-Type: application/json"

get All Meals
curl -X GET --location "http://localhost:8080/topjava/rest/meals" \
-H "Accept: application/json"

get Meals 100003
curl -X GET --location "http://localhost:8080/topjava/rest/meals/100003" \
-H "Accept: application/json"

filter Meals
curl -X GET --location "http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=07:00:00&endDate=2020-01-30&endTime=15:00:00" \
-H "Accept: application/json"

delete Meals
curl -X DELETE --location "http://localhost:8080/topjava/rest/meals/100003" \
-H "Content-Type: application/json"

create Meals
curl -X POST --location "http://localhost:8080/topjava/rest/meals" \
-H "Content-Type: application/json" \
-d "{\"dateTime\":\"2020-02-01T12:00\",
\"description\":\"Created lunch\",
\"calories\":300
}"

update Meals
curl -X PUT --location "http://localhost:8080/topjava/rest/meals/100004" \
-H "Content-Type: application/json" \
-d "{\"dateTime\":\"2020-01-30T07:00\",
\"description\":\"Updated breakfast\",
\"calories\":200
}"




