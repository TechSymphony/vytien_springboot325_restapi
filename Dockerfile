FROM amazoncorretto:17 as build

WORKDIR /app
COPY . .

RUN ./gradlew build -x test
CMD ["java","-jar","vytien_springboot325_restfulapi-0.0.1-SNAPSHOT.jar"]