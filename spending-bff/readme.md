# Build image Docker
docker build -t spending-bff .

# Run image Docker
docker run -d -it -p 8081:8081 spending-bff

#Run with Maven and Docker skiptests
mvn clean package -DskipTests -B && docker build -t spending-bff . && docker run -d -it -p 8081:8081 spending-bff

#Run with Maven and Docker with tests
mvn clean package && docker build -t spending-bff . && docker run -d -it -p 8081:8081 spending-bff