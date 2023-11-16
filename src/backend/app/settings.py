import psycopg2


conn = psycopg2.connect(
    dbname="postgres",
    user="postgres",
    password="1234",
    host="localhost",
    port=7777
)

cur = conn.cursor()

SECRET_KEY = "sdghkghgkjshdfkjghsdkghsdlkghdkghsdkghdjkghsdlghsdghjdlkghdfh"

alg = 'HS256'