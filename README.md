Этот аддон выводит информацию о моде "Властелин колец" в виде парсеров и таблиц, предназначенных для MediaWiki. Также пригоден для отлова багов, вроде неприсвоенных полей и непереведённых строк.

Чтобы сгенерировать первую часть информации, войдите в игру, введите команду "/db xml" и затем кликните структурой под названием "Генератор баз данных" (последний в инвентаре креатива) по земле. Так вы получите файл xml.txt в директории игры, этот файл можно переименовать в file.xml и импортировать на MediaWiki. Будет сгенерировано несколько сотен статей на том языке, который был у вас в игре. 

Чтобы статьи заработали, вам нужно скопировать и отредактировать под себя 6 шаблонов MediaWiki:

1. Статья Биом (пример: gotminecraftmod.fandom.com/wiki/Template:Статья Биом)
2. Статья Фракция
3. Статья Моб
4. Статья Биом/Инфобокс (пример: gotminecraftmod.fandom.com/wiki/Template:Статья Биом/Инфобокс)
5. Статья Фракция/Инфобокс
6. Статья Моб/Инфобокс,

Как это сделано на gotminecraftmod.fandom.com.

Вторая часть информации - это "/db tables" и очередной клик структурой по земле. Так вы получите около 10 файлов в директории игры, их можно использовать как обычные таблицы в MediaWiki. 

Для загрузки изображений примените массовую загрузку ассетов мода.

В конечном итоге вы получите достаточно большой и хорошо заполненный сайт MediaWiki. Аддон можно приспособить, по сути, для любого сабмода LOTR.

Отлов ошибок производится следующим образом: можно открыть в блокноте каждый из сгенерированных файлов и искать там строки с фрагментами "name", "title", "desc", "info", "null" и т.д. Строки с упомянутыми фрагментами в 90% указывают на баг.

Для адаптации к сабмодам LOTR необходимо через JD GUI скопировать методы регистрации в файлах LOTREntities и LOTRStructures и вставить их в файл LFGConfig в практически неизменном виде, переименовав методы регистрации на методы получения информации. Затем может потребоваться обновление импортов во всех файлах и запуск.
