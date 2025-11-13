# Etapa 1: Build (construcción con JDK completo)
FROM eclipse-temurin:21-jdk-alpine AS builder

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Dar permisos de ejecución al wrapper de Maven
RUN chmod +x mvnw

# Descargar dependencias (aprovecha el cache de Docker)
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar la aplicación y generar el JAR
RUN ./mvnw clean package -DskipTests

# Extraer el JAR para optimizar las capas
RUN mkdir -p target/dependency && \
    cd target/dependency && \
    jar -xf ../*.jar

# Etapa 2: Runtime (ejecución con JRE ligero)
FROM eclipse-temurin:21-jre-alpine

# Crear usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Establecer directorio de trabajo
WORKDIR /app

# Variables de entorno para optimización de JVM
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Copiar las capas de la aplicación desde el builder
ARG DEPENDENCY=/app/target/dependency
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib ./lib
COPY --from=builder ${DEPENDENCY}/META-INF ./META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes ./

# Exponer el puerto de la aplicación
EXPOSE 8080

# Comando de entrada para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -cp .:./lib/* com.alumnositm.todo.TodoApplication"]
