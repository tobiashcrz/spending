# Build image Docker
docker build -t spending-manager .

# Run image Docker
docker run -d -it -p 8080:8080 spending-manager

#Run with Maven and Docker skiptests
mvn clean package -DskipTests -B && docker build -t spending-manager . && docker run -d -it -p 8080:8080 spending-manager

#Run with Maven and Docker with tests
mvn clean package && docker build -t spending-manager . && docker run -d -it -p 8080:8080 spending-manager