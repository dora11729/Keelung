# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# 先複製 pom.xml 來下載依賴
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 再複製 src
COPY src ./src

# 打包
RUN mvn clean package -DskipTests

# Debug: 確認 jar 有生出來
RUN ls -l /app/target

# Run stage
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
