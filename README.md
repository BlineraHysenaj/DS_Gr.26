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

Komanda e parë e krijon një user dhe e ruan tek file-t me .xml
Siashtu kjo komandë i përmbahet kërkesave të projektit, pra emrat përmbajnë vetëm simbolet A-Z, a-z, 0-9,
dhe _, dhe nuk përmbajnë hapësira.

Komanda e dytë e fshin userin e krijuar më parë, pra i largon celësat ekzistues të user-it.
Komanda e tretë e eksporton celësin privat ose publik nga direktorumi i celësave.
Pra ne ja jepim mundësinë shfrytëzuesit që me zgjedh cilin nga celësat dëshiron me i eksportu.
Siashtu edhe përmes argumentit opcional [file] kemi përcaktu pathin se ku do ruhet celësi i eksportuar.
Siashtu në rastin kur nuk e dimë celësin privat të shfrytëzuesit, e në rast se përzgjedhja është 'private' atëherë në konzolë del që ai celës nuk egziston.







