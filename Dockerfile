# 使用 OpenJDK 作為基底映像
FROM eclipse-temurin:17-jdk

# 設定工作目錄
WORKDIR /app

# 複製 Maven 打包後的 Jar 檔
COPY target/*.jar app.jar

# 預設執行指令
ENTRYPOINT ["java", "-jar", "app.jar"]