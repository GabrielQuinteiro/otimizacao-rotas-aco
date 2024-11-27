# Usar uma imagem base com Java instalado
FROM openjdk:17-slim

# Definir o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar o arquivo JAR para o contêiner
COPY deploy/otimizacao-rotas-aco-1.0-SNAPSHOT.jar /app/otimizacao-rotas.jar

# Expor a porta usada pelo seu backend
EXPOSE 4567

# Definir o comando de inicialização do seu backend
CMD ["java", "-jar", "otimizacao-rotas.jar"]
