LOTRFGen - аддон к моду "Властелин колец" для Minecraft, который выводит информацию о нём в виде парсеров и таблиц, предназначенных для MediaWiki. Быстро портируется на сабмоды "Властелина колец". Также пригоден для отлова багов, вроде неприсвоенных полей и непереведённых строк.

## Общая информация

Этот репозиторий - проект Gradle, который должен быть открыт через IntelliJ IDEA или импортирован в Eclipse IDE.

| Технология | Версия  | Пояснение                                     |
|------------|---------|-----------------------------------------------|
| Gradle     | 8.4-bin | Версия системы автоматической сборки          |
| Gradle JVM | 17.0.9  | Версия Java, используемая для запуска Gradle  |
| JDK        | 17.0.9  | Версия Java, используемая для запуска проекта |
| Java       | 8       | Синтаксис Java, используемый в проекте        |

## Установка

Первым делом нужно скачать репозиторий и разархивировать его в любое место на диске. Если всё сделано правильно, вы должны увидеть папку, в которой находятся файлы `build.gradle.kts`, `settings.gradle.kts` и другие. Полученную папку будем называть ***папкой проекта***.

### IntelliJ IDEA

Запустите IntelliJ IDEA и откройте папку проекта: `File -> Open -> [Выбираете папку] -> OK`. Сразу после открытия начнётся установка среды. Если от вас потребуется разрешение на скачивание файлов, дайте его. Спустя некоторое время все необходимые файлы скачаются, и среда будет готова к работе.

Если на этом моменте что-то пошло не так и среда не установилась, значит, самое время проверить значения, указанные в таблице из первого раздела. Где их настроить:
* Gradle JVM: `File -> Settings -> Build -> Build Tools -> Gradle -> Gradle JVM`;
* JDK: `File -> Project Structure -> Project -> SDK`;
* Java: `File -> Project Structure -> Project -> Language Level`.

После изменения этих значений необходимо перезагрузить проект Gradle. Это можно сделать в ***меню Gradle***: `View -> Tool Windows -> Gradle`, нажав на значок перезагрузки в появившемся справа меню.

### Eclipse IDE

Запустите Eclipse IDE и импортируйте папку проекта: `File -> Import -> Gradle -> Existing Gradle Project -> Next -> [Выбираете папку] -> Finish`. Сразу после импорта начнётся установка среды. Если от вас потребуется разрешение на скачивание файлов, дайте его. Спустя некоторое время все необходимые файлы скачаются, и среда будет готова к работе.

Если на этом моменте что-то пошло не так и среда не установилась, значит, самое время проверить значения, указанные в таблице из первого раздела. Где их настроить:
* Gradle JVM: *переменные среды ОС, а именно JAVA_HOME и Path*;
* JDK: `Project -> Properties -> Java Build Path -> Libraries -> [нажимаете на JRE System Library] -> Remove -> Add Library -> JRE System Library -> Next -> Alternate JRE -> Installed JREs -> Add -> Standard VM -> Next -> [выбираете JRE home] -> Finish -> Apply and close -> [в выпадающем меню справа от Alternate JRE выбираете нужную JRE] -> Finish -> Apply and close`;
* Java: `Project -> Properties -> Java Compiler -> [галочка на Enable project specific settings] -> [выставляете Compiler compliance level] -> Apply and close`.

После изменения этих значений необходимо перезагрузить проект Gradle. Это можно сделать, нажав слева (под панелью Package Explorer) ПКМ по названию проекта и выбрав `Gradle -> Refresh Gradle Project`. После перезагрузки проекта в нижней части окна появится ***меню Gradle***. 

## Основы работы

После установки среды весь необходимый инструментарий готов к работе. Вот самые важные команды, необходимые каждому разработчику:

* Генерация исходного кода Minecraft: `Меню Gradle -> modded minecraft -> setupDecompWorkspace`.
* Запуск клиента из среды: `Меню Gradle -> modded minecraft -> runClient`.
* Запуск локального сервера из среды: `Меню Gradle -> modded minecraft -> runServer`. К нему можно подключиться из клиента, введя в качестве адреса `localhost`.
* Компиляция мода в файл с расширением .jar: `Меню Gradle -> build -> build`. После компиляции ваш мод появится в папке `папка_проекта/build/libs`. Вас интересует тот файл, который без приписки `-dev.jar`.

Примечания: 
* Команды запускаются двойным нажатием по ним.
* В IntelliJ IDEA все вышеуказанные команды содержатся в категории Gradle "Tools".

## Использование

### Генерация сайта MediaWiki

Чтобы сгенерировать первую и основную часть информации, войдите в игру и введите команду "/db xml". Так вы получите файл \*.xml в директории игры (.minecraft/hummel/\*.xml). Этот файл можно импортировать на MediaWiki. Пример хорошего бесплатного хостинга MediaWiki - Fandom. В итоге будет сгенерировано несколько сотен статей на том языке, который был у вас в игре.

Чтобы статьи заработали и начали отображать информацию, вам нужно скопировать и отредактировать под себя 6 шаблонов MediaWiki:

* [Статья Биом](https://gotminecraftmod.fandom.com/wiki/Template:Статья_Биом)
* [Статья Фракция](https://gotminecraftmod.fandom.com/wiki/Template:Статья_Фракция)
* [Статья Моб](https://gotminecraftmod.fandom.com/wiki/Template:Статья_Моб)
* [Статья Дерево](https://gotminecraftmod.fandom.com/wiki/Template:Статья_Дерево)
* [Статья Ископаемое](https://gotminecraftmod.fandom.com/wiki/Template:Статья_Ископаемое)
* [Статья Структура](https://gotminecraftmod.fandom.com/wiki/Template:Статья_Структура)

Не забывайте также и о подшаблонах, которые будут гореть красным цветом. Если у вас возникли вопросы, можете задавать их опытным участникам Википедии - они умеют обращаться с шаблонами.

### Дополнение сайта MediaWiki

Вторая часть информации - это "/db tables". Так вы получите около 10 файлов в директории игры (.minecraft/hummel/\*.txt), их можно использовать как код обычных таблиц в MediaWiki и размещать там, где посчитаете нужным. Главное - не забыть сделать заголовок и завершение таблицы.

Для загрузки изображений примените массовую загрузку ассетов мода.

В конечном итоге вы получите достаточно большой и хорошо заполненный сайт MediaWiki. Аддон можно приспособить, по сути, для любого сабмода LOTR.

### Полезные побочные эффекты

Перед тем, как у вас сгенерируется информация, вы получите тонну ошибок, которые указывают на то, что ваш код не идеален. Отлов ошибок производится следующим образом: можно открыть в блокноте каждый из сгенерированных файлов и искать там строки с фрагментами "name", "title", "desc", "info", "null" и т.д. Строки с упомянутыми фрагментами в 90% указывают на баг в коде или отсутствие перевода.

Если же ваши файлы баз данных вовсе не создались, то, возможно, у вас в коде мода кроется NullPointerException. Самое время пройтись по папке .minecraft/logs и посмотреть, какие ошибки выдала команда базы данных.

Для адаптации к сабмодам LOTR необходимо через JD GUI скопировать методы регистрации в файлах LOTREntities и LOTRStructures и вставить их в файл LFGConfig в практически неизменном виде, переименовав методы регистрации на методы получения информации. Затем может потребоваться обновление импортов во всех файлах и запуск.
