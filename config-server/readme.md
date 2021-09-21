# Build image Docker
docker build -t config-server .

# Run image Docker
docker run -d -it -p 8888:8888 config-server

#Run with Maven and Docker skiptests
mvn clean package -DskipTests -B && docker build -t config-server . && docker run -d -it -p 8888:8888 config-server