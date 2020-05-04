# DS_Gr.26
*Kodi që e kemi ngarkuar 10 ditë me parë, nuk u hapke andaj e kemi fshirë, për shkaqe kofidencialiteti, mund t’i shihni edhe n’branchat e tonë që i kemi fshi *

𝗙𝗔𝗭𝗔 𝟭- 𝗦𝗶𝗴𝘂𝗿𝗶 𝗲 𝘁𝗲 𝗱𝗵𝗲𝗻𝗮𝘃𝗲
Grupi ynë ka pasur për detyrë 3 komandat siashtu dhe nënkomandat në vijim:
1) 𝙆𝙤𝙢𝙖𝙣𝙙𝙖 𝙁𝙧𝙚𝙦𝙪𝙚𝙣𝙘𝙮
Kjo komandë në konzolë tregon se sa herë janë përseritur shkonjat në fjalinë e dhënë nga përdoruesi.
Siashtu kemi bërë edhe paraqitjen e shkronjave me anë të 𝗔𝗦𝗖𝗜𝗜 𝗴𝗿𝗮𝗽𝗵-it.

2) 𝙆𝙤𝙢𝙖𝙣𝙙𝙖 𝙑𝙞𝙜𝙚𝙣𝙚𝙧𝙚 dhe dy nënkomandat: 
   2𝒂)𝑵ë𝒏𝒌𝒐𝒎𝒂𝒏𝒅𝒂 𝒑ë𝒓 𝒆𝒏𝒌𝒓𝒊𝒑𝒕𝒊𝒎 
   2𝒃)𝑵ë𝒏𝒌𝒐𝒎𝒂𝒏𝒅𝒂 𝒑ë𝒓 𝒅𝒆𝒌𝒓𝒊𝒑𝒕𝒊𝒎.
Qëllimi i kësaj komande ka qenë të enkriptohet dhe dekriptohet fjalia e dhënë, duke i marrë parasysh edhe hapsirat mes fjalëve.

3) 𝙆𝙤𝙢𝙖𝙣𝙖𝙙𝙖 P𝙡𝙖𝙮𝙛𝙖𝙞𝙧 dhe dy nënkomandat e saj: 
    3𝒂)𝑵ë𝒏𝒌𝒐𝒎𝒂𝒏𝒅𝒂 𝒑ë𝒓 𝒆𝒏𝒌𝒓𝒊𝒑𝒕𝒊𝒎 
    3𝒃)𝑵ë𝒏𝒌𝒐𝒎𝒂𝒏𝒅𝒂 𝒑ë𝒓 𝒅𝒆𝒌𝒓𝒊𝒑𝒕𝒊𝒎. 
Qëllimi i kësaj komande ka qenë që fillimisht fjalinë e dhënë nga përdoruesi t’a paraqesim në formë 𝐭𝐚𝐛𝐞𝐥𝐞(𝐦𝐚𝐭𝐫𝐢𝐜𝐞) 𝟓𝐱𝟓, e mandej 
me anë të dy nënkomandave, fjalinë e dhënë nga përdoruesi t’a enkriptojmë dhe dekriptojmë, por me kusht që  enkriptimi t’a ndan
 fjalinë dy nga dy shkronja (𝐝𝐢𝐠𝐫𝐚𝐩𝐡).

Gjuha që kemi përdor për implementimin e kësaj faze, është 𝐉𝐚𝐯𝐚. 
Kodi i komandës frequency ka referencën e saj (se ku jemi bazu), ndërsa dy komandat e tjera i kemi bërë pothuajse (80%) 
vetëm (me prejashtim ndonjë tutoriali,por sidoçoftë kemi dhënë disa referenca edhe për to)

                    ** 𝘔𝘦𝘯𝘺𝘳𝘢 𝘴𝘦 𝘴𝘪 𝘪 𝘬𝘦𝘮𝘪 𝘦𝘨𝘻𝘦𝘬𝘶𝘵𝘶𝘢𝘳 𝘬𝘰𝘮𝘢𝘯𝘥𝘢𝘵**
Përmes 𝐛𝐚𝐭𝐜𝐡 𝐟𝐢𝐥𝐞. Kemi marr kodin, kemi ekstraktuar, më pas kemi fituar file-n 𝐌𝐚𝐢𝐧.𝐣𝐚𝐫 .Më pas për secilën komandë kemi bërë nga një file të re me prapashtesën 𝐛𝐚𝐭, ku ne te kemi shkruar Pathin (psh për komandën playfair)si në vijim: 𝒋𝒂𝒗𝒂 -𝒋𝒂𝒓 "𝑴𝒂𝒊𝒏.𝒋𝒂𝒓" "𝒑𝒍𝒂𝒚𝒇𝒂𝒊𝒓" 𝒆𝒏𝒄𝒓𝒚𝒑𝒕 "𝒌𝒆𝒚 " "𝑷𝒆𝒓𝒔𝒉𝒆𝒏𝒅𝒆𝒕𝒋𝒆 𝒏𝒈𝒂 𝑭𝒊𝒆𝒌" 𝒑𝒂𝒖𝒔𝒆, ose mënyra dyte, duke shkuar në pathin file-s tek i cili kemi vendosur shkurtesen '𝐜𝐦𝐝' dhe më pas është hapur konzolat për të marr  argumentet.
Shembujt se si janë ekzekutuar, mund t'i gjeni tek fotot qe i kemi ngarkuar me siper.

Referenca per Create-User:
https://github.com/codybartfast/java-to-dotnet-signature/blob/master/CreateKeysJ/src/CreateKeysJ.java
Referenca per komanden write-message:
https://www.w3schools.com/java/java_files_create. - Ruajtja e tekstit te enkriptuar ne path-in e dhene nga perdoruesi
https://gist.github.com/ufologist/5581496 - ne kete link jemi bazuar per enkriptimin/dekriptimin e DES-it.
Referenca per encrypt dhe decrypt:
 https://github.com/only2dhir/rsaencryption/blob/master/src/main/java/com/devglan/rsa/RSAUtil.java
 https://stackoverflow.com/questions/11410770/load-rsa-public-key-from-file
