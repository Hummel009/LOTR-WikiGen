LOTRFGen - аддон к моду "Властелин колец" для Minecraft, который выводит информацию о нём в виде парсеров и таблиц, предназначенных для MediaWiki. Быстро портируется на сабмоды "Властелина колец". Также пригоден для отлова багов, вроде неприсвоенных полей и непереведённых строк.

## Общая информация

Этот репозиторий - проект Gradle, который должен быть открыт через IntelliJ IDEA. После установки среды проконтролируйте, чтобы версии Gradle JVM, Java и JDK соответствовали указанным ниже, иначе среда может установиться некорректно или не установиться вовсе.

| Технология | Версия  | Пояснение                                    | Где настроить                                                    |
|------------|---------|----------------------------------------------|------------------------------------------------------------------|
| Gradle     | 8.4-bin | Версия системы автоматической сборки         | -                                                                |
| Gradle JVM | 17.0.9  | Версия Java, используемая для запуска Gradle | File -> Settings -> Build -> Build Tools -> Gradle -> Gradle JVM |
| Java       | 8       | Language Level, используемый в проекте       | File -> Project Structure -> Project -> Language Level           |
| JDK        | 17.0.9  | Версия SDK, используемая в проекте           | File -> Project Structure -> Project -> SDK                      |

Если значения не соответствуют необходимым, необходимо перезагрузить проект Gradle. Ниже об этом будет написано подробнее.

## Установка и основы работы

Собственно, для начала нужно скачать и разархивировать в любое место на диске папку с исходниками. Будем называть это **папкой проекта**. В ней лежат папки (gradle, src) и различные файлы.

Эта версия использует RetroFuturaGradle - плагин, который применяет новые технологии для сборки старых версий. Таким образом, установка среды будет простой и автоматической.

Первым делом, запустите IntelliJ IDEA и откройте папку проекта: `File -> Open -> [Выбираете папку]`. Сразу после открытия начнётся установка среды. Если от вас потребуется разрешение на скачивание файлов, дайте его. Спустя некоторое время все необходимые файлы скачаются, и среда будет готова к работе.

Если на этом моменте что-то пошло не так, значит, самое время проверить значения, указанные в таблице из первого раздела. После изменения этих значений необходимо перезагрузить проект Gradle. Это можно сделать в **меню Gradle**, нажав на циклические стрелки. Меню можно открыть, нажав на значок слона в верхней правой части окна.

После установки среды весь необходимый инструментарий готов к работе. Сборочная машина Gradle вместе с плагином RetroFuturaGradle предлагает множество функций, которые находятся в ранее упомянутом меню Gradle. Вот самые важные из них:

* Запуск клиента из среды: `Меню Gradle -> Run Configurations -> 1. Run Client`.
* Запуск локального сервера из среды: `Меню Gradle -> Run Configurations -> 2. Run Server`. К нему можно подключиться из клиента, введя в качестве адреса `localhost`.
* Компиляция мода в файл с расширением .jar: `Меню Gradle -> Tasks -> build`. После компиляции ваш мод появится в папке `папка_проекта/build/libs`. Вас интересует тот файл, который без приписки -dev.jar.
