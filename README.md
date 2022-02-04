# MovieNepal

This is the application realted to movies

This application consists of good user interface along with animation during activities loading and item loading.

This application works on both online as well as offline mode.
 The offline is achieved using room databse which is new technology available.

In this application the list of movies are fetch from the movie databse i.e https://www.themoviedb.org/
 Get URL : https://api.themoviedb.org/3/movie/now_playing?api_key=80dff2970093b6849866a98cc4bf6f34&language=en-US&page=1

Images are loaded using **Glide** library with the URL : https://image.tmdb.org/t/p/w500/{image_poster_path} 

Movie detail is fetch using the movie id with the help of URL : https://api.themoviedb.org3/movie/{movie_id}?api_key=80dff2970093b6849866a98cc4bf6f34

In the movie Detail section there is detail information about the movies such as movie's released date, duration , rating , description, movie collection if avaiable, production companies and country, and so on.
There is a vido streaming for the related movies

For video streaming we use the **exo player** with default link 

## How to build and run the project
1. Download this project from the github link `https://github.com/Rajan-developer/MovieNepal`
2. Open the project in code editor called **Android studio**
3. Connect the device with USB debugging model enabled.
4. Synchronizing completed, build the app in the device by clicking :arrow_forward: button on editor.
