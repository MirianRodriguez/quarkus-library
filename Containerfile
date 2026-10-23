# imagen base - cambiar por la de agip
FROM registry.access.redhat.com/ubi9/openjdk-25-runtime:1.24

# Copiar la aplicación Quarkus en capas, para reutilizar las capas que no tienen cambios
COPY --chown=185 target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 target/quarkus-app/*.jar /deployments/
COPY --chown=185 target/quarkus-app/app/ /deployments/app/
COPY --chown=185 target/quarkus-app/quarkus/ /deployments/quarkus/

# Puerto HTTP
EXPOSE 8080

# Ejecutar con un usuario sin privilegios de root
USER 185

# Configura opciones adicionales de la JVM y de Quarkus
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

# indica el JAR que se debe ejecutar
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

# indica qué proceso se ejecuta al arrancar el servidor
ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]
