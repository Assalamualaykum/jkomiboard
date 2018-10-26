;RepltestiPala
(ns komiboard
  (:gen-class))

(declare onkoYmparoity ymparoidytKivet voikoSyoda initBoard aloitaPeli paikanValinta vuoronValinta asetaListaan poistaListasta kivenAsetusJaTarkistus voikoAsettaaJaSyoda)

;paikanValinta palauttaa valitun paikan muodossa [Y X]
;voikoAsettaaJasSyoda taas settaa kiven mikälisen asettaminen on mahdollista ja palauttaa syötävät kivet.
(defn aloitaPeli []
  (def board (initBoard))
  (def tamanhetkinenVuoro (vuoronValinta))
  (def valittuPaikka (paikanValinta))
  ;jos if lause on totta ja paikka on sallittu, aseta kivi paikalleen
  (kivenAsetusJaTarkistus valittuPaikka board tamanhetkinenVuoro)
  ;poistalistasta
  ;recur vaihdavuoro?
  ;tilannetta jossa pelaaja asettaa kivensä tyhjään kohtaan jossa kaikki ympäröivät kivet ovat omia,
    ;jotka kuitenkin ovat kaikki vihollisen ympäröimiä ei ole tarkistettu

  ;miten boardia päivitetään? for looppi?
  )

(defn initBoard []
  (println "How about a game of komi, old friend")
  (def tyhjaBoard [["W" "W" "W" "W" "W" "W" "W"]
               ["W" "R" "G" "G" "R" "E" "W"]
               ["W" "R" "R" "G" "R" "E" "W"]
               ["W" "E" "E" "E" "E" "E" "W"]
               ["W" "E" "E" "E" "E" "E" "W"]
               ["W" "E" "E" "E" "E" "E" "W"]
               ["W" "W" "W" "W" "W" "W" "W"]])
  tyhjaBoard
  )

(defn vuoronValinta []
  (def valinta [1 1])
  (println "Which player starts? R for red, G for green")

  (def vuoroKirjain (read-line))
  (def kiviVuoro
    (loop [kiviVuoro vuoroKirjain]
      (if (not (or (= kiviVuoro "G") (= kiviVuoro "R")))
        (do
          (println "This letter (" kiviVuoro ") is neither R or G, please enter the value again, note the capitalization")
          (def kiviVuoro1 (read-line))
          (recur kiviVuoro1)
          )
        kiviVuoro
        )
      )
    )
  )

(defn paikanValinta []
  (println kiviVuoro "Aloittaa, valitse ensimmäinen paikka johon aiot sijoittaa kiven,
      anna paikkvalintasi muodossa Y X, valitse paikoista [1 1] - [5 5]
      Anna ensin rivi, sitten sarake.")

  ;(def paikkavalinta [(read-line) (read-line)])
  (def valittupaikka1 (read-line))
  (def paikkavalinta2 (read-line))
  (def paikkavalinta
    (loop [paikkavalinta1 valittupaikka1 paikkavalinta2 paikkavalinta2]
      (println "paikkavallinnan arvot?" paikkavalinta1 paikkavalinta2)
      (if (not (and (or (= paikkavalinta1 "1") (= paikkavalinta1 "2") (= paikkavalinta1 "3") (= paikkavalinta1 "4") (= paikkavalinta1 "5"))
               (or (= paikkavalinta2 "1") (= paikkavalinta2 "2") (= paikkavalinta2 "3") (= paikkavalinta2 "4") (= paikkavalinta2 "5"))))
        (do
          (println "These letters (" paikkavalinta1 " and " paikkavalinta2 ") aren't both 1, 2, 3, 4 or 5, please enter the letters again")
          (def palautettupaikka1 (read-line))
          (def palautettupaikka2 (read-line))
          (recur palautettupaikka1 palautettupaikka2)
          )
        paikkavalinta
        )
      )
    )
  )

;Tarkistetaan onko kiven asetus sallittua ja syödäänkö kiviä.
(defn kivenAsetusJaTarkistus [valittuPaikka board vuoro]

  (def palautetutPaikat (valittuPaikka voikoAsettaaJaSyoda board vuoro))

  ;vaihtoehto, asetetaan ilman syöntiä
  (if (= palautetutPaikat 1)
    ;aseta vain alun perin valittu kivi, ei syötäviä paikkoja
    (println "Kiven asetus ilman lisatoimia")
    )
  ;vaihtoehto, ympäröity
  (if (= palautetutPaikat 2)
    ;ala tee mitään ja huuda käyttäjälle vääränlaisesta asetuksesta
    (println "Valittu paikka on ympäröity")
    )
  ;vaihtoehto, varattu
  (if (= palautetutPaikat 3)
    ;ala tee mitään ja huuda käyttäjälle vääränlaisesta asetuksesta
  (println "Valitussa paikassa on jo kivi")
    )
  ;vaihtoehto, asetetaan ja syödään
  (if (not (contains? palautetutPaikat nil))
    ;vaihtoehto, syödään
    ;aseta tyhjää kaikkiin syötyihin paikkoihin
    (asetaListaan board palautetutPaikat vuoro)
    )
  ;vaihtoehto, rekursiohirviö kutsuttu mutta ei syödä
  (if (contains? palautetutPaikat nil)
      ;älä syö, mutta aseta vuoron omistajan kivi
      (println "Ei mitään 2")
      )
    )

;ota board ja lisää siihen annetut paikkojen kohdat tietyksi vuoromerkiksi
(defn asetaListaan [board paikat vuoro]
  (println "Testejä" paikat " " )
  (println "Testejä" paikat " " (some paikat vector?))
  (if (some paikat vector?)
    ;true, aseta vain annettu paikka
    (let [uusiBoard (assoc-in board paikat vuoro)]
      uusiBoard
      )
    ;else, aseta vektorissa paikat annettuun kenttään
    (loop [i 0 uusiBoard board]
      (println "Testejä" paikat " " (get paikat i))
      (when (< i (count paikat))
        (println (type (get paikat i)))
        (if (= (+ i 1) (count paikat))
          uusiBoard
          (recur (inc i) (assoc-in uusiBoard (get paikat i) vuoro))
          )
        )
      )
    )
  )

;TARVITAANKO TÄTÄ!?
;ota board ja poista siitä annetut paikkojen kohdat tietyksi merkiksi
(defn poistaListasta [board paikat merkki]
     (loop [i 0 uusiBoard board]
       (when (< i (count paikat))
         (type (get paikat i))
         (if (= (+ i 1) (count paikat))
           uusiBoard
           (recur (inc i) (assoc-in uusiBoard (get paikat i) merkki))
           )
         )
       )
     )

;tarkistetaan onko paikassa oleva kivi syöty,
;kivi on syöty mikäli sitä ympäröivät kivet ovat joko vastustajan tai seiniä
; palautusarvo nil mikäli ei mikäli kiveä ei voida syödä, true jos syöty
(defn onkoSyoty [paikka kentta vuoro]
  (def ymparoivatKivet (ymparoidytKivet paikka kentta))
    (loop [i 0]
      (when (< i 4)
        (println i)
        ;lopeta mikäli vieressäoleva kivi on oma taikka tyhjä, ja palauta false
        (if (or (= (get ymparoivatKivet i) "E")
                (= (get ymparoivatKivet i) vuoro))
          false
          (if (= i 3)
            true
            (recur (inc i))
            )
          )
        ) ;when
      ) ;loop
  )

;Funktio palauttaa kutsuttavaa paikkaa ympäröivät kivet jarjestyksessa vasen, oikea, yla, ala
(defn ymparoidytKivet [paikka kentta]
  ;palauttaa taman muuttujan, def jotta muuttuja muuttuisi kutsujen välillä
  (println "PAIKKA" paikka)
  (def kivet [(get-in kentta [(get paikka 0) (- (get paikka 1) 1)])
              (get-in kentta [(get paikka 0) (+ (get paikka 1) 1)])
              (get-in kentta [(- (get paikka 0) 1) (get paikka 1)])
              (get-in kentta [(+ (get paikka 0) 1) (get paikka 1)])])
  kivet
  )

;Funktio palauttaa kutsuttavaa paikkaa ympäröivien kivien paikat jarjestyksessa vasen, oikea, yla, ala
(defn ymparoidytPaikat [paikka]
  ;palauttaa taman muuttujan, def jotta muuttuja muuttuisi kutsujen välillä
  (def paikat [[(get paikka 0) (- (get paikka 1) 1)]
               [(get paikka 0) (+ (get paikka 1) 1)]
               [(- (get paikka 0) 1) (get paikka 1)]
               [(+ (get paikka 0) 1) (get paikka 1)]])
  paikat
  )

;Palauttaa valitun kiven viereiset kivet jotka ovat syötävissä tästä paikasta
(defn otaSyotavat [alkPerKivi annettuboard]
  (def annetutSivuKivet (ymparoidytKivet alkPerKivi annettuboard))
  (def annetutSivuPaikat (ymparoidytPaikat alkPerKivi))
  (loop [syontiTarkistettavat [] i 0]
    ;tarkasteltavaKivi sama kuin talla hetkella tarkistettava kivi, onko vihollinen
    (def tarkasteltavaKivi (get annetutSivuKivet i))
    ;alkuperainen kivi jonka paikkaa testataan,
    ;sama kuin vuoron omistavan kivi joka olisi asetettu vaikka paikka olisikin tyhja
    (def asetettavaKivi (get-in annettuboard alkPerKivi))
    (when (< i 4)
      ;tämä ensimmäinen if palauttaa mahdollisen vihollisen kiven sillon kuin se on tarkasteltavan kiven vieressä
      (def lisattavaKohta (if (not (or (= tarkasteltavaKivi "W") (= tarkasteltavaKivi "E") (= tarkasteltavaKivi asetettavaKivi)))
                            (get annetutSivuPaikat i)
                            ;(println "else")
                            ))
      ;(println lisattavaKohta "(lisattava ja tarkistettavat)" syontiTarkistettavat)
      ;(println (not (or (= tarkasteltavaKivi "W") (= tarkasteltavaKivi "E") (= tarkasteltavaKivi asetettavaKivi))))

      ;(println "loopattava " tarkasteltavaKivi)
      ;(println "permakivi " asetettavaKivi)

      ;(println "testit")
      ;(println (= tarkasteltavaKivi "W"))
      ;(println (= tarkasteltavaKivi "E"))
      ;(println (= tarkasteltavaKivi asetettavaKivi))

      ) ;When
    ;tässä korjaan lisättävän arvon nil arvon tyhjäksi
    (if (and (< i 4) (= lisattavaKohta nil))
      (recur (get (into [] (conj lisattavaKohta syontiTarkistettavat)) 0) (inc i))
      (if (< i 4)
        (recur (conj syontiTarkistettavat lisattavaKohta) (inc i))
        syontiTarkistettavat
        )
      )
    ;jos palautusarvon laittaa tahan, syntyy loputon looppi, korjattu if-lauseilla
    ) ;loop
  )

;Rekursiohirviö joka selvittää kaikki ne kivet jotka ovat samassa ryppäässä kutsutun kiven kanssa,
;palauttaa listan missä ei ole arvoa nil mikäli rypäs on syötävissä eli ympäröity
(defn rekursiohirvio [paikka kentta tarkistetutLista]
  (println "tämän kutsun paikat" paikka tarkistetutLista)
  ;tarkistetaan aluksi ovatko aiemmat hirviöt löytäneet tyhjää
  (if (not (some #(= nil %) [tarkistetutLista]))
    (do
      ;tarvittavat alustukset, kirjoitettu vasta tyhjäntarkistuksen jälkeen optimoinnin vuoksi
      (let [vierekkaisetKivet (ymparoidytKivet paikka kentta)]
      (println "Vierekkaisetkivet kutsu" vierekkaisetKivet)
      (let [vierekkaisetPaikat (ymparoidytPaikat paikka)]
      (println "vierekkaisetPaikat kutsu" vierekkaisetPaikat)
      ;lisätään uusin rekursiohirviökutsuttu kivi listaan
      (if (not (some #(= paikka %) tarkistetutLista))
        (def tarkistetutLista1 (conj tarkistetutLista paikka))
        )
      (println "toimii" tarkistetutLista1)
      (loop [i 0, syotavatKivet tarkistetutLista1]
        (let [tamanhetkinenKivi (get vierekkaisetKivet i)]
        (let [tamanhetkinenPaikka (get vierekkaisetPaikat i)]
        (when (< i 5)
          (println syotavatKivet tamanhetkinenPaikka tamanhetkinenKivi i)
          ;onko listassa tarkasteltavalla kivellä kutsuttu rekursioHirviötä?
          (if (and (not (some #(= tamanhetkinenPaikka %) syotavatKivet)) (< i 4))
            ;onko tarkasteltava paikka tyhjä?
            (if (= tamanhetkinenKivi "E")
              ;true palauta lista jossa on nil, valitettavasti tässä ratkaisussa hukataan listan aiemmat noodit
              (recur (+ i 1) (conj syotavatKivet nil))
              ;else: onko vieressäoleva kivi oma eli parametriksi annetun, kutsutun kiven väri?
              (if (= tamanhetkinenKivi (get-in kentta paikka))
                (recur (+ i 1) (rekursiohirvio tamanhetkinenPaikka kentta syotavatKivet))
                ;else jatka seuraavaan loopin kiveen, älä tee mitään
                (recur (+ i 1) (conj syotavatKivet ""))
                ); if-4 onkoRyppaassa/oma
              ); if-3 onkotyhja
            ;else jatka seuraavaan loopin kiveen, sillä viimeisin oli jo tarkistettu ------Ei konsistentti palautus.-----
            (if (= i 4)
              ;kun looppi loppuu palautetaan tähän asti muodostettu tarkistettujen noodien lista
              syotavatKivet
              (recur (+ i 1) (conj syotavatKivet ""))
              )
            ); if-2 onkotarkistettu^
          );when
          ));Let3;Let4
        );loop
      ;else, älä tee mitään vaan jatka seuraavan hirviön kohteesta
      ));Let1;Let2
      );do, if-1 sisässä
    ); if-1 tarkistatyhja
  )

;Tarkistetaan voiko seuraavaksi asetettava kivi syödä vieresssäolevia kiviä, ja jos pystyy, kutsuu rekursiohirviötä
  ;Mikäli rekursiohirviön seurauksena asetetaan uusi kivi kenttään, kutsu palauttaa uuden kentän johon uusi kivi on sijoitettu
(defn voikoAsettaaJaSyoda [paikka kentta vuoro]
  (def vierekkaisetSyotavatPaikat (otaSyotavat paikka kentta))
  (println vierekkaisetSyotavatPaikat "NAMA PITAISI SYODA")
  ;tarkistetaan onko paikka jo varattu
  (if (not= vuoro paikka)
    ;tarkistetaan aluksi onko paikka sallittu eli siis sitä ei ole ympäröity
    (if (= (onkoSyoty paikka kentta vuoro) false)
      ;seuraavaksi vielä tarkistetaan voidaanko kivi vain asettaa paikalleen mikäli syötäviä kiviä ei ole
      (if (not= 0 (count vierekkaisetSyotavatPaikat))
        ;tässä loopissa käydään kutsumassa rekursiohirviötä jokaiselle syötävälle kivelle
          ;optimoinnin vuoksi syönti tehdään heti sen jälkeen kun rekursiohirviö palauttaa syötäviä paikkoja
        (loop [i 0 palautettavaBoard kentta]
          (when (< i (+ (count vierekkaisetSyotavatPaikat) 1))
            ;tämä if lause on viimeistä looppia varten jolloin uusiboard palautetaan varsinaisen loopin sijaan
            (if (< i (count vierekkaisetSyotavatPaikat))
            (do
              (def tamanhetkinenPaikka (get vierekkaisetSyotavatPaikat i))
              (println "Tamanhetkinen syova kivi" (get-in kentta tamanhetkinenPaikka))
              (def rekursionPalautus (rekursiohirvio tamanhetkinenPaikka kentta []))
              ;vaihtoehto, syödään
              (println "TESTIT 2 " paikka palautettavaBoard rekursionPalautus)
              (println "TESTIT 3 " (some nil? rekursionPalautus))
              (if (some nil? rekursionPalautus)
                ;Aseta kivi valittuun paikkaan ilman syöntiä
                (recur (+ i 1) (asetaListaan palautettavaBoard paikka vuoro))
                ;aseta tyhjää kaikkiin syötyihin paikkoihin
                (let [uusiboard (asetaListaan palautettavaBoard rekursionPalautus "E")] ;-------------------------------ASETALISTAAN TOIMII LISTALLA JOSSA NIL
                  (println "ÄLÄ MEE TÄHÄN NIL ARVOJEN KANSSA")
                  (println "Syodaan paikat: " rekursionPalautus " Ja asetettiin kivi '" vuoro "' kohtaan '" paikka "'")
                  (recur (+ i 1) (asetaListaan uusiboard paikka vuoro))
                  )
                )
              ;vaihtoehto, rekursiohirviö kutsuttu mutta ei syödä
              ;(if (contains? rekursionPalautus nil)
                ;älä syö, mutta aseta vuoron omistajan kivi
                ;(recur (+ i 1) (asetaListaan palautettavaBoard paikka vuoro))
                ;)
              )
              palautettavaBoard
            )
            )
          ) ;loop
        ;palautetaan numero yksi, tarkoitetaan että kivi voidaan asettaa ilman lisäseuraamuksia.
        1
        );if 3
      ;palautetaan numero kaksi, Ei loopattu, tahan ei voida asettaa kivea, syontiuhka tai kivi tiella
      2
      );if 2
    3
    );if 1
  )


(def board2 [["W" "W" "W" "W" "W" "W" "W"]
             ["W" "R" "G" "G" "R" "E" "W"]
             ["W" "R" "R" "G" "R" "E" "W"]
             ["W" "E" "R" "E" "R" "E" "W"]
             ["W" "E" "E" "R" "E" "G" "W"]
             ["W" "E" "E" "E" "G" "E" "W"]
             ["W" "W" "W" "W" "W" "W" "W"]])
(def valinta [3 3])
(def tyhjapaikka [4 4])
(println (voikoAsettaaJaSyoda valinta board2 "R")) ;ei toimi sillä paikka johon asetetaan on tyhjä, rekursiohirviö kusee
;(println (voikoAsettaaJaSyoda tyhjapaikka board2 "R"))
;(println (aloitaPeli))
(def valinta [5 5])
;(println (onkoSyoty valinta board2 "R")) ;tee vielä niin että tämä ottaa humioon vuoron

;(def tarkistettavat [[1 1] [2 2]])
(def annetut [[1 0] [1 2] [0 1] [2 1]])
(def annettu [1 0])
;(println (asetaListaan board2 annettu "G"))
;(println annetut)

;yksikkötesti
(def rekursionPalautus [[4 5] [4 4]])
(println "TESTIT 3 " (not (contains? rekursionPalautus nil)) (contains? rekursionPalautus nil))

(def asd [[4 5] [4 4] nil])
(println "TESTIT 3 "  (some nil? asd) (some nil? rekursionPalautus))