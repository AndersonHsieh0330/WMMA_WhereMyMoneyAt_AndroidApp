# WMMA_WhereMyMoneyAt_AndroidApp
This is a full stack application I built that allows you to keep track of your expenses! 
WhereMyMoneyAt is a CRUD application and this repository is is the front end Android part of this project. I used Retrofit to fetch data from [WMMA_RESTAPI](https://github.com/AndersonHsieh0330/WMMA_WhereMyMoneyAt_RestAPI), Manipulating data in the remote PostgreSQL database. On top of fetching data through HTTP requests, I also used SQlite(with the help of R) for local cache so you can access data even when offline

**Note: The [WMMA_RESTAPI](https://github.com/AndersonHsieh0330/WMMA_WhereMyMoneyAt_RestAPI) is published on Heroku, but Heroku puts web application to sleep after if the app idles for too long. Thus the first HTTP request to might be significantly slower than usual. Try sending another request or wait for a little longer if this issue come up.

Wat2Eat supports:
- Create, Read, Update, Delete expense entries

Some new things I’ve learned:
- Transitioning from Java to Kotlin 
- Restful APIs
- Send http request with Volley as HTTP client
- Presenting JSON data response from API
- FusedLocationClient for fetching the geocode
- Runtime permission request 
- Presenting image links: Glide
- How to attribute resources used in a app and software licenses
- How to publish an app to Google Play Store
- Activity BackStak

Libraries and resources I used 
- Volley
- Glide
- FusedLocationClient API (for Geolocation)
- Google places APIs: Nearby Search, Place Detail, Place Photo 
