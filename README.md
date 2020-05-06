# DS_Gr.26

#Faza 2
Në këtë fazë të projektit, kemi pas detyrë që me i implementu komandat:
--Komanda Create-user--
--Komanda Delete-user--
--Komanda Import-Key--
--Komanda Export-Key--
--Komanda Write-message--
--Komanda Read-message--
Direktorumin e celesave e kemi ruajtur ne formatin .xml
Per te dalluar celesat publik nga ata privat, kemi perdorur parashtesen .pub.
Ekzekutimi dhe Kompajllimi i tyre bëhet përmes argumenteve.

* Komanda e parë e krijon një user qe permban celesat e gjeneruar privat dhe publik dhe e ruan tek file-t me .xml
Siashtu kjo komandë i përmbahet kërkesave të projektit, pra emrat përmbajnë vetëm simbolet A-Z, a-z, 0-9,
dhe _, dhe nuk përmbajnë hapësira.
Ekzekutimi:
create-user <test >

* Komanda e dytë e fshin userin e krijuar më parë, pra i largon celësat ekzistues të user-it.
Ekzekutimi:
delete-user <test >
  
* Komanda e tretë e eksporton celësin privat ose publik nga direktorumi i celësave.
Pra ne ja jepim mundësinë shfrytëzuesit që me zgjedh cilin nga celësat dëshiron me i eksportu.
Siashtu edhe përmes argumentit opcional [file] kemi përcaktu shtegun e fajjlit se ku do ruhet celësi i eksportuar.
Nese ky argument mungon celesi vetem paraqitet ne console
Ekzekutimi:
export-key <public|private> <test> [test.xml]
  
* Komanda e katert e importon celesin nga shtegu i kerkuar ne direktorumin e celësave
Nese celesi qe do te importohet eshte privat automatikisht me te importohet edhe celesi publik qe te ruhen bashke ne direktorumin e celesave 
Me ane te argumentit <name> percaktojme emrin e celesit qe ruhet ne direktorum
Ekzekutimi:
import-key fiek test.pub.xml
  
*  Komanda e peste enkripton nje mesazh dedikuar nje user-i
Me argumentin opcional [file] caktojme shtegun se ku do te ruhet teksti i enkriptuar, nese mungon ky argument teksti shfaqet ne console
Me argumentin <name> caktojme user-in per te cilin eshte mesazhi dhe perdorim celesin publik te tij per enkriptim
Ekzekutimi:
write-message fiek pershendetje enkriptimi.txt
  
*  Komanda e gjashte dekripton mesazhin e enkriptuar ne metoden paraprake
Ekzekutimi:
read-message "ZWRvbg==.MTIzNDU2Nzg=.cnNhKGZpZWsyMDE4KQ==.ZGVz..."
  
  
  
  
  
  
  
