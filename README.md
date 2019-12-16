x# trumplator

A simple mash-up of APIs that allows you to translate Trump-tweets to Yodaspeak. Trust us, it's funny.

## Installera NPM

Eftersom det krävs typ en miljon filer för att ha möjligheten att installera
alla dependencies i NPM så ignoreras dessa i Git. Det krävs därför lite pill för att få igång front-end "servern".

## Installera node.js och ladda ner rätt filer

1. Gå till https://nodejs.org/en/
2. Ladda ner och installera.
3. För att kontrollera att installeringen skedde korrekt, öppna en terminal och skriv:
   node -v
   Om installationen fungerat så lär ni få upp den nuvarande versionen av node.js.
4. För att kontrollera att ni fått med alla dependencies, öppna en terminal och skriv:
   npm -v
   Om installationen fungerat så lär ni få upp den nuvarande versionen av npm.
5. I terminalen i relevant kodmiljö, gå till rätt mapp med
   cd my-app
6. I terminalen, skriv
   npm ci

- (Man kan även skriva
  npm install
  , men det finns en möjlighet att package.lock-filen (som innehåller alla dependencies) uppdateras vilket kan mörda allt.)

7. Vänta på att nedladdningen är färdig.

## Köra front-end servern

1. Splitta terminalen i er kodmiljö. Detta så att ni kan köra back-end servern och front-end servern samtidigt utan klydd.
2. I terminalen, skriv
   npm start

## Få igång git lokalt på datorn

1. Eftersom denna startversion kommer laddas upp på GIT med relevanta GIT-vägar, så är det rätt bra att komma ihåg att man även behöver GIT lokalt.
   Detta gör ni genom att skriva
   git init
2. Kontrollera även att ni har en .gitignore-fil i er mapp. Denna borde ha kommit med installationen av npm, men om så inte är fallet så hmu så fixar vi den tillsammans.

## JavaScript plugins must-haves för kodnördar

1. Gå till plugins i valfri kodmiljö (INTE IDLE)
2. Sök efter
   ESLint
   och ladda ner den med flest nedladdningar.
3. a) Om du har VS-code, sök efter:
   prettier vs-code
   b) Om du har en annan kodmiljö, sök efter:
   js prettifier

Thank and goodbye

## VIKTIGT LÄS

1. I mappen "public" finns en index.html fil. I denna, rör INTE elementet <root>
2. Observera att vissa av filerna som ligger i vår FrontEnd kommer att tas bort efterhand (t.ex testmiljön och alla extra CSS filer). Kom ihåg att ändringar i t.ex CSS kanske kräver uppdatering av app.js.
3. Rör INTE filerna
   package.json
   package-lock.json
   ServiceWorker.js
   setupTests.js
   Att röra dessa filerna är lika med säker död för vår applikation. Alla andra filer är fritt fram, men kom ihåg att använda GIT om ni ska pilla i REACT (app.js).
   Ett undantag är om ni vill scripta något, t.ex att starta backend-servern eller andra kommandon som körs i terminalen. Detta görs i package.json (alltså INTE i package-lock.json) under nyckeln "scripts". Kommandon körs sedan med
   npm <scriptname>
