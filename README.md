# Introducció

La pràctica es desenvoluparà en grups de tres persones. L'objectiu és crear una aplicació funcional que compleixi amb un conjunt de requisits tècnics, els quals s'indiquen a continuació. Es valora especialment la capacitat de cada grup per estructurar i dissenyar el projecte de forma pròpia, sempre que es compleixin els mínims especificats.

L'avaluació de la pràctica es farà en base a:

- Vídeo de presentació
- Documentació en markdown
- Release del codi

Aquesta pràctica afecta als següents RAs:

- 0485: Programació (RA5, RA8, RA9)
- 0486: Entorns de desenvolupament (RA3)
- 0487: Sostenibilitat aplicada al sistema productiu (tots els RAs)

---

## Explicació prèvia

En el marc del mòdul de Sostenibilitat aplicada al desenvolupament tecnològic, aquesta pràctica proposa el disseny i la creació d'una API REST orientada a mesurar i analitzar l'impacte ambiental de l'ús d'eines d'intel·ligència artificial.

L'ús de la IA té un cost energètic i mediambiental real i sovint invisible: entrenar un model com GPT-4 va generar al voltant de 500 tones de CO₂, i una sola consulta de text pot consumir entre 5 i 10 vegades més energia que una cerca convencional a Google. Mitjançant aquesta eina, anomenada **AImpact**, es pretén facilitar el registre i la gestió de sessions d'ús d'IA, així com el càlcul estimat de les emissions de CO₂ associades.

Aquest projecte no només integra valors ambientals sinó que posa en pràctica coneixements tècnics essencials com la programació orientada a objectes, l'arquitectura per capes amb Spring Boot, la persistència de dades, el disseny d'APIs REST i l'ús d'eines professionals de desenvolupament.

L'activitat forma part d'un enfocament global que pretén potenciar el pensament crític i la responsabilitat en la creació de programari, amb especial atenció als criteris ambientals i de qualitat del codi.

---

## Com calcular el consum de CO₂ de les IAs

L'aplicació inclourà una taula interna o fitxer de configuració amb valors aproximats per model i tipus de tasca. Alguns exemples orientatius:

| Model / Eina                 | Tipus de tasca      | CO₂ per consulta (g) | Consum (Wh)       |
| ---------------------------- | ------------------- | --------------------- | ----------------- |
| GPT-4 (text)                 | Generació de text  | 3.0 – 10.0           | 0.001 – 0.01 kWh |
| GPT-3.5 (text)               | Generació de text  | 0.3 – 1.0            | ~0.001 kWh        |
| Stable Diffusion (imatge)    | Generació d'imatge | 2.5 – 15.0           | 0.005 – 0.02 kWh |
| GitHub Copilot (suggeriment) | Assistència codi   | 0.05 – 0.1           | ~0.0001 kWh       |
| Cerca Google                 | Cerca web           | ~0.1                  | ~0.0003 kWh       |

> Els valors poden variar en funció del proveïdor de núvol, la regió geogràfica i la mida del model. L'aplicació ha d'usar una taula de valors per defecte, però es valorarà positivament permetre-ne la configuració.

---

## Funcionalitats obligatòries

L'aplicació haurà de complir, com a mínim, amb les següents funcionalitats:

- Registre de sessions d'ús d'IA amb els camps: usuari, eina (GPT-4, Copilot...), tipus de tasca (text, imatge, codi...), nombre de consultes, data i descripció breu.
- Càlcul automàtic del CO₂ estimat i consum en kWh per sessió, a partir d'una taula de valors interna.
- API REST completa amb Spring Boot que exposi, com a mínim, els endpoints següents:
    - `POST /sessions` — registrar una nova sessió
    - `GET /sessions` — llistar totes les sessions
    - `GET /sessions/{id}` — obtenir una sessió concreta
    - `DELETE /sessions/{id}` — esborrar una sessió
    - `GET /sessions/report` — resum total de CO₂ i kWh acumulats per usuari
- Ús d'una base de dades MySQL per emmagatzemar totes les sessions (Spring Data JPA).
- Exportació de les sessions a un fitxer extern (formats acceptats: CSV o TXT) accessible via endpoint REST (`GET /sessions/export`).
- Proves unitàries amb JUnit i ús de Mockito per simular la capa de repositori.
- Disseny d'un Master Test plan testejant les diverses funcionalitats de l'aplicació (com a mínim seguint el model d'excel de classe)
- Una funcionalitat oberta i creativa dissenyada pel grup, que aporti valor afegit a l'aplicació (ex: comparador d'equivalències ecològiques, rànquing de models per eficiència, informe de tendències setmanals, sistema d'alertes per llindar...).

---

## Arquitectura esperada

L'aplicació ha de seguir una arquitectura per capes pròpia de Spring Boot:

```
src/
└── main/
    └── java/
        └── com/aiimpact/
            ├── controller/    ← @RestController: rep les peticions HTTP
            ├── service/       ← @Service: lògica de negoci i càlculs
            ├── repository/    ← @Repository / JpaRepository: accés a MySQL
            ├── model/         ← @Entity: classes de domini (Sessió, Model...)
            └── dto/           ← Objectes de transferència de dades
```

Es valorarà el **principi de responsabilitat única**: cada capa ha de fer únicament allò que li pertoca.

---

## El projecte amb Github

Tots els grups hauran de desenvolupar la pràctica utilitzant un repositori de GitHub creat específicament per a aquest projecte. L'ús incorrecte o inexistent de Git (commits desordenats, poc freqüents, missatges no significatius o treball no repartit) serà penalitzat.

A més, es premiarà positivament l'ús de GitHub Projects per gestionar les tasques, assignar responsables i fer seguiment de les issues, sempre que es reflecteixi una planificació coherent i una col·laboració efectiva.

---

## Lliurament i avaluació

Seguidament s'exposa com s'avaluarà cadascún dels entregables:

### Codi

| Criteri                                                                            | Pes  |
| ---------------------------------------------------------------------------------- | ---- |
| Disseny orientat a objectes (POO: classes, herència, encapsulament, polimorfisme) | 20%  |
| Arquitectura per capes Spring Boot (controller, service, repository, dto)          | 10%  |
| API REST funcional (endpoints, respostes HTTP correctes)                           | 10%  |
| Connexió i operacions amb MySQL (Spring Data JPA)                                 | 15%  |
| Gestió d'excepcions i try-catch (errors controlats, respostes HTTP adequades)     | 10%  |
| Exportació a fitxers (CSV/TXT via endpoint REST)                                  | 10%  |
| Càlcul i representació del CO₂ i consum energètic                              | 10%  |
| Proves (Master Test Plan + JUnit/Mockito)                                          | 10%  |
| Part oberta dissenyada per l'alumne/a                                              | 5%   |
| Penalització per ús incorrecte de POO, capes o Git                               | -15% |
| Extra per Github Projects                                                          | +5%  |

### Vídeo de presentació

Vídeo enregistrat pel grup, amb suport visual (presentació, screencasts de l'aplicació, etc.). El vídeo ha de seguir l'estructura i els timings indicats a continuació.

**1. Introducció** *(30s - 1min)*

- Breu descripció del projecte: què és AImpact?
- Objectiu principal: visibilitzar el cost ambiental de les IAs.
- Nom del grup, components, rol de cada membre (opcional però recomanable).

**2. Funcionalitat pròpia i creativa** *(1-2 min)*

Aquest és el bloc més important per diferenciar-se dels altres grups.

- Quina funcionalitat extra heu afegit? *(Exemples: comparador d'equivalències ecològiques, rànquing de models, alertes per llindar, tendències setmanals, integració amb API externa d'intensitat de carboni de la xarxa elèctrica...)*
- Per què vau triar aquesta funcionalitat?
- Com s'ha implementat (visió tècnica i screencast de demostració amb Postman o similar)?

**3. Decisions tècniques pròpies** *(1 min)*

Centrat en les diferències de disseny o arquitectura de codi.

- Com heu estructurat les capes de l'aplicació?
- Com heu modelat les entitats i els DTOs?
- Com heu implementat el càlcul de CO₂? Heu innovat en aquest punt?

**4. Testing** *(1 min)*

Mostra de maduresa tècnica.

- Quines parts heu testejat?
- Heu fet servir Mockito? Per mockejar quina capa (repositori, servei...)?
- Cobertura de codi aproximada (si disponible).
- Alguna prova automatitzada interessant o complexa que vulgueu mostrar.
- Breu comentari sobre el Master Test Plan

**5. Treball en equip i GitHub** *(1 min)*

Valoració del procés.

- Com us heu organitzat com a equip?
- Heu utilitzat GitHub Projects o issues?
- Com ha funcionat el flux de treball i les revisions de codi?

**6. Valoració personal i aprenentatges** *(opcional, 30s)*

- Dificultats trobades i com les heu superat.
- Què heu après més important durant la pràctica.

**7. Demo de l'aplicació** *(5min)*

Screencast enregistrat de l'aplicació en funcionament (Postman, terminal o interfície si n'hi ha).

**ALERTA! El vídeo rebrà una penalització si no s'ajusten els timings**

---

### Documentació

Documentació ubicada en el Readme (fet en Markdown) dins del repositori.

**1. Document tècnic del projecte**

Inclourà:

- **Descripció general del projecte**
    - Breu explicació de què fa l'aplicació i amb quina finalitat.
    - Descripció de la funcionalitat oberta afegida pel grup.
- **Estructura del projecte**
    - Descripció de les capes (controller, service, repository, model, dto).
    - Relació entre les classes més importants.
    - Diagrama de classes (opcional): ha d'incloure com a mínim les classes de domini (Sessio, ModelIA, Usuari...).
- **Documentació dels endpoints REST**
    - Taula o col·lecció Postman amb tots els endpoints: mètode HTTP, ruta, descripció, exemple de request i response.
- **Esquema de la base de dades**
    - Diagrama E-R, o descripció de les taules amb camps, tipus i relacions (opcional).
    - Script SQL o esquema generat per JPA (adjunt o dins del repositori).
- **Descripció tècnica del càlcul de CO₂**
    - Com es calcula el valor per sessió.
    - Font dels valors de referència (taula interna, fitxer, fórmula...).

**2. Manual d'usuari / Manual d'integració**

- Instruccions d'instal·lació i execució (configuració de MySQL, `application.properties`, dependències Maven...).
- Exemples de crides a l'API amb `curl` o Postman.
- Explicació de com interpretar els resultats de l'endpoint de report i d'exportació.

**3. Pla de proves (testing)**

- Explicació breu de l'enfocament del Master Test Plan (unitari, integració...).
- Llistat de proves fetes:
    - Què es prova.
    - Mètodes o classes testejades.
    - Resultats esperats.
- Referències a les proves amb JUnit i Mockito (fitxers inclosos al projecte).
- Enllaç a l'excel del Master Test Plan.

**4. Ús de la intel·ligència artificial**

Reflexió honesta i detallada sobre com el grup ha fet servir eines d'IA durant el desenvolupament del projecte. Ha d'incloure:

- Quines eines d'IA s'han utilitzat (ChatGPT, GitHub Copilot, Claude, etc.).
- En quines parts concretes del projecte s'han aplicat (generació de codi, depuració, documentació, tests, disseny...).
- Com s'ha fet servir: quins tipus de prompts o consultes s'han fet, i com s'han integrat o adaptat els resultats obtinguts.
- Valoració crítica: què ha funcionat bé, on ha calgut corregir la IA i quines parts s'han fet íntegrament sense assistència.
