FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew build

EXPOSE 8080

CMD ["java", "-jar", "app/build/libs/app-1.0.0.jar"]