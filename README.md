Selenium:

Selenium is an Open Source Testing Automation Framework, which helps the tester in automating test execution of web applications. It support multiple browsers, OS and languages.

What will the Project Perform?

1. Open the site https://www.ndtv.com/ 
2. Weather section is opened.
3. City is pinned on Map and all the weather details are captured in an Object.
4. API is executed to capture the weather details in another Object.
5. Weather of two objects (Step 3 and Step 4) along with the variance is Compared and test is marked as pass or fail based on comparator response.

Languages Used:
* Java
* Selenium
* Rest Assured
* TestNG

How to build?

* Download the zip or clone the Git repository.
* Unzip the zip file (if you downloaded one).
* Open Eclipse
o File -> Import -> Existing Maven Project -> Navigate to the folder where you unzipped the zip
o Select the right project
* Click on Finish and Project will be imported to your Workspace.
* Build the Project through Eclipse or Open Command Prompt as administrator Change directory to the folder containing the project code base and run the command "maven install". Click enter Key in keyboard.

How to run/use the Project?

Tester needs to be fill the configuration details in config.properties file available at src\main\resources 
1. Application URL
2. City Name for which you want to find the weather
3. Create and Add Path of the screenshots where it will save the Screenshot on any test case failure.
4. Path of the Reports where Emailable will be saved on Execution.
5. Key for API execution
6. The API you wish to call for having the Weather Report.
7. Parameter values for that call e.g. City name/city code etc. based on the API call chosen at Step 6.
8. Variance Value, which will the compare the difference between API weather field and NDTV Weather field.

Tester needs to perform the following after providing the above details:
1. Make sure “drivers” folder has required .exe for the browser on which Test case needs to be run. 
2. Run the Project using any of the two Options:
* Right click on test-Suite.xml available at src\main\resources -> Run As -> TestNG Suite.
* Else, Right click on TestWeatherReport.java available at src\test\java\com\test\cases -> Run As -> TestNG Test.
