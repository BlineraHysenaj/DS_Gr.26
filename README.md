# DS_Gr.26
***Kodi që e kemi ngarkuar 10 ditë me parë, nuk u hapke andaj e kemi fshirë, për shkaqe kofidencialiteti, mund t’i shihni edhe n’branchat e tonë që i kemi fshi !!!

FAZA 1- Siguri e te dhenave
Grupi ynë ka pasur për detyrë 3 komandat siashtu dhe nënkomandat në vijim:
1) Komanda Frequency 
Kjo komandë në konzolë tregon se sa herë janë përseritur shkonjat në fjalinë e dhënë nga përdoruesi.
Siashtu kemi bërë edhe paraqitjen e shkronjave me anë të ASCII graph-it.

2) Komanda Vigenere dhe dy nënkomandat 2a)Nënkomanda për enkriptim dhe 2b)Nënkomanda për dekriptim.
Qëllimi i kësaj komande ka qenë të enkriptohet dhe dekriptohet fjalia e dhënë, duke i marrë parasysh edhe hapsirat mes fjalëve.

3)Komanada playfair dhe dy nënkomandat e saj 3a) Nënkomanda për enkriptim dhe 3b)Nënkomanda për dekriptim. 
Qëllimi i kësaj komande ka qenë 
që fillimisht fjalinë e dhënë nga përdoruesi t’a paraqesim në formë tabele(matrice) 5x5, e mandej me anë të dy nënkomandave, fjalinë e dhënë nga përdoruesi t’a enkriptojmë dhe dekriptojmë, por me kusht që  enkriptimi t’a ndan fjalinë dy nga dy shkronja (digraph).
Gjuha që kemi përdor për implementimin e kësaj faze,është Java. 
Kodi i komandës frequency ka referencën e saj( se ku jemi bazu), ndërsa dy komandat e tjera i kemi bërë pothuajse (80%) vetëm (me prejashtim ndonjë tutoriali,por sidoçoftë kemi dhënë disa referenca edhe për to)

** Menyra se si i kemi egzekutuar komandat**
 Permes batch file. Kemi marr kodin, kemi ekstraktuar, me pas kemi fituar file-n Main.jar .Me pas per secilen komande kemi bere nga nje file te re me prapashtesen bat, ku ne te kemi shkruar Pathin (psh per komanden playfair)si ne vijim: java -jar "Main.jar" "playfair" encrypt "key " "Pershendetje nga Fiek", ose menyra dyte, duke shkuar ne pathin file-s tek i cili kemi vendosur shkurtesen 'cmd' dhe me pas eshte hapur konzolat per te marr  argumentet.
