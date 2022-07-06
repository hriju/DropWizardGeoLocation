# Dropwizard IP Geolocation 


How to start the application
---

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/geolocationAPI-1.0.0.jar server config.yml`
3. To check that your application is running enter url(on Postman or Browser) `http://localhost:8080/geolocationAPI/69.162.81.155`
   you will have responses like the following:
   `{"id":2,"query":"69.162.81.155","status":"success","country":"United States","countryCode":"US","region":"TX","regionName":"Texas","city":"Dallas","zip":"75270","lat":"32.7767","lon":"-96.797","timezone":"America/Chicago","isp":"Limestone Networks","org":"Vadim Mazo","as":"AS46475 Limestone Networks, Inc."}`
check with different IP
---

You can try with different IP as well for example if you use 162.254.206.227  `http://localhost:8080/geolocationAPI/162.254.206.227`

`{"id":3,"query":"162.254.206.227","status":"success","country":"United States","countryCode":"US","region":"FL","regionName":"Florida","city":"Miami","zip":"33197","lat":"25.7689","lon":"-80.1946","timezone":"America/New_York","isp":"GoDaddy.com","org":"Dana Consulting","as":"AS29066 Host Europe GmbH"}`
