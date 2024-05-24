## Get all routes
<host-name>/actuator/gateway/routes

## Start local docker image
docker run -p 8074:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:20.0.3 start-dev