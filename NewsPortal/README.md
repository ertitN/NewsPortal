# News Portal REST API

This project provides a REST API for managing news titles, content, and authors. The API supports operations such as creating, updating, deleting, and listing news articles.

## Table of Contents

- [Features](#Features)
- [Installation](#Installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)

## Features

- User registration with role-based access control
- Users with the role of 'Author' can create news articles and update their own articles
- Only the user who created a news article can update it
- Admin users have permissions to update and delete any news articles
- Create, update, delete, and list news articles
- Create, update, and list authors
- Works with MySQL database (can be configured to work with other databases)
- Basic authentication and authorization

## Technologies

- Java
- Spring Boot
- Maven
- MySQL
- Lombok
- MapStruct

## Installation


### Steps

1. Install dependencies and build the project:
    ```
    mvn clean install
    ```
   
2. Run the application:
     ```
    mvn spring-boot:run
    ```

## Usage
### Example API Requests
#### User Registration
```bash
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{
        "username": "newuser",
        "password": "password"
  }
  ```
  
      

#### Create a News Article
```bash
curl -X POST http://localhost:8080/api/news \
  -H "Content-Type: application/json" \
  -d '{
        "title": "New News Title",
        "content": "New news content",
        "user": {
            "id": 1,
            "name": "Author Name"
        }
      }   
```

#### Create a News Article (Author Role)
```bash
curl -X POST http://localhost:8080/api/news \
  -u username:password \
  -H "Content-Type: application/json" \
  -d '{
        "title": "New News Title",
        "content": "New news content",
        "author": {
            "id": 1,
            "name": "Author Name"
        }
      }'

```

#### List All News Articles
```bash
curl -X GET http://localhost:8080/api/news
```

#### Update a News Article(Author Role,Own Article)
```bash
curl -X PUT http://localhost:8080/api/news/{id} \
  -u username:password \
  -H "Content-Type: application/json" \
  -d '{
        "title": "Updated News Title",
        "content": "Updated news content"
      }'
```

### API Endpoints

| HTTP Method | Endpoint          | Description                                                  |
|-------------|-------------------|--------------------------------------------------------------|
| POST        | /api/register     | Register a new user                                          |
| GET         | /api/news         | List all news articles                                       |
| POST        | /api/news         | Create a new news article                                    |
| GET         | /api/news/{id}    | Get a specific news article                                  |
| PUT         | /api/news/{id}    | Update a specific news article (only by owner or admin)      |
| DELETE      | /api/news/{id}    | Delete a specific news article (only by admin)               |       
| GET         | /api/users/{id} | Get a specific author                                        |
| PUT         | /api/users/{id} | Update a specific author                                     |
| DELETE      | /api/users/{id} | Delete a specific author                                     |

