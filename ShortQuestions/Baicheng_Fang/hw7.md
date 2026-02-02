## Explain Referential Integrity in RDBMS/SQL with sample queries. ##
What:   
Referential Integrity means if a table refers another table the refereced data must exist.  

Why:  
To prevent invalid references (e.g., orders linked to non-existing users).  
Maintains data consistency and avoid orphan records.  

How:  
Foreign Keys

Sample:  
```SQL
CREATE TABLE Customers (
    customer_id INT PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE Orders (
    order_id INT PRIMARY KEY,
    customer_id INT,
    order_data DATE,
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id) 
);

// Valid
INSERT INTO Customers VALUES (1, 'Anderson');
INSERT INTO Orders VALUES (1, 1, '2026-01-01');

// Invalid
INSERT INTO Orders VALUES (2, 2, '2026-01-01');  // no customer
```

Can define cascade child rows update/delete
```SQL
CREATE TABLE Orders (
    order_id INT PRIMARY KEY,
    customer_d INT,
    FOREIGN KEY (customer_d) REFERENCES Customer(customer_d)
                    ON DELETE CASCADE 
                    ON UPDATE CASCADE;
)
```

## Explain Join in RDBMS/SQL with sample queries. ##
What:  
Combine rows from two or more tables based on a related column.  

Why:  
Bring records together for querying  

How:  
1.INNER JOIN: returns only matching records that appear in both tables
```sql
SELECT Customers.name, Order.order_id
FROM Customers
INNER JOIN Orders
ON Customers.customer_id = Orders.customer_id;

Anderson | 001
Anderson | 002
```


2.LEFT JOIN: Return all rows from left table, if no matching from right table then column would be NULL
```sql
SELECT Customers.name, Orders.order_id
FROM Customers
LEFT JOIN Orders
ON Customers.customer_id = Orders.customer_id;

Anderson | 001
Anderson | 002
Emma     | NULL
```

3. RIGHT JOIN: Return all rows from right table, if no matching from left table then NULL
```sql
SELECT Customers.name, Orders.order_id
FROM Customers
RIGHT JOIN Orders
ON Customers.customer_id = Orders.customer_id;

Anderson | 001
Anderson | 002
NULL     | 003
```

4. FULL JOIN: Return all rows from both tables
```sql
SELECT Customers.name, Orders.order_id
FROM Customers
FULL JOIN Orders
ON Customers.customer_id = Orders.customer_id;

Anderson | 001
Anderson | 002
Emma     | NULL
NULL     | 003
```


## Compare Developer API vs User API ##
Developer API:  
1. methods, interface
2. rest API, GraphQL, soap, RPC(Http API)

User API:  
1. rest API, GraphQL, soap, RPC(Http API)

## Explain components of REST API? ##
#### Resource ####
- Any piece of data the API exposes
- Represented by a URI (endpoint)
- Usually a noun

```text
/users
/orders
/mechandise/001
```

#### HTTP Methods ####
- Define what action you want to perform on a resource
- A verb

```text
| Method | Purpose                |
| ------ | ---------------------- |
| GET    | Read data              |
| POST   | Create data            |
| PUT    | Update entire resource |
| PATCH  | Partial update         |
| DELETE | Remove data            |

```

#### Request ####
- Endpoint (URI)
- HTTP Method
- Headers
- Query
- Request Body

#### Response ####
- HTTP Status code
```text
| Code | Meaning      |
| ---- | ------------ |
| 200  | OK           |
| 201  | Created      |
| 400  | Bad Request  |
| 401  | Unauthorized |
| 403  | Forbidden    |
| 404  | Not Found    |
| 500  | Server Error |

```
- Response Headers
- Responde Body

#### Representation ####
- JSON
- XML
- YAML

## Compare each type of HTTP Method ###

```text
| Method | Purpose                |
| ------ | ---------------------- |
| GET    | Read data              |
| POST   | Create data            |
| PUT    | Update entire resource |
| PATCH  | Partial update         |
| DELETE | Remove data            |

```

## Explain authentication field in http header? ##
Authentication field in an HTTP header, typically the Authorization header, is used to prove the identity of the client maing the request.

Most common way is bearer token authentication.
- Client sends a token (ususally JWT)
- Server validates the token


## Explain cookies field in http header? ##
Cookies are small pieces of data stored in the client (browser) and sent to the server with every HTTP request to maintain state.
HTTP itself is stateless, so cookies help servers remember users.  
Cookies are used for 
- Session management (login state)
- User preferences
- Shopping carts
- Tracking / analytics

Cookie vs Session
```txt
| Concept    | Cookie                    | Session           |
| ---------- | ------------------------- | ----------------- |
| Stored     | Client (browser)          | Server            |
| Identifier | Cookie value              | Session ID        |
| State      | Stateless HTTP workaround | Server state      |
| Expiry     | Time-based                | Server-controlled |

```
- Cookie stores session ID
- Session data lives on server

Common Cookie Attributes
- HttpOnly: Prevents access vis JS, protects against XSS attacks
- Secure: Sent only over HTTPS
- SameSite
- Strict: Only same-site rqst
- None: Cross-site allowed (must use Secure)
- Lax: Top-level navigation allowed

Cookie Flow (login example)
- Login Request: POST /login
- Server Response: Set-Cookie: sessionId=qwer001; HttpOnly; Secure
- Subsequent Requests
```txt
GET /shoppingcart
Cookie: sessionId=qwer001
```
To summarize
```txt
| Header     | Purpose       |
| ---------- | ------------- |
| Set-Cookie | Store cookie  |
| Cookie     | Send cookie   |
| HttpOnly   | JS protection |
| Secure     | HTTPS only    |
| SameSite   | CSRF control  |

```

## Explain the purpose of http response headers? Why it is necessary? ##
HTTP response headers are key-value pairs sent by the server to the client in an HTTP response.  
They only provide metadata about the response, not the actual data itself.

#### Describe Response Content ####
```http
Content-Type: application/json
Content-Length: 512
```

#### Control Caching Behavior ####
```http
Cache-Control: max-age=3600
ETag: "qwer001"
```

#### Security ####
```http request
Strict-Transport-Security: max-age=31536000
X-Content-Type-Options: nosniff
Content-Security-Policy: default-src 'self'
```

To summarize
- HTTP is stateless
- Headers provide context and control
- Improve performance, security and compatibility