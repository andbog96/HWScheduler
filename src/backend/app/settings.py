import psycopg2


conn = psycopg2.connect(
    dbname="postgres",
    user="postgres",
    password="1234",
    host="localhost"
)

cur = conn.cursor()

SECRET_KEY = "sdghkghgkjshdfkjghsdkghsdlkghdkghsdkghdjkghsdlghsdghjdlkghdfh"

alg = 'HS256'