[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=code_smells)](https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=sqale_rating)](https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=security_rating)](https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=bugs)](https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=vulnerabilities)](https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=duplicated_lines_density)](https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=reliability_rating)](https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=alert_status)](https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=sqale_index)](https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_LOTR-FGen&metric=ncloc)](https://sonarcloud.io/summary/overall?id=Hummel009_LOTR-FGen)

LOTRFGen - аддон к моду "Властелин колец" для Minecraft, который выводит информацию о нём в виде парсеров и таблиц,
предназначенных для MediaWiki. Быстро портируется на сабмоды "Властелина колец". Также пригоден для отлова багов, вроде
неприсвоенных полей и непереведённых строк.

## Общая информация

Этот репозиторий - проект Gradle, который должен быть открыт через IntelliJ IDEA.

| Технология                             | Версия |
|----------------------------------------|--------|
| Система автоматической сборки Gradle   | 8.5    |
| Java, используемая для запуска Gradle  | 8+     |
| Java, используемая для сборки проекта  | 8      |
| Java, используемая для запуска проекта | 8      |

## Установка

Установка моих проектов Gradle и основы работы с ними примерно одинаковы, так что
смотрите [общую инструкцию](https://github.com/Hummel009/Legendary-Item#readme).

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
