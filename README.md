# Планировщик домашек для студентов

***Проблема:*** нам приходится держать в голове все дз и их дедлайны

***Решение:*** сервис позволяющий преподавателям вести канал по своему предмету и выкладывать на нем дз с дедлайном и примерной оценкой времени на выполнение в часах.


Со стороны студента можно будет подписаться на каналы и видеть список дз которые нужно сдать. Список будет автоматически ранжироваться в зависимости от близости к дедлайну и сложности.

Студент может отмечать потраченное время на выполнение задания. У задачи будет выводиться среднее время потраченное студентами. Будет авторизация и регистрация аккаунта.

# How to Start

1. Create DataBase: `docker-compose up -d`
2. Launch Backend:
```
cd /src/backend
pip install --no-cache-dir -r requirements.txt
python main.py
```
3. Launch Frontend:
```
cd /src/frontend
npm run build
```