# innovativeproject-surveys
Surveys app with anonimity in mind

API:
POST      /login          formularz logowania
POST      /logout         zakończenie sesji
PUT       /register       rejestracja nowego konta

GET       /users          zwrócenie kont użytkowników

GET       /about          informacje o aktualnie zalogowanym użytkowniku
POST      /invite         wysłanie zaproszenia

GET       /users/@id      użytkownik o id
POST      /users/@id      edycja danych użytkownika o id
DELETE    /users/@id      usunięcie użytkownika o id

GET       /surveys        zwrócenie ankiet
PUT       /surveys        dodanie nowej ankiet

GET       /surveys/@id    zwrócenie ankiety o danym id
POST      /surveys/@id    edycja ankiety
DELETE    /surveys/@id    usunięcie ankiety
