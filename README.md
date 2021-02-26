# Fetch Rewards.

## About

> Our users have points in their accounts. Users only see a single balance in their accounts. But for reporting purposes we actually track their points per payer/partner. 
>
> In our system, each transaction record contains: payer (string), points (integer), timestamp (date). For earning points it is easy to assign a payer, we know which actions earned the points. And thus which partner should be paying for the points. When a user spends points, they don't know or care which payer the points come from. But, our accounting team does care how the points are spent. 
>
> There are two rules for determining what points to "spend" first:
>
> 1) We want the oldest points to be spent first (oldest based on transaction timestamp, not the order they’re received)
>
> 2) We want no payer's points to go negative.


## Technologies Used

> **Programming Language:** [Java](https://en.wikipedia.org/wiki/Java_(programming_language))
>
> **Framework:** [Spring Boot](https://www.tutorialspoint.com/spring_boot/spring_boot_introduction.htm)
>
> **Database:** [H2](https://en.wikipedia.org/wiki/H2_(DBMS))
>
> Here I have used H2 database because using a durable data store was not a requirement. So I am storing transactions in memory using H2.
>
> **Tools:** IntelliJ, Postman

## Links to download the Tools

> [Java](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
>
> [IntelliJ](https://www.jetbrains.com/idea/download/#section=windows)
>
> [Postman](https://www.postman.com/downloads/)

## How to run the project

Step 1: Setting up the tools
> * Download Java, IntelliJ and Postman. Links are given above.
>
> * [Set up java](https://www.youtube.com/watch?v=1ZbHHLobt8A) in local machine. [Set up java in IntelliJ](https://www.youtube.com/watch?v=L7IZ6Ckujbw).
> 

Step 2: Setting up the project
> * Using "-git clone" clone the project into your local machine.
>
> * Open IntelliJ. On top click on File->Open. Select the pom.xml in the project folder and select add as project. This will add project into your IntelliJ IDE.
For detailed tutorial [click here](https://vaadin.com/learn/tutorials/modern-web-apps-with-spring-boot-and-vaadin/importing-running-and-debugging-a-java-maven-project-in-intellij-idea)
> 
> * Run the project. 

Note: Generally the project starts at localhost:8080  but in case it does not, go to your console and see last second line it will show the port at which your porject is running.

Step 3: Testing the project
> * Now to check our end points(APIs/Services) we would need a postman. Its a tool that will simulate various types of request sent to our webservice. For more details about postman [click here](https://www.postman.com/api-platform/).
>
> * Open postman and import Fetch Rewards.postman_collection from your project folder. 
> 

## Requests

## 1) Add new transaction: 

Adds a new transaction into the database.

>**URL** : `localhost:8080/spend/rewards`
>
>**Method** : `POST`
>
>**Auth required** : NO
>
>**Permissions required** : None
>

**Data constraints**

Provide name of Payer, points we want to add for that payer and the timestamp.

```json
{ 
"payer": ["Name of the payer"], 
"points": ["Points to add"], 
"timestamp": ["Time stamp in format YYYY-MM-DDTHH:MM:SSZ"] 
}
```

**Data example** All fields must be sent.

```json
{ 
"payer": "DANNON",
"points": 300,
"timestamp": "2020-10-31T10:00:00Z"
}
```
   
**Success Response**

>**Condition** : If transaction is added successfully.
>
>**Code** : `201 CREATED`
>
>**Content example**

```json
 {
"message": "Transaction Saved!",
"success": true,
"error": "",
"response": [
  {
      "tid": "27a1141e-b8c1-4e0f-aba8-8ff1935c604a",
      "payer": "MILLER COORS",
      "points": 10000,
      "timestamp": "2020-11-01T14:00:00.000+00:00",
      "processed": false
  }
],
"status": "CREATED"
}
```

**Error Response**

>**Condition** : If transaction is not saved internally
>
>**Code** : `400 BAD REQUEST`
>
>**Content example** :

```json
{
    "message": "Error saving the transaction",
    "success": false,
    "error": "Internal error",
    "response": [
       {
         "tid": "27a1141e-b8c1-4e0f-aba8-8ff1935c604a",
         "payer": "MILLER COORS",
         "points": 10000,
         "timestamp": "2020-11-01T14:00:00.000+00:00",
         "processed": false
       }
   ],
    "status": "BAD_REQUEST"
}
```

## 2) Spend the points

Spend the points according to the rules

>**URL** : `localhost:8080/spend/rewards`
>
>**Method** : `POST`
>
>**Auth required** : NO
>
>**Permissions required** : None

**Data constraints**

```json
{
    "points": "[points to spend]",
}
```

**Data examples**

```json
{
    "points": 5000
}
```

**Success Responses**
>
>**Condition** : Data provided is valid and User is Authenticated.
>
>**Code** : `200 OK`
>
>**Content example** : Response will reflect back the information on the points spent from payers. 

```json
{
    "message": "Rewards spent!",
    "success": true,
    "error": "",
    "response": [
        {
            "points": -200,
            "payer": "UNILEVER"
        },
        {
            "points": -4700,
            "payer": "MILLER COORS"
        },
        {
            "points": -100,
            "payer": "DANNON"
        }
    ],
    "status": "OK"
}
```

**Error Response**

>**Condition** : If provided data amount is greater than the total points balance.
>
>**Code** : `400 BAD REQUEST`
>
>**Content example** :

```json
{
    "message": "Not enough balance",
    "success": false,
    "error": "Amount passed is greater than the total amount",
    "response": [],
    "status": "BAD_REQUEST"
}
```

## 3) Show payer balances

Its shows the current balances of points of the payers

>**URL** : `localhost:8080/get/balance`
>
>**Method** : `GET`
>
>**Auth required** : NO
>
>**Permissions required** : None

**Success Response**

>**Code** : `200 OK`
>
>**Content examples**

```json
{
    "message": "Rewards spent!",
    "success": true,
    "error": "",
    "response": {
        "UNILEVER": 200,
        "MILLER COORS": 10000,
        "DANNON": 1100
    },
    "status": "OK"
}
```
