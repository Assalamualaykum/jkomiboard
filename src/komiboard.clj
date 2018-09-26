;RepltestiPala
(ns komiboard
  (:gen-class))

(declare onkoYmparoity ymparoidytKivet voikoSyoda aloitaPeli paikanValinta vuoronValinta )

(defn aloitaPeli []
  (defn board (initboard))
  (vuoronValinta board) ;palauttaa valitun vuoron, R tai G
  (paikanValinta board) ;palauttaa valitun paikan muodossa [Y X]
  )

(defn initBoard []
  (def tyhjaBoard [["W" "W" "W" "W" "W" "W" "W"]
               ["W" "R" "G" "G" "R" "E" "W"]
               ["W" "R" "R" "G" "R" "E" "W"]
               ["W" "E" "E" "E" "E" "E" "W"]
               ["W" "E" "E" "E" "E" "E" "W"]
               ["W" "E" "E" "E" "E" "E" "W"]
               ["W" "W" "W" "W" "W" "W" "W"]])
  )

(defn vuoronValinta [board1]
  (def board1 [["W" "W" "W" "W" "W" "W" "W"]
               ["W" "R" "G" "G" "R" "E" "W"]
               ["W" "R" "R" "G" "R" "E" "W"]
               ["W" "E" "E" "E" "E" "E" "W"]
               ["W" "E" "E" "E" "E" "E" "W"]
               ["W" "E" "E" "E" "E" "E" "W"]
               ["W" "W" "W" "W" "W" "W" "W"]])
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

(defn paikanValinta [board2]
  (println kiviVuoro "Aloittaa, valitse ensimmäinen paikka johon aiot sijoittaa kiven,
      anna paikkvalintasi muodossa Y X, valitse paikoista [1 1] - [5 5]
      Anna ensin rivi, sitten sarake.")

  (def paikkavalinta [(read-line) (read-line)])
  (println paikkavalinta)
  (def paikkavalinta
    (loop [paikkavalinta paikkavalinta]
      (if (not (or (= paikkavalinta "1") (= paikkavalinta "2") (= paikkavalinta "3") (= paikkavalinta "4") (= paikkavalinta "5")))
        (do
          (println "This number (" paikkavalinta ") is neither 1, 2, 3, 4 or 5, please enter the value again, note the capitalization")
          (def palautettupaikka (read-line))
          (recur palautettupaikka)
          )
        paikkavalinta
        )
      )
    )

  ;takista seuraavaksi voiko kivi syödä mitään, jos ei,
  ;palauta vaarä liike
  ;eikoVoiSyoda [paikka kentta]

  ;kirjoita sitten syönti, tarkista onko kivenasetus järjestetty ennen syöntikutsua niin että kivi myös poistetaan
  ;Kirjoita syötyjen kivien poistamiskutsu. Kirjoita laitaListaan, poistaListasta (boardista)
  )

;tarkistetaan onko paikassa oleva kivi syöty,
;kivi on syöty mikäli sitä ympäröivät kivet ovat joko vastustajan tai seiniä
; palautusarvo nil mikäli ei mikäli kiveä ei voida syödä, true jos syöty
(defn eikoVoiSyoda [paikka kentta]
  (def ymparoivatKivet (ymparoidytKivet paikka kentta))
  (def omaKivi (get-in kentta paikka))
  (loop [i 0 vertailuarvo true]
    (when (< i 4)
      ;(println i)
      ;lopeta mikäli vieressäoleva kivi on oma taikka tyhjä, ja palauta true
      (if (or (= (get ymparoivatKivet i) "E")
              (= (get ymparoivatKivet i) omaKivi))
        vertailuarvo
        (recur (inc i) (= vertailuarvo true))
        )
      );when
    );loop
  )

;Funktio palauttaa kutsuttavaa paikkaa ympäröivät kivet jarjestyksessa vasen, oikea, yla, ala
(defn ymparoidytKivet [paikka kentta]
  ;palauttaa taman muuttujan, def jotta muuttuja muuttuisi kutsujen välillä
  ;(println paikka)
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
      (println lisattavaKohta "asda" syontiTarkistettavat)
      (println (not (or (= tarkasteltavaKivi "W") (= tarkasteltavaKivi "E") (= tarkasteltavaKivi asetettavaKivi))))

      (println "loopattava " tarkasteltavaKivi)
      (println "permakivi " asetettavaKivi)

      (println "testit")
      (println (= tarkasteltavaKivi "W"))
      (println (= tarkasteltavaKivi "E"))
      (println (= tarkasteltavaKivi asetettavaKivi))

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

;Tarkistetaan voiko seuraavaksi asetettava kivi syödä vieresssäolevia kiviä, ja lisätään se listaan
(defn voikoAsettaaJaSyoda [paikka kentta]
  (def vierekkaisetSyotavatPaikat (otaSyotavat paikka kentta))
  (println vierekkaisetSyotavatPaikat "NAMA PITAISI SYODA")
  ;tarkistetaan aluksi onko paikka sallittu ja vapaana
  (if (= (eikoVoiSyoda paikka kentta) true)
    (loop [i 0]
      (when (< i (count vierekkaisetSyotavatPaikat))
        (def tamanhetkinenPaikka (get vierekkaisetSyotavatPaikat i))
        (println "Tamanhetkinen syova kivi" (get-in kentta tamanhetkinenPaikka))
        (println "Rekursiohirvio palautti taman: " (rekursiohirvio tamanhetkinenPaikka kentta []))
        (recur (+ i 1)) ;recur
        );when
      );loop
    (println "Ei loopattu, tahan ei voida asettaa kivea, syontiuhka tai kivi tiella")
    );if
  ;älä jatka
  )

;ennen asetusta tulee tyhjään paikkaan asettaa vuoronomistajan kivi, ja poistaa se asetustestin jälkeen
(def board2 [["W" "W" "W" "W" "W" "W" "W"]
             ["W" "R" "G" "G" "R" "E" "W"]
             ["W" "R" "R" "G" "R" "E" "W"]
             ["W" "E" "E" "E" "E" "E" "W"]
             ["W" "E" "E" "E" "E" "E" "W"]
             ["W" "E" "E" "E" "E" "E" "W"]
             ["W" "W" "W" "W" "W" "W" "W"]])
(def valinta [3 3])
;(println (voikoAsettaaJaSyoda valinta board2))
(println (aloitaPeli))
;(println (eikoVoiSyoda valinta board2))

(def kivi [1 1])
(def tarkistettavat [[1 1] [2 2]])
(def annetut [[1 0] [1 2] [0 1] [2 1]])
(if true
  (def annetut "taysin uusi arvonalustus")
  )
(println annetut)