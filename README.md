The issue only presents itself if you deploy this application to heroku.

Expected behavior

html loads for `/` and `/chess`

Observed behavior

When loading this app (only when served through heroku)

 * blank pages for `/` and `/chess`, network tab shows that responses are empty but status is SUCESS 200
 * Sometimes the error ERR_INCOMPLETE_CHUNKED_ENCODING will be seen in the browser devtools
 * curl also fails to load and says that the connection was closed before the content was sent
