In primul rand, pentru myDispatcher, adaug task-urile in functie de algoritmul 
de scheduling pe care il primesc, folosind acolo unde consider ca e nevoie un
synchronized pentru nu a avea race condition (de exemplu,a shortest queue pentru
a fi sigur ca nu se calculeaza size-ul cozii in timp ce alt dispatcher a adaugat 
un task in coada unui host, fac un synchronized pe clasa host). In rest, implementez
algoritmii de scheduling fix cum sunt ei prezentati in tema.
In al doilea rand, in myHost implementez functiile la care se da override(cele din 
schelet) + o functie isRunning creata de mine care ma ajuta sa vad daca un host ruleaza
ceva la un anumit moment care ma ajuta la algoritmul shortest_queue in cazul in care
doua host-uri au o coada la fel de mare de task-uri, insa unul ruleaza un task, in timp
ce al doilea sta degeaba in acel moment. Pentru a-i da shutdown unui host folosesc functia
interrupt pe care o folosesc daca nu mai are nimic de rulat. Pentru a afla cat mai are de 
munca implementez functia getWorkLeft unde adun totul din coada si ceea ce este in rulare
la un moment dat. De asemenea, mai implementez functia getQueuesize() care returneaza mari
-mea cozii. Apoi am implementat functia AddTask din myHost. In implementarea acesteia, prima
oara verific daca e primul task si il adaug in coada pe pozitia 0, altfel verific daca ceea 
ce este in rularea este preemptibil si daca prioritatea task-ului ce trebuie adaugat este mai 
mare decat ceea ce este pe procesor, adaug task-ul in rulare si pe cel preemptibil il pun la 
loc in coada, iar daca nu se verifica vreuna dintre aceste variante il adaug la inceput in coa-
da. De asemenea, inainte sortam coada imediat dupa ce adaugam ceva in ea, insa am realizat ca 
pot sa fac asta in functia de run atunci cand aleg un nou task pe care sa-l rulez(se face sorta-
rea de mai putine ori, doar atunci cand se terminat un task de pe procesor si trebuie ales altul).
Sortarea o fac pe baza prioritatii, iar mai apoi pe baza timpului la care procesele au venit in 
coada. In run, pentru a rula task-urile ma folosesc de Timer-ul implementat din schelet pentru a
cronometra cat timp sta un task pe procesor si daca este preemptibil cat timp mai are de rulat cand
trebuie rulat altul. De asemenea, de fiecare data cand modific coada am grija sa fac un synchronized
pe obiect pentru a nu avea race condition-uri. Intr-un final, el iese din functia de run atunci cand 
primeste intrerrupt din shutdown si conditia din while nu mai este verificata. 
