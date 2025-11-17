# Stateful Datatable

Ein Nutzen Zwiegespräch für implementieren ein stateful Datatable benutzend Efeu
und PrimeFaces. Dies Datatable Stände aus weil hält fest es seinen Staat über
verschiedene Interaktionen. Aktionen #mögen

- Ordnen
- Filtern
- Seitennummerierung

Verharr sogar intakt nach einer Seite reload oder schalten zu einem
verschiedenen Ausblick. Dies erlaubt für nahtlos und leistungsstarke Nutzung, da
du willst nicht brauchen zu reconfigure eure Lagen jede Zeit.

**#Erfahren verbessert #Brauchbarkeit mit unser stateful #Axon Efeu Datatable!**

## Demo

In dieser Demo, ein faules datatable ist listen alle die Produkte verfügbar in
die #Daten Basis oder in das Efeu Geschäft repo.

![Liste von Produkte](DemoCapture.png "Liste von Produkte")

## Einrichtung

 - Vor starten, eine #Daten Basis Notwendigkeiten zu sein Einrichtung und dann
   generieren das Schema von die Beharrlichkeit Einheit.
 - #Nächste, starte den Klausur Arbeitsgang: `Schafft Klausur #Daten Datenbank`
   zu generieren #der Klausur #Daten für die Demo.

 - Ob du brauchst zu starten das stateful-datatable mit Efeu Geschäft repo,
   keine Notwendigkeit zu Einrichtung die #Daten Basis aber du einzige
   Notwendigkeit zu starten den Klausur Arbeitsgang: `Schafft Klausur #Daten
   Repo`.

## Wie zu zufügen ein colum:

Der Ort jede Stufe findet statt hat einen Kommentar mag `#Zufügen Spalten STUFE
1`, `#Zufügen Spalten STUFE 2` #und so weiter.

1. SCHREITE 1 (Produkt):

Füg zu ein Feld mit gewünscht Typ zu dem Produkt, #d.h. Datiert creationDate mit
getter und #Setter und annotiert mit @Spalte.

2. STUFE 2 (StatefulDatatable.xhtml):

 - Kopier eine Spalte mit die gewünscht #eintippen die StatefulDatatable.xhtml
   Und #bekleben ihm.
 - Benenn um die Spalte id, aber es muss haben `Spalte` am Ende, #d.h.
   `creationDateColumn`.
 - Wechsel sortBy und filterBy zu anrichten das Feld in dem Produkt, #d.h.
   `#{Produkt.creationDate}`.
 - Änderung Kopfball Text zu zeigen den Text du willst. Wechsel den Wert von dem
   #benutzerdefiniert Filter, #d.h.
   `#{#Daten.stateDataTableBean.lazyModel.filterText.Bekomm('creationDates)}`.
 - Änderung Ausgabe und Input Werte zu anrichten das Feld in dem Produkt, #d.h.
   `#{Produkt.creationDate}`
 - Wechsel exportable und sichtbare Werte zu anrichten die Spalte, #d.h.
   `#{#Daten.stateDataTableBean.lazyModel.columnVisibility.Bekomm('deliveryDateColumns)}`

3. STUFE 3 (ProductDatabaseLazyDataModel und/oder ProductRepoLazyDataModel):

 - Jede diese Änderungen passieren in #jeder ProductDatabaseLazyDataModel oder
   ProductRepoLazyDataModel gegründet auf was Speicher benutzt du. Sie wollen
   sein refered da LazyModel
 - Füg zu Filter Namen da ein stetiges herein LazyModel, #d.h. `öffentliche
   Statik endgültige Schnur KREATION_DATUM_FILTERT = "creationDate";` Diese
   Konstante sollte gleichen Namen da das Feld haben.

4. STUFE 4 (ProductDatabaseLazyDataModel und/oder ProductRepoLazyDataModel):

 - Herein das LazyModel, in Ladung Methode fügt zu Anruf zu einer Methode für
   filtern gegründet auf dem Typ von filtern und wechseln den Parameter von das
   stetiges zu #derjenige du fügtest zu, #d.h. für datieren #zufügen
   `addDateRangeQueryFilter(Anfrage, KREATION_DATUM_FILTER, Filter, falsch);`
   Herein ProductRepoLazyDataModel oder addDateRangeQueryFilter(Satzaussagen,
   cb, Wurz, KREATION_DATUM_FILTER, Filter, falsch); Herein
   ProductDatabaseLazyDataModel

5. STUFE 5 (ProductDatabaseLazyDataModel und/oder ProductRepoLazyDataModel):

 - Herein das LazyModel, herein das updateProduct Methode, füg zu sonst ob mit
   eure stetiges und #Setter zu dem Produkt, ie.

    ```

        else if (field.contains(CREATION_DATE_FILTER)) {
            DateTime dateTime = (DateTime) newObjects.get(0);
            product.setCreationDate(dateTime.toJavaDate());
        }

    ```

 - Du kannst auch irgendwelche Bestätigung zufügen hier

6. STUFE 6 (StateDataTableBean):

 - Ob benutzend ein dropdown Filter, füg zu ob sonst mit #abgeändert Konstante
   und enum #eingruppieren dir bist benutzen zu die für Rad fährst herein
   getFilterStateFromIUser() Methode, #d.h.

    ```

        else if(ProductRepoLazyDataModel.NEW_ENUM_FILTER.equals(filter.getKey())) {
            setDropdownFilterValue(filter, ((ArrayList<?>) filter.getValue()).size(), NewEnum.class);
        }

    ```

 - Ob usign ein Datum Filter, füg zu das Stetiges zu der setDateFilterValue
   Methode Anruf zu den für Rad fährt herein getFilterStateFromIUser() Methode,
   #d.h.

    ```

        setDateFilterValue(filter, 
                ProductRepoLazyDataModel.VALID_THROUGH_FILTER,
                ProductRepoLazyDataModel.ORDER_DATE_FILTER,
                ProductRepoLazyDataModel.DELIVERY_DATE_FILTER,
                ProductRepoLazyDataModel.CREATION_DATE_FILTER);

    ```
