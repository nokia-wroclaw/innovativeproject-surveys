# innovativeproject-surveys
Surveys app with anonimity in mind

heroku url:		https://survey-innoproject.herokuapp.com/

API:
POST      /spp/login          formularz logowania
POST      /app/logout         zakończenie sesji
PUT       /app/user/@login 	  rejestracja nowego konta

GET       /app/users          zwrócenie kont użytkowników

GET       /app/about          informacje o aktualnie zalogowanym użytkowniku
POST      /app/invite         wysłanie zaproszenia

GET       /app/users/@login      użytkownik o login
POST      /app/users/@login      edycja danych użytkownika o login
DELETE    /app/users/@login      usunięcie użytkownika o id

GET       /app/surveys        zwrócenie ankiet
PUT       /app/surveys        dodanie nowej ankiet

GET       /app/surveys/@id    zwrócenie ankiety o danym id
POST      /app/surveys/@id    edycja ankiety
DELETE    /app/surveys/@id    usunięcie ankiety
