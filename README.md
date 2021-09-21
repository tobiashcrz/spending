# spending
### Backend system created in spring boot + react.js to control expenses
---

#### First step:
**Create user for database**

```
alter session set "_ORACLE_SCRIPT"=true; 
CREATE USER SPENDING_MANAGER IDENTIFIED BY SPENDING_MANAGER; 
GRANT CONNECT TO SPENDING_MANAGER; 
GRANT CONNECT, RESOURCE, DBA TO SPENDING_MANAGER; 
GRANT UNLIMITED TABLESPACE TO SPENDING_MANAGER; 
grant all privileges to SPENDING_MANAGER; 
commit;
```

### Second step:
**Run the spring cloud config microservice**

```sh
# Build image Docker
docker build -t config-server .

# Run image Docker
docker run -d -it -p 8888:8888 config-server

#Run with Maven and Docker skiptests
mvn clean package -DskipTests -B && docker build -t config-server . && docker run -d -it -p 8888:8888 config-server
````

### Third step:
**Run the backend spending-manager microservice**
**Important:**
Change IP in bootstrap.properties

```
# Build image Docker
docker build -t spending-manager .

# Run image Docker
docker run -d -it -p 8080:8080 spending-manager

#Run with Maven and Docker skiptests
mvn clean package -DskipTests -B && docker build -t spending-manager . && docker run -d -it -p 8080:8080 spending-manager

#Run with Maven and Docker with tests
mvn clean package && docker build -t spending-manager . && docker run -d -it -p 8080:8080 spending-manager
```

### Fourth step:
**Run the spending-bff microservice that exposes the route security interface to the frontend**
**Important:**
Change IP in bootstrap.properties
```
# Build image Docker
docker build -t spending-bff .

# Run image Docker
docker run -d -it -p 8081:8081 spending-bff

#Run with Maven and Docker skiptests
mvn clean package -DskipTests -B && docker build -t spending-bff . && docker run -d -it -p 8081:8081 spending-bff

#Run with Maven and Docker with tests
mvn clean package && docker build -t spending-bff . && docker run -d -it -p 8081:8081 spending-bff
```

### Fifth step:
**Run the frontend-spending-app application**

```
yarn install
```
```
yarn start
```

### Users for test:
##### ROLE = USER
* user: test
* password: test

##### ROLE = ADMIN
* user: admin
* password: admin