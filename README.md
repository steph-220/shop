Simple shop project with products and processing orders features.

Build and run containers (app + db) with: docker-compose up --build

After docker containers are up, all tables are automatically created. Use swagger-ui to add data.

API documentation available on: http://localhost:8089/swagger-ui/

mysql can be reached on localhost port 7306
app can be reached on localhosts port 8089

Proposal for authentication and authorization:
Simple solution would be adding JdbcTokenStore inside existing 'shop' schema for storing user info.
Protocol would use oauth2 with JWT.
The idea is after user is registrated (id, email, name and password are stored in db), user can get token holding info about his permissions.
JWT can be used to authorize requests on server side until it expires, after which user/client would need to refresh token and continue using the app.
The whole process would be stateless (no sessions) so if app is distributed or if it is divided in microservices, client app (FE, mobile etc.) would need to use just one token for all instances, and BE app would not need to worry about sessions.
From JWT BE app can easily check who is user, his permission, is token valid and act accordingly.
Even if JWT is stolen password would not be exposed, and attacker could only use token until it expires (few minutes at most).

All above in Spring Boot could be achieved with extending AuthorizationServerConfigurerAdapter, ResourceServerConfigurerAdapter and WebSecurityConfigurerAdapter.


Proposal for redundancy:
Application should be distributed, if any machine unexpectedly fails, other can serve clients.
One example would be to use ASG on AWS across few availability zones, so even in case of data center outage application would continue to work.
Healthchecks should also be added so failed services could be replaced, and app could be scaled using memory, cpu etc. usages across instances.
Deployment strategy could be either blue-green or rolling update, so when app is deployed clients won't have downtime.
Before every deployment app should pass through one or more testing environments and as a result there is less chance for bugs/errors.
If there are lots of orders app could be divided in product and order services, and between them queue could be added, so order service could act as a consumer for orders, and even in case of failure or slower execution order service could catch up with client orders.

DB should be separated from app, and should also be distributed.
As online shop is more read heavy type of service, one way would be to use read-replication, having one master node, and n replicas.
If shop start to gain a lot of customers and new product, sharding should also be considered or moving away from SQL db to leaderless db architecture (Dynamo like dbs).
