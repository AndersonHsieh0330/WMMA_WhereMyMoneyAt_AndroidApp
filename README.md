# WMMA_WhereMyMoneyAt_AndroidApp
This is a full stack application I built that allows you to keep track of your expenses! 
WhereMyMoneyAt is a CRUD application and this repository is is the front end Android part of this project. I used Retrofit to fetch data from [WMMA_RESTAPI](https://github.com/AndersonHsieh0330/WMMA_WhereMyMoneyAt_RestAPI), Manipulating data in the remote PostgreSQL database. On top of fetching data through HTTP requests, I also used SQlite(with the help of Room) for local cache so you can access data offline. However, Editing, Creating and Deleting Data offline is not supported. 


**Note: The [WMMA_RESTAPI](https://github.com/AndersonHsieh0330/WMMA_WhereMyMoneyAt_RestAPI) is published on Heroku, but Heroku puts web application to sleep after if the app idles for too long. Thus the first HTTP request to might be significantly slower than usual. Try sending another request or wait for a little longer if this issue come up.**


## WMMA supports:
- Create, Read, Update, Delete Transaction data



## Some new things I’ve learned:
- Enhance MVVM project structure
- Using Retrofit to fetch data from a Web API
- Using Room to keep local data cache in SQLite
- Using LiveData and ViewModel 
- Custome ViewModelFactory



## Libraries and resources I used 
- [Retrofit](https://square.github.io/retrofit/)
- [SwipeRevealLayout](https://github.com/chthai64/SwipeRevealLayout)
- Room + SQLite
- Android LifeCycle Components
