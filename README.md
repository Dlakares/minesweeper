# Описание
Этот проект содержит несколько архитектурных решений, которые я посчитал правильными.
1) [Redis-кэш](https://github.com/Dlakares/minesweeper/blob/master/src/main/java/ru/studiotg/minesweeper/cache/GameCache.java) является важной частью этого проекта. Я посчитал, что открытие определённой ячейки происходит часто, по этому, что бы ускорить обработку очередного хода вся необходимая информация об игре хранится в кэше. Сейчас там установлен TTL на 5 минут, после чего игры исчезают из него.
2) PostgreSQL даёт возможность хранить историю игр, так же, на ее стороне генерируется уникальный ключ для игры. Сущности внутри БД всего две - игра и поле связанные OneToOne связью.
3) [Сборщик](https://github.com/Dlakares/minesweeper/blob/master/src/main/java/ru/studiotg/minesweeper/schedule/GatherOldGames.java) решает проблему, когда из кэша игры уже пропали, а внутри бд об этом узнать невозможно. В зависимости от TTL редиса, в отдельном потоке собирает игры в которых нет отметки об конце, и отмечает их завершенными.
4) Сердце всего проекта - [сервис](https://github.com/Dlakares/minesweeper/blob/master/src/main/java/ru/studiotg/minesweeper/service/GameService.java), который и объединяет всю логику и формирует ответ пользователю.
5) [Async](https://github.com/Dlakares/minesweeper/blob/master/src/main/java/ru/studiotg/minesweeper/util/handler/CustomAsyncExceptionHandler.java) и [Global](https://github.com/Dlakares/minesweeper/blob/master/src/main/java/ru/studiotg/minesweeper/util/handler/GlobalExceptionHandler.java) exception handler. Нужны для правильной обработкт ошибок со всех мест приложения.
6) [FieldBuilder](https://github.com/Dlakares/minesweeper/blob/b2471d0e054586b69528b1f0da7d8c1014c99509/src/main/java/ru/studiotg/minesweeper/util/FieldBuilder.java), выделяет логику создания поля в отдельном классе.
7) [GameProccessor](https://github.com/Dlakares/minesweeper/blob/b2471d0e054586b69528b1f0da7d8c1014c99509/src/main/java/ru/studiotg/minesweeper/util/GameProcessor.java), если сервис это сердце, то это - мозг всего приложения. Именно тут содержиться основной алгоритм основаный на DFS-обходе и другие алгоритмы, которые и создают правильный ответ.
8) [JsonMapper](https://github.com/Dlakares/minesweeper/blob/b2471d0e054586b69528b1f0da7d8c1014c99509/src/main/java/ru/studiotg/minesweeper/util/JsonMapper.java) удобный (собственноручно созданый) маппер, который позволяет легко мапить любые JSON данные внутрь приложения. Используется для мапинга из редиса.
9) [Валидатор](https://github.com/Dlakares/minesweeper/blob/b2471d0e054586b69528b1f0da7d8c1014c99509/src/main/java/ru/studiotg/minesweeper/util/NewGameRequestValidator.java) - валидирует запросы на новую игру. Я допустил, что с фронта не может придти неверный ход, по этому других валидаторов нет.

## Приятные мелочи
Весь необходимый фунционал покрыт тестами, включая интеграционные, а так же все переменные зашиты в конфигурации. В самом репозитории настроен процесс [CI](https://github.com/Dlakares/minesweeper/blob/b2471d0e054586b69528b1f0da7d8c1014c99509/.github/workflows/gradle.yml), и каждый новый функионал был разработал в отдельных ветках. Так же, по при запущеном приложении на [localhost](http://localhost:8080/swagger-ui/index.html) находится OpenApi конфигурация.
