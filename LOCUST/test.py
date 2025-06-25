from locust import HttpUser, task, between
import random

class PublicacionesUser(HttpUser):
    host = "http://localhost:8086"  # ✅ Puerto correcto de ms-publicaciones
    wait_time = between(1, 3)

    @task
    def crear_libro(self):
        titulos = ["Microservicios", "Spring Boot Avanzado", "RabbitMQ para Todos"]
        editoriales = ["OpenAI Press", "ESPE Editorial", "TechBooks"]

        payload = {
            "titulo": random.choice(titulos),
            "editorial": random.choice(editoriales),
            "anioPublicacion": random.randint(2000, 2025),
            "resumen": "Libro generado por Locust",
            "isbn": f"{random.randint(1000000000000, 9999999999999)}",
            "numeroPaginas": random.randint(100, 500),
            "autorId": 1  # ✅ Necesario para que el POST funcione
        }

        self.client.post("/api/libros", json=payload)  # ✅ Endpoint correcto
