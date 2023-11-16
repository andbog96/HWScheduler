FROM python:3.11

WORKDIR /app

COPY ./src/backend .

RUN pip install --no-cache-dir -r requirements.txt

EXPOSE 5000

ENTRYPOINT [ "python" ]

CMD ["main.py" ]