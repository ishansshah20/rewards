# Fetch Rewards Assessment.

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

Step 1: Setup tools
> * Download Java, IntelliJ and Postman. Links are given above.
>
> * [Set up java](https://www.youtube.com/watch?v=1ZbHHLobt8A) in local machine. [Set up java in IntelliJ](https://www.youtube.com/watch?v=L7IZ6Ckujbw).
> 

Step 2: Setup project
> * Click on fork on top right corner. It will paste the project on your repository.
>
> * Now copy the path to your repository from your browser's address bar. 
> 
> * On your local machine go to the directory where you want to set up your project.
>
> * On your file explorer write 'cmd' in your address bar. It will open up a command prompt for that directory.
>
> * In that command prompt write "git clone <copied web address in step 4)>". It will clone the project from your repository into your local machine.
>
> * Open IntelliJ. On top click on File->Open. Select the pom.xml in the project folder and select add as project. This will add project into your IntelliJ IDE.
For detailed tutorial [click here](https://vaadin.com/learn/tutorials/modern-web-apps-with-spring-boot-and-vaadin/importing-running-and-debugging-a-java-maven-project-in-intellij-idea)
> 

Step 3: Run project
> * Click on the play button on top. It will start your project. Generally the project starts at localhost:8080  but in case it does not, go to your console and see last second line it will show the port at which your porject is running.
>

Step 4: Test project
> * Now to check our end points(APIs/Services) we would need a postman. Its a tool that will simulate various types of request sent to our webservice. For more details about postman [click here](https://www.postman.com/api-platform/).
>
> * Open postman and click import on the top. Select Fetch Rewards.postman_collection from your project folder. 
> 
> * In the request collection you will see 5 add/transaction requests, 1 spend/rewards request and 1 get/balance request. 
>
> * Send requests in this chronology:

1) **add/transaction request:** Adds a transaction into the database.

      Request Body:
      ```
      { "payer": "DANNON", "points": 300, "timestamp": "2020-10-31T10:00:00Z" }
      ```
      
      Response Body:
      ```
       {
          "message": "Transaction Saved!",
          "success": true,
          "error": "",
          "response": [],
          "status": "OK"
      }
      ```

2) **spend/rewards request:** This will deduct 5000 points.

      Request Body:
      ```
        { "points": 5000 }
      ```

      Response Body:
      ```
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

3) **get/balances request:** It will show payers and their balances.

      Response Body:
      ```
        {
          "message": "Rewards spent!",
          "success": true,
          "error": "",
          "response": {
              "UNILEVER": 200,
              "MILLER COORS": 10000,
              "DANNON": 800
          },
          "status": "OK"
        }
      ```

## Finished!




