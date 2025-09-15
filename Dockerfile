# -------- 建置階段 --------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# 先複製 pom.xml，讓 Docker 能快取依賴
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 再複製 src，避免每次改 code 都要重新下載依賴
COPY src ./src

# 打包成 JAR（跳過測試）
RUN mvn clean package -DskipTests

# -------- 執行階段 --------
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 從上一個階段 copy jar
COPY --from=build /app/target/*.jar app.jar

# 執行 Spring Boot
ENTRYPOINT ["java","-jar","app.jar"]
