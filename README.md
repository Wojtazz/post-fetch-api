# PostFetchApiApp
PostFetchApiApp is application providing fetching posts from https://jsonplaceholder.typicode.com/posts and synchronize them with local database.

Functionality:
  - Getting posts from local database and filter them by title (filter method - LIKE),
  - Getting posts from external url and save them every 24h (12 AM) or by endpoint (skipping edited and deleted posts before),
  - Editing posts from local database,
  - Deleting posts from local database.

Every successful edit and delete requests of posts save this activity to ActivityLog (Activity entity).
Second solution was to create flags isModify and isDelete to Post entity, but to provide strict structure of Post I used first idea. 

# Configuration
Application is based on Spring boot, Hibernate and Rest Api and uses Postgres SQL database.
### Database
To connect to database you need to create database named "postdb" on server postgres. 

Connection string:
```
spring.datasource.url=jdbc:postgresql://127.0.0.1:5432/postdb
```
# Endpoints

Application provides four endpoints:

Synchronize posts and save them to local database without edited and deleted before:
```
GET
http://localhost:8081/posts/synchronize
```
Get all posts from local database:
```
GET
http://localhost:8081/posts
```
Edit post from local database:
```
PUT
http://localhost:8081/posts/{id}
```
Delete post from local database:
```
DELETE
http://localhost:8081/posts/{id}
```

Getting post example body to filter posts (filtering method - LIKE):
```
{
  "title": "sint",
}
```
Edit post example body to edit specific post:
```
{
  "title": "edited",
  "body": "edited"
}
```


### Others

Application is providing JUnit and Mockito testing of services, 
repositories and controller layers of application.

IDE used for implementation is Intelij.
