# databases


Вопросы:
- длинные транзакции в перемешку запросы в сеть, расчеты?
- миграции
- где границы ООП, круды создавались. Сюда же большие выборки
- как сделать снапшет в постгрессе для проверки миграции? А размер?
- паджинация, как делать?
- удалять очень неудобно, можно ли каскадно?
- как видятся изменения? в одной транзакии(refresh)? между тредами/процессами?
- как глубоком можно сделать join в sqlalh.?
- разбить по строкам лучшее. А сетка? или положит ьсписок в ячейку?
- можно ли работать без PK?
- datetime -> date?
- как эффективно хранить временные ряды?
- что такое индексы?
- postgras мжет не возвращать
- created_at
- как смотреть запросы в постгресс? задержки, какие запросы?
- TASK - попробовать репликацию


# Course
Aggrigation -> SUm/max//
возвращается таблица
uniqueness and keys
joining tables
```
select
  animals.name,
  animals.species,
  diet.food
  from animals join diet
    on animals.species = diet.spices;
    where food = 'fish'
```
разбиение таблицы на объекты
date/text/integer

```
#
# Uncomment one of these QUERY variables at a time and use "Test Run" to run it.
# You'll see the results below.  Then try your own queries as well!
#

# QUERY = "select max(name) from animals;"

# QUERY = "select * from animals limit 10;"

# QUERY = "select * from animals where species = 'orangutan' order by birthdate;"

# QUERY = "select name from animals where species = 'orangutan' order by birthdate desc;"

# QUERY = "select name, birthdate from animals order by name limit 10 offset 20;"

# QUERY = "select species, min(birthdate) from animals group by species;"

# QUERY = '''
# select name, count(*) as num from animals
# group by name
# order by num desc
# limit 5;
# '''


```

limit count [offset skip]

# "Топ ошибок со стороны разработки при работе с PostgreSQL / Алексей Лесовский (Data Egret)"
- pg_log не удалть
- pg базовый конфиг под слабые машины
- "Мониторинг и планирование"
- на SSD
- планируйте схему данных
- партиционирование - проще удалять будет
- "Масштабирование"
- OLTP/OLAP - короткие и длинные, как разнести?
- реплики, логическа публикация подписки
- внешние таблицы, декларативная партиционирование
- ребалансировка?
- лаг репликации
- "Приложения и транзакции"
- idle transactions? долгие транзакции источник мусорных данных
- избегать долгих танзакций
- "Велосипедосттроение"
- "Автоматизация"
- сплитбрейн, файловеры
- Patroni, ансибл плохо
- "Контенеы и оркестрация"
- работает пока

# "Поиск проблем в базе данных, если ты разработчик / Алексей Лесовский (Coins.ph)"
- отказы запросов на репликах