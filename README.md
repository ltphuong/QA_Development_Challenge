Automation Test for a web application: https://openweathermap.org/

Features:
  1. Call current weather data for one location
  2. One Call API - Current and forecast weather data

Technical in project:
-	Programming language: Java
-	Selenium + Gradle for UI test
-	Rest Assured for API test
-	BDD Cucumber framework
-	Data driven
-	Using Cucumber report
-	CI/CD integration (This item will be attached in another file)

Automation approach:
-	Because I don’t control data and don’t have permission to access to database so that I cannot get data to verify with Api response. My approach will be:
   o	Open https://openweathermap.org/ and get data from UI
   o	Send API request to get response data
   o	Verify data between UI and Api response

Install
-	Install java sdk version 8: https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html
-	Install IntelliJ: https://www.jetbrains.com/idea/download/
-	Install Gradle 7.0: https://gradle.org/install/

How to execute?
- Clone source from this repo to your PC
-	Go to folder that store souce code, open terminal and run "grade test"
-	Report will be generated at “htmlReport” folder

Notes: The scenario “2. One Call API” is failed because this is a bug. Please help to refer to in my bug list file (refer bug No.1).
![image](https://user-images.githubusercontent.com/17809726/116035362-10f5a400-a68f-11eb-8932-72d0cce88d2b.png)
