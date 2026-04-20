# Lab4 Docker Deployment

Это Spring Boot приложение настроено для запуска в Docker с использованием PostgreSQL и MongoDB.

## Что было добавлено

### 1. **Зависимость Actuator** (`build.gradle`)
   - Добавлена `spring-boot-starter-actuator` дляHealth Check
   - Endpoint: `/actuator/health`

### 2. **Docker файлы**
   - **Dockerfile** - Multi-stage build для оптимизации размера образа
   - **docker-compose.yml** - Орхестрация всех сервисов (App, PostgreSQL, MongoDB)
   - **.dockerignore** - Оптимизация Docker build

### 3. **Конфигурация приложения**
   - **application-docker.properties** - Docker-специфичная конфигурация с переменными окружения
   - Поддержка всех существующих возможностей без изменений основного файла

### 4. **Health Checks**
   - Docker healthcheck в Dockerfile
   - Spring Actuator `/actuator/health` endpoint
   - Healthchecks для PostgreSQL и MongoDB в compose файле

---

## Как запустить

### Вариант 1: Полный Docker Compose

```bash
docker-compose up --build
```

Это запустит:
- PostgreSQL на порту 5432
- MongoDB на порту 27017
- Spring Boot App на порту 8080

### Вариант 2: Только приложение (если БД локально)

```bash
docker build -t lab4-app .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/student_db \
  -e SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/studentdb \
  lab4-app
```

---

## Проверка Health Status

### Docker Health Status
```bash
docker ps
# Посмотреть HEALTHCHECK статус в колонке STATUS
```

### Spring Actuator Endpoints

```bash
# Health check
curl http://localhost:8080/actuator/health

# Подробный health статус
curl http://localhost:8080/actuator/health/liveness
curl http://localhost:8080/actuator/health/readiness

# Информация о приложении
curl http://localhost:8080/actuator/info
```

---

## Переменные окружения

Все переменные читаются из окружения Docker контейнера и имеют fallback значения:

- `SPRING_DATASOURCE_URL` - URL PostgreSQL
- `SPRING_DATASOURCE_USERNAME` - Username PostgreSQL
- `SPRING_DATASOURCE_PASSWORD` - Password PostgreSQL
- `SPRING_DATA_MONGODB_URI` - MongoDB connection string
- `JWT_SECRET` - JWT secret key
- `JWT_EXPIRATION` - JWT token expiration time (ms)
- `SPRING_MAIL_*` - Email конфигурация

---

## Логи

### Docker Logs
```bash
docker-compose logs -f app
```

### Kubernetes-style logs для конкретного сервиса
```bash
docker logs -f lab4_app
```

---

## Остановка

```bash
docker-compose down
```

Для удаления всех данных:
```bash
docker-compose down -v
```

---

## Notes

✅ **Не удалено ничего из существующего кода**
- Все оригинальные файлы остались без изменений
- application.properties остался без изменений
- Добавлена только одна зависимость (actuator)
- Созданы новые конфигурационные файлы для Docker

✅ **Обратная совместимость**
- Приложение работает как локально, так и в Docker
- Используется Spring profile `docker` для переключения конфигурации

✅ **Health Check реализован двумя способами**
1. Docker HEALTHCHECK в Dockerfile (для Docker монитора)
2. Spring Actuator `/actuator/health` endpoint
