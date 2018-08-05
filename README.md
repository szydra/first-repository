# Result Counter
Repozytorium zawiera aplikację napisaną w Spring Boot, która wypisuje przybliżoną liczbę wyników zwracanych dla podanych argumentów przez wyszukiwarkę Google.

Uruchamiając aplikację z podanymi argumentami, przetwarza ona każdy z nich w osobnym wątku. Sprawdza, czy dane zapytanie zostało już zarchiwizowane w bazie danych; jeżeli tak, to pobiera je, a jeżeli nie, to łączy się ze stroną Google, parsuje liczbę wyników i dodaje uzyskany rezultat do bazy danych. Następnie aplikacja wypisuje wszystkie wyniki malejąco według popularności.

Dostępne flagi to
* **--clear-db** usuwa wszystkie dane zapisane w bazie;
* **--show-db** pokazuje wszystkie dane zapisane w bazie;
* **--remove** usuwa z bazy rekordy dla podanych argumentów.

Przed pierwszym uruchomieniem programu należy utworzyć bazę danych, przechodząc do katalogu sql i wykonując polecenie:
`./create_db.bash results.db`.
