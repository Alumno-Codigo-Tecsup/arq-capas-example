# # 🏗️ Etapa 1: Construcción del JAR
# FROM eclipse-temurin:21-jre AS builder
# WORKDIR /app
# # Copiar archivos de Maven
# COPY .mvn/ .mvn
# COPY mvnw pom.xml ./
# # Descargar dependencias en caché
# RUN ./mvnw dependency:go-offline
# # Copiar el código fuente y compilar
# COPY src ./src
# RUN ./mvnw clean package -DskipTests
# # 🚀 Etapa 2: Imagen final optimizada
# FROM eclipse-temurin:21-jre
# WORKDIR /app
# # Copiar solo el JAR generado
# COPY --from=builder /app/target/*.jar app.jar
# # Exponer el puerto
# EXPOSE 8080
# # Ejecutar la aplicación con el JAR
# CMD ["java", "-jar", "app.jar"]
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8081

# Variables de entorno para la conexión a Neon
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://ep-twilight-sound-a504i3no-pooler.us-east-2.aws.neon.tech/MyJavaApp
ENV SPRING_DATASOURCE_USERNAME=MyJavaApp_owner
ENV SPRING_DATASOURCE_PASSWORD=npg_eFrWmzsd10BI

ENTRYPOINT ["java", "-jar", "app.jar"]
