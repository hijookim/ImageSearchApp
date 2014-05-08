This is a simple Android image search app that uses Google Image Search API to query the images.
The app also stores previous image queries made, so that on tapping on any of the list items will load the image search for the search keyword.

As the user scroll down the list of the image search grid view (a 3x3 image grid), when the user get to the bottom of the page, a new request will asynchronously load more images, and populate grid views as the user scrolls.
Each grid view is loaded asynchronously to allow non-blocking loading of each bitmaps (using picasso library).
When there are no more pages to load, the user will reach the bottom of the image grid.

The request is made using Apache's HttpClient library. The response is returned in json format, and is parsed and mapped to Java objects using Jackson parser library.