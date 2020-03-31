# Exchange Rates Api
This api is to provide exchange rates against EUR for GBP, USD, HKD.
It uses in memory mongo db and the values used are not real.
The api has rest endpoints exposed as well as basic UI pages to view the exchange rates
### Local Running
Needs java 8

Start the app:

* `./gradlew bootRun`

Home Page:  http://localhost:8080/

The app uses basic security. Use  **user** for username and **password** for password

Swagger for rest api : 
* http://localhost:8080/v2/api-docs
* http://localhost:8080/swagger-ui.html

Rest End points

* Latest Rates : http://localhost:8080/api/latest
* Previous Rates: http://localhost:8080/api/previous/2020-04-01

