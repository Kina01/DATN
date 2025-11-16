# BUILD STAGE: build backend/backend bằng Maven (JDK 21)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# copy chỉ thư mục backend để tránh copy cả repo
COPY backend ./backend

# vào thư mục backend và build
WORKDIR /app/backend
RUN mvn clean package -DskipTests

# RUN STAGE
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/backend/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
