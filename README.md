> [!CAUTION]
> С 29.02.2024 разработка проекта завершена. Инструментарий будет поддерживаться в актуальном и рабочем состоянии, но
> правки в основной код вноситься не будут.

[![Code Smells][code_smells_badge]][code_smells_link]
[![Maintainability Rating][maintainability_rating_badge]][maintainability_rating_link]
[![Security Rating][security_rating_badge]][security_rating_link]
[![Bugs][bugs_badge]][bugs_link]
[![Vulnerabilities][vulnerabilities_badge]][vulnerabilities_link]
[![Duplicated Lines (%)][duplicated_lines_density_badge]][duplicated_lines_density_link]
[![Reliability Rating][reliability_rating_badge]][reliability_rating_link]
[![Quality Gate Status][quality_gate_status_badge]][quality_gate_status_link]
[![Technical Debt][technical_debt_badge]][technical_debt_link]
[![Lines of Code][lines_of_code_badge]][lines_of_code_link]

LOTRFGen - аддон к моду "Властелин колец" для Minecraft, который выводит информацию о нём в виде парсеров и таблиц,
предназначенных для MediaWiki. Быстро портируется на сабмоды "Властелина колец". Также пригоден для отлова багов, вроде
неприсвоенных полей и непереведённых строк.

## Использование

### Генерация сайта MediaWiki

Чтобы сгенерировать первую и основную часть информации, войдите в игру и введите команду "/db xml". Так вы получите файл
\*.xml в директории игры (.minecraft/hummel/\*.xml). Этот файл можно импортировать на MediaWiki. Пример хорошего
бесплатного хостинга MediaWiki - Fandom. В итоге будет сгенерировано несколько сотен статей на том языке, который был у
вас в игре.

Чтобы статьи заработали и начали отображать информацию, вам нужно скопировать и отредактировать под себя 6 шаблонов
MediaWiki:

* [Статья Биом](https://gotminecraftmod.fandom.com/wiki/Template:Статья_Биом)
* [Статья Фракция](https://gotminecraftmod.fandom.com/wiki/Template:Статья_Фракция)
* [Статья Моб](https://gotminecraftmod.fandom.com/wiki/Template:Статья_Моб)
* [Статья Дерево](https://gotminecraftmod.fandom.com/wiki/Template:Статья_Дерево)
* [Статья Ископаемое](https://gotminecraftmod.fandom.com/wiki/Template:Статья_Ископаемое)
* [Статья Структура](https://gotminecraftmod.fandom.com/wiki/Template:Статья_Структура)

Не забывайте также и о подшаблонах, которые будут гореть красным цветом. Если у вас возникли вопросы, можете задавать их
опытным участникам Википедии - они умеют обращаться с шаблонами.

### Дополнение сайта MediaWiki

Вторая часть информации - это "/db tables". Так вы получите около 10 файлов в директории игры (
.minecraft/hummel/\*.txt), их можно использовать как код обычных таблиц в MediaWiki и размещать там, где посчитаете
нужным. Главное - не забыть сделать заголовок и завершение таблицы.

Для загрузки изображений примените массовую загрузку ассетов мода.

В конечном итоге вы получите достаточно большой и хорошо заполненный сайт MediaWiki. Аддон можно приспособить, по сути,
для любого сабмода LOTR.

### Полезные побочные эффекты

Перед тем, как у вас сгенерируется информация, вы получите тонну ошибок, которые указывают на то, что ваш код не
идеален. Отлов ошибок производится следующим образом: можно открыть в блокноте каждый из сгенерированных файлов и искать
там строки с фрагментами "name", "title", "desc", "info", "null" и т.д. Строки с упомянутыми фрагментами в 90% указывают
на баг в коде или отсутствие перевода.

Если же ваши файлы баз данных вовсе не создались, то, возможно, у вас в коде мода кроется NullPointerException. Самое
время пройтись по папке .minecraft/logs и посмотреть, какие ошибки выдала команда базы данных.

Для адаптации к сабмодам LOTR необходимо через JD GUI скопировать методы регистрации в файлах LOTREntities и
LOTRStructures и вставить их в файл LFGConfig в практически неизменном виде, переименовав методы регистрации на методы
получения информации. Затем может потребоваться обновление импортов во всех файлах и запуск.

<!----------------------------------------------------------------------------->

[code_smells_badge]: https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=code_smells
[code_smells_link]: https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen
[maintainability_rating_badge]: https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=sqale_rating
[maintainability_rating_link]: https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen
[security_rating_badge]: https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=security_rating
[security_rating_link]: https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen
[bugs_badge]: https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=bugs
[bugs_link]: https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen
[vulnerabilities_badge]: https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=vulnerabilities
[vulnerabilities_link]: https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen
[duplicated_lines_density_badge]: https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=duplicated_lines_density
[duplicated_lines_density_link]: https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen
[reliability_rating_badge]: https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=reliability_rating
[reliability_rating_link]: https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen
[quality_gate_status_badge]: https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=alert_status
[quality_gate_status_link]: https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen
[technical_debt_badge]: https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=sqale_index
[technical_debt_link]: https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen
[lines_of_code_badge]: https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=ncloc
[lines_of_code_link]: https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen
