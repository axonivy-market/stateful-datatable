# Stateful Datatable

Ein Utility-Dialogfeld zur Implementierung einer zustandsbehafteten Datentabelle
unter Verwendung von Ivy und PrimeFaces. Diese Datentabelle zeichnet sich
dadurch aus, dass sie ihren Zustand über verschiedene Interaktionen hinweg
beibehält. Aktionen wie

- Sortieren
- Filtern
- Paginierung

bleiben auch nach einem Neuladen der Seite oder dem Wechsel zu einer anderen
Ansicht erhalten. Dies ermöglicht eine nahtlose und effiziente Nutzung, da Sie
Ihre Einstellungen nicht jedes Mal neu konfigurieren müssen.

**Erleben Sie eine verbesserte Benutzerfreundlichkeit mit unserer
zustandsbehafteten Axon Ivy Datatable!**

## Demo

In dieser Demo listet eine Lazy-Datentabelle alle Produkte auf, die in der
Datenbank oder im Ivy-Business-Repo verfügbar sind.

![Liste der Produkte](DemoCapture.png "Liste der Produkte")

## Einrichtung

 - Bevor Sie beginnen, muss eine Datenbank eingerichtet und anschließend das
   Schema aus der Persistenz-Einheit generiert werden.
 - Starten Sie als Nächstes den Testprozess: `Erstellen Sie die
   Testdaten-Datenbank`, um die Testdaten für die Demo zu generieren.

 - Wenn Sie die stateful-datatable mit Ivy Business Repo starten müssen, müssen
   Sie die Datenbank nicht einrichten, sondern nur den Testprozess starten:
   `Erstellen Sie ein Testdaten-Repo`.

## So fügen Sie eine Spalte hinzu:

Der Ort, an dem jeder Schritt stattfindet, ist mit einem Kommentar versehen, z.
B. „ `“ (Hinzufügen von Spalten SCHRITT 1)`, „ `“ (Hinzufügen von Spalten
SCHRITT 2)` und so weiter.

1. SCHRITT 1 (Produkt):

Fügen Sie dem Produkt ein Feld mit dem gewünschten Typ hinzu, z. B. „Date
creationDate“ mit Getter und Setter und mit @Column annotiert.

2. SCHRITT 2 (StatefulDatatable.xhtml):

 - Kopieren Sie eine Spalte mit dem gewünschten Typ in die Datei
   „StatefulDatatable.xhtml” und fügen Sie sie ein.
 - Benennen Sie die Spalten-ID um, aber sie muss am Ende „ `“ und „Column` “
   enthalten, d. h. „ `“ und „creationDateColumn` “.
 - Ändern Sie sortBy und filterBy, um das Feld im Produkt anzusprechen, d. h.
   `#{product.creationDate}`.
 - Ändern Sie den Kopfzeilentext, um den gewünschten Text anzuzeigen. Ändern Sie
   den Wert des benutzerdefinierten Filters, d. h.
   `#{data.stateDataTableBean.lazyModel.filterText.get('creationDate')}`.
 - Ändern Sie die Ausgangs- und Eingabewerte, um das Feld im Produkt
   anzusprechen, d. h. `#{product.creationDate}`
 - Ändern Sie die exportierbaren und sichtbaren Werte, um die Spalte
   anzusprechen, d. h.
   `#{data.stateDataTableBean.lazyModel.columnVisibility.get('deliveryDateColumn')}`

3. SCHRITT 3 (ProductDatabaseLazyDataModel und/oder ProductRepoLazyDataModel):

 - All diese Änderungen finden entweder in ProductDatabaseLazyDataModel oder
   ProductRepoLazyDataModel statt, je nachdem, welchen Speicher Sie verwenden.
   Sie werden als LazyModel bezeichnet.
 - Fügen Sie den Filternamen als Konstante in LazyModel hinzu, d. h. `public
   static final String CREATION_DATE_FILTER = "creationDate";` Diese Konstante
   sollte denselben Namen wie das Feld haben.

4. SCHRITT 4 (ProductDatabaseLazyDataModel und/oder ProductRepoLazyDataModel):

 - Fügen Sie im LazyModel in der load-Methode einen Aufruf einer Methode zum
   Filtern basierend auf dem Filtertyp hinzu und ändern Sie den Parameter der
   Konstante in den von Ihnen hinzugefügten, d. h. für das Datum fügen Sie
   `addDateRangeQueryFilter(query, CREATION_DATE_FILTER, filters, false);` in
   ProductRepoLazyDataModel oder addDateRangeQueryFilter(predicates, cb, root,
   CREATION_DATE_FILTER, filters, false); in ProductDatabaseLazyDataModel

5. SCHRITT 5 (ProductDatabaseLazyDataModel und/oder ProductRepoLazyDataModel):

 - Fügen Sie im LazyModel in der Methode updateProduct ein else if mit Ihrer
   Konstante und Ihrem Setter zum Produkt hinzu, d. h.

    ```

        else if (field.contains(CREATION_DATE_FILTER)) {
            DateTime dateTime = (DateTime) newObjects.get(0);
            product.setCreationDate(dateTime.toJavaDate());
        }

    ```

 - Sie können hier auch eine Validierung hinzufügen.

6. SCHRITT 6 (StateDataTableBean):

 - Wenn Sie einen Dropdown-Filter verwenden, fügen Sie if else mit der
   geänderten Konstante und der von Ihnen verwendeten Enum-Klasse zum for-Zyklus
   in der Methode getFilterStateFromIUser() hinzu, d. h.

    ```

        else if(ProductRepoLazyDataModel.NEW_ENUM_FILTER.equals(filter.getKey())) {
            setDropdownFilterValue(filter, ((ArrayList<?>) filter.getValue()).size(), NewEnum.class);
        }

    ```

 - Wenn Sie einen Datumsfilter verwenden, fügen Sie die Konstante zum Aufruf der
   Methode setDateFilterValue zum for-Zyklus in der Methode
   getFilterStateFromIUser() hinzu, d. h.

    ```

        setDateFilterValue(filter, 
                ProductRepoLazyDataModel.VALID_THROUGH_FILTER,
                ProductRepoLazyDataModel.ORDER_DATE_FILTER,
                ProductRepoLazyDataModel.DELIVERY_DATE_FILTER,
                ProductRepoLazyDataModel.CREATION_DATE_FILTER);

    ```
