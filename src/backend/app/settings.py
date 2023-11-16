import psycopg2

def init_base():
    return psycopg2.connect(
        dbname="postgres",
        user="postgres",
        password="1234",
        host="localhost",
        port=5432
    )


SECRET_KEY = "sdghkghgkjshdfkjghsdkghsdlkghdkghsdkghdjkghsdlghsdghjdlkghdfh"

alg = 'HS256'