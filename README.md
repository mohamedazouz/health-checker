Build and Deploy
----------------------

Make sure you have all necessary tools installed

    Java 8
    
Build the application:

    mvn clean install
     
Run locally
----------------------

   
    * Java
        java -jar target/health-checker.jar 
    
Running behaviour
----------------------

* You have to set the service info in src/main/resources/application.properties 
* You can run the service as instructed above
* The service is always running with ni output unless there's a failure call will be printed as following:
   * ex. `failure call Service:magnificentService`
* after N time of unsucess trials will print as following:
  * ex. `Service:magnificentService is unealthy`
* If the service goes up again will print:
  * ex. `Service:magnificentService is back again`


