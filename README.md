# Popular Movies
# Android Developer Nanodegree Project 

This project uses an API key from https://www.themoviedb.org/. You need to request your key and follow the instructions below for the application to work.

In the folder app/res/values/, create a resource xml file that has a string
name = "api_key".

In MainActivity.java, your key will be retrieved right here:

```java
try{
	mApiKey = getResources().getString(R.string.api_key);
}
catch(Exception e) {
	e.printStackTrace();
	mApiKey = "";
}
```
  
You should now see movie posters on the application screen. 
