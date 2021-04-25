QA_Development_Challenge
Automation Test for a web application: https://openweathermap.org/

Features:
  1. Call current weather data for one location
  2. One Call API - Current and forecast weather data

Technical in project:
-	Language: Java
-	Selenium + Gradle for UI test
-	Rest Assured for API test
-	BDD Cucumber framework
-	Data driven
-	Using Cucumber report
-	Integrate CI/CD (This item will be attached in another file)

Automation approach:
-	Because I don’t control data and don’t have permission to access to database so that I cannot get data to verify with Api response. My approach will be:
   o	Open https://openweathermap.org/ and get data from UI
   o	Send API request to get response data
   o	Verify data between UI and Api response

How to set up this project?
-	Install java sdk version 8: https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html
-	Install IntelliJ: https://www.jetbrains.com/idea/download/
-	Install Gradle 7.0: https://gradle.org/install/
-	Clone source from this repo to your PC

How to execute?
-	Open terminal, run "grade test"
-	Report will be generated at “htmlReport” folder

Notes: The scenario “2. One Call API” is failed because this is a bug. Please help to check in my bug list file (refer bug No.1).
