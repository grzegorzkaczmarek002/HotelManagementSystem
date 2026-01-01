# Hotel Management System - Projekt Inżynierski

## Uruchamianie:
Aplikacja domyślnie uruchamia się na porcie 9090. Adres lokalny: http://localhost:9090
## Opis projektu:
Aplikacja bazodanowa do zarządzania hotelem.

## Cel projektu:
Stworzenie systemu zarządzania hotelem, który będzie spełniał wiele wymagań, które stoją przed usługami hotelarskimi. Aplikacja zapewnia sprawne zarządzanie danymi, bezpieczeństwo informacji oraz intuicyjny interfejs użytkownika.

## Opis działania:
System będzie działał na zasadzie aplikacji internetowej. Użytkownik poprzez przeglądarkę uzyska dostęp do systemu, 
który jest oparty na serwerze www. Następnie poprzez interfejs graficzny będą wykonane operacje, które 
zostaną przesłane do bazy danych. 

## Architektura:
System jest oparty na architekturze MVC:
- Model: Logika biznesowa i struktura danych oparta na encjach. Do zarządzania wykorzystywane jest Spring Data JPA, który udostępnia operacje typu CRUD w bazie danych oraz Hibernate, który służy do mapowania obiektowo-relacyjnego.
- Widok: Interfejs użytkownika zbudowany w HTML/CSS przy użyciu silnika Thymeleaf.
- Kontroler: Odpowiada za logikę aplikacji, przetwarzanie żądań oraz za interakcje między widokiem a modelem. Obsługuje żądania HTTP, a następnie zwraca odpowiednie widoki.

## Scenariusze użytkowania:
- Klient: kontaktuje się z recepcją w celu dokonania rezerwacji. Recepcjonista wprowadza dane do systemu.
- Recepcjonista: zarządza klientami i rezerwacjami.
- Sprzątacz: zmienia statusy pokoi, gdy zostaną one posprzątane.
- Kierownik: zarządza dodatkowo pracownikami, pokojami i stawkami.
- Administrator: zarządza systemem i bazą danych.


## Projekt zabezpieczeń:
- Logowanie za pomocą loginu i hasła: System weryfikuje dane użytkownika, sprawdzając ich zgodność z danymi w bazie danych.
- Role i uprawnienia: Każdy pracownik ma przypisaną rolę, która określa jego dostęp do funkcji aplikacji.
- Mechanizm blokady konta: Po 5 próbach nieudanych próbach konto jest czasowo blokowane na 15 minut, co chroni przed atakami typu bruce-force.
- Szyfrowanie danych: Pesel i hasła są szyfrowane za pomocą algorytmu BCrypt.
- Ochrona przed SQL Injection : Operacje na bazie danych są realizowane za pomocą zapytań przygotowanych w Spring Data JPA, co eliminuje możliwość wstrzykiwania niebezpiecznego kodu SQL.
- Ochrona przed Cross-Site Scripting: Dane wprowadzone przez użytkowników są sprawdzane przed zapisaniem lub wyświetleniem oraz mechanizmy wbudowane w Thymeleaf pomagają w kodowaniu wyjścia w HTML.
- Ochrona Cross-Site Request Forgery (CSRF): Mechanizm CSRF jest domyślnie aktywowany w Spring Security.
- System logów: Każda operacja w systemie jest rejestrowana w tabeli Logi.
- Silne hasła: Hasła muszą mieć długość minimum 8 znaków oraz zawierać 1 dużą literę, małą literę, cyfrę i znak specjalny.
  
## Użyte technologie
- Java
- Spring Boot
- Spring Security
- Hibernate
- Spring Data JPA
- Thymeleaf
- Maven
- MySQL
- HTML
- CSS
