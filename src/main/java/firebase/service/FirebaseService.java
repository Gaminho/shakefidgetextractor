package firebase.service;

import firebase.error.FirebaseException;
import firebase.utils.JacksonUtility;
import firebase.error.JacksonUtilityException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FirebaseService {

    private static final String FIREBASE_API_JSON_EXTENSION = ".json";
    private static final String FIREBASE_SHAKE_FIDGET_BASE_URL = "https://songwritter-377b0.firebaseio.com/shakefidget/";



///////////////////////////////////////////////////////////////////////////////
//
// PROPERTIES & CONSTRUCTORS
//
///////////////////////////////////////////////////////////////////////////////


    private final String baseUrl;
    private String secureToken = null;
    private List<NameValuePair> query;
    private boolean useJsonExt = true;


    public FirebaseService(final String baseUrl) throws FirebaseException {
        if (StringUtils.isBlank(baseUrl)) {
            final String msg = "baseUrl cannot be null or empty; was: '" + baseUrl + "'";
            throw new FirebaseException( msg );
        } else {
            this.baseUrl = baseUrl.trim();
            this.query = new ArrayList<>();
        }
    }


    public FirebaseService(final String baseUrl,
                           final String secureToken) throws FirebaseException {
        this(baseUrl);
        this.secureToken = secureToken;
    }



///////////////////////////////////////////////////////////////////////////////
//
// PUBLIC API
//
///////////////////////////////////////////////////////////////////////////////


    public FirebaseResponse get() throws FirebaseException, UnsupportedEncodingException {
        return this.get( null );
    }


    public FirebaseResponse get( String path ) throws FirebaseException, UnsupportedEncodingException {

        // make the request
        String url = this.buildFullUrlFromRelativePath( path );
        HttpGet request = new HttpGet( url );
        HttpResponse httpResponse = this.makeRequest( request );

        // process the response
        FirebaseResponse response = this.processResponse( FirebaseRestMethod.GET, httpResponse );

        return response;
    }

    public FirebaseResponse patch(Map<String, Object> data) throws FirebaseException, JacksonUtilityException, UnsupportedEncodingException {
        return this.patch(null, data);
    }

    public FirebaseResponse patch(String path, Map<String, Object> data) throws FirebaseException, JacksonUtilityException, UnsupportedEncodingException {
        // make the request
        String url = this.buildFullUrlFromRelativePath( path );
        //HttpPut request = new HttpPut( url );
        HttpPatch request = new HttpPatch(url);
        request.setEntity( this.buildEntityFromDataMap( data ) );
        HttpResponse httpResponse = this.makeRequest( request );

        // process the response
        FirebaseResponse response = this.processResponse( FirebaseRestMethod.PATCH, httpResponse );

        return response;
    }

    public FirebaseResponse patch(String jsonData) throws UnsupportedEncodingException, FirebaseException {
        return this.patch(null, jsonData);
    }

    public FirebaseResponse patch(String path, String jsonData) throws UnsupportedEncodingException, FirebaseException {
        // make the request
        String url = this.buildFullUrlFromRelativePath( path );
        HttpPatch request = new HttpPatch( url );
        request.setEntity( this.buildEntityFromJsonData( jsonData ) );
        HttpResponse httpResponse = this.makeRequest( request );

        // process the response
        FirebaseResponse response = this.processResponse( FirebaseRestMethod.PATCH, httpResponse );

        return response;
    }


    public FirebaseResponse put( Map<String, Object> data ) throws JacksonUtilityException, FirebaseException, UnsupportedEncodingException {
        return this.put( null, data );
    }

    /**
     * PUTs data to the provided-path relative to the base-url (ie: creates or overwrites).
     * If there is already data at the path, this data overwrites it.
     * If data is null/empty, any data existing at the path is deleted.
     *
     * @param path -- if null/empty, refers to base-url
     * @param data -- can be null/empty
     * @return {@link FirebaseResponse}
     * @throws UnsupportedEncodingException
     * @throws {@link JacksonUtilityException}
     * @throws {@link FirebaseException}
     */
    public FirebaseResponse put( String path, Map<String, Object> data ) throws JacksonUtilityException, FirebaseException, UnsupportedEncodingException {

        // make the request
        String url = this.buildFullUrlFromRelativePath( path );
        HttpPut request = new HttpPut( url );
        request.setEntity( this.buildEntityFromDataMap( data ) );
        HttpResponse httpResponse = this.makeRequest( request );

        // process the response
        FirebaseResponse response = this.processResponse( FirebaseRestMethod.PUT, httpResponse );

        return response;
    }

    /**
     * PUTs data to the provided-path relative to the base-url (ie: creates or overwrites).
     * If there is already data at the path, this data overwrites it.
     * If data is null/empty, any data existing at the path is deleted.
     *
     * @param jsonData -- can be null/empty
     * @return {@link FirebaseResponse}
     * @throws UnsupportedEncodingException
     * @throws {@link FirebaseException}
     */
    public FirebaseResponse put( String jsonData ) throws FirebaseException, UnsupportedEncodingException {
        return this.put( null, jsonData );
    }

    /**
     * PUTs data to the provided-path relative to the base-url (ie: creates or overwrites).
     * If there is already data at the path, this data overwrites it.
     * If data is null/empty, any data existing at the path is deleted.
     *
     * @param path -- if null/empty, refers to base-url
     * @param jsonData -- can be null/empty
     * @return {@link FirebaseResponse}
     * @throws UnsupportedEncodingException
     * @throws {@link FirebaseException}
     */
    public FirebaseResponse put( String path, String jsonData ) throws FirebaseException, UnsupportedEncodingException {

        // make the request
        String url = this.buildFullUrlFromRelativePath( path );
        HttpPut request = new HttpPut( url );
        request.setEntity( this.buildEntityFromJsonData( jsonData ) );
        HttpResponse httpResponse = this.makeRequest( request );

        // process the response
        FirebaseResponse response = this.processResponse( FirebaseRestMethod.PUT, httpResponse );

        return response;
    }

    /**
     * POSTs data to the base-url (ie: creates).
     *
     * NOTE: the FirebaseService API does not treat this method in the conventional way, but instead defines it
     * as 'PUSH'; the API will insert this data under the base-url but associated with a FirebaseService-
     * generated key; thus, every use of this method will result in a new insert even if the data already
     * exists.
     *
     * @param data -- can be null/empty but will result in no data being POSTed
     * @return {@link FirebaseResponse}
     * @throws UnsupportedEncodingException
     * @throws {@link JacksonUtilityException}
     * @throws {@link FirebaseException}
     */
    public FirebaseResponse post( Map<String, Object> data ) throws JacksonUtilityException, FirebaseException, UnsupportedEncodingException {
        return this.post( null, data );
    }

    /**
     * POSTs data to the provided-path relative to the base-url (ie: creates).
     *
     * NOTE: the FirebaseService API does not treat this method in the conventional way, but instead defines it
     * as 'PUSH'; the API will insert this data under the provided path but associated with a FirebaseService-
     * generated key; thus, every use of this method will result in a new insert even if the provided path
     * and data already exist.
     *
     * @param path -- if null/empty, refers to base-url
     * @param data -- can be null/empty but will result in no data being POSTed
     * @return {@link FirebaseResponse}
     * @throws UnsupportedEncodingException
     * @throws {@link JacksonUtilityException}
     * @throws {@link FirebaseException}
     */
    public FirebaseResponse post( String path, Map<String, Object> data ) throws JacksonUtilityException, FirebaseException, UnsupportedEncodingException {

        // make the request
        String url = this.buildFullUrlFromRelativePath( path );
        HttpPost request = new HttpPost( url );
        request.setEntity( this.buildEntityFromDataMap( data ) );
        HttpResponse httpResponse = this.makeRequest( request );

        // process the response
        FirebaseResponse response = this.processResponse( FirebaseRestMethod.POST, httpResponse );

        return response;
    }

    /**
     * POSTs data to the base-url (ie: creates).
     *
     * NOTE: the FirebaseService API does not treat this method in the conventional way, but instead defines it
     * as 'PUSH'; the API will insert this data under the base-url but associated with a FirebaseService-
     * generated key; thus, every use of this method will result in a new insert even if the provided data
     * already exists.
     *
     * @param jsonData -- can be null/empty but will result in no data being POSTed
     * @return {@link FirebaseResponse}
     * @throws UnsupportedEncodingException
     * @throws {@link FirebaseException}
     */
    public FirebaseResponse post( String jsonData ) throws FirebaseException, UnsupportedEncodingException {
        return this.post( null, jsonData );
    }

    /**
     * POSTs data to the provided-path relative to the base-url (ie: creates).
     *
     * NOTE: the FirebaseService API does not treat this method in the conventional way, but instead defines it
     * as 'PUSH'; the API will insert this data under the provided path but associated with a FirebaseService-
     * generated key; thus, every use of this method will result in a new insert even if the provided path
     * and data already exist.
     *
     * @param path -- if null/empty, refers to base-url
     * @param jsonData -- can be null/empty but will result in no data being POSTed
     * @return {@link FirebaseResponse}
     * @throws UnsupportedEncodingException
     * @throws {@link FirebaseException}
     */
    public FirebaseResponse post( String path, String jsonData ) throws FirebaseException, UnsupportedEncodingException {

        // make the request
        String url = this.buildFullUrlFromRelativePath( path );
        HttpPost request = new HttpPost( url );
        request.setEntity( this.buildEntityFromJsonData( jsonData ) );
        HttpResponse httpResponse = this.makeRequest( request );

        // process the response
        FirebaseResponse response = this.processResponse( FirebaseRestMethod.POST, httpResponse );

        return response;
    }

    /**
     * Append a query to the request.
     *
     * @param query -- Query string based on FirebaseService REST API
     * @param parameter -- Query parameter
     * @return FirebaseService -- return this FirebaseService object
     */

    public FirebaseService addQuery(String query, String parameter) {
        this.query.add(new BasicNameValuePair(query, parameter));
        return this;
    }

    /**
     * DELETEs data from the base-url.
     *
     * @return {@link FirebaseResponse}
     * @throws UnsupportedEncodingException
     * @throws {@link FirebaseException}
     */
    public FirebaseResponse delete() throws FirebaseException, UnsupportedEncodingException {
        return this.delete( null );
    }

    /**
     * DELETEs data from the provided-path relative to the base-url.
     *
     * @param path -- if null/empty, refers to the base-url
     * @return {@link FirebaseResponse}
     * @throws UnsupportedEncodingException
     * @throws {@link FirebaseException}
     */
    public FirebaseResponse delete( String path ) throws FirebaseException, UnsupportedEncodingException {

        // make the request
        String url = this.buildFullUrlFromRelativePath( path );
        HttpDelete request = new HttpDelete( url );
        HttpResponse httpResponse = this.makeRequest( request );

        // process the response
        FirebaseResponse response = this.processResponse( FirebaseRestMethod.DELETE, httpResponse );

        return response;
    }



///////////////////////////////////////////////////////////////////////////////
//
// PRIVATE API
//
///////////////////////////////////////////////////////////////////////////////


    private StringEntity buildEntityFromDataMap(Map<String, Object> dataMap ) throws FirebaseException, JacksonUtilityException {

        String jsonData = JacksonUtility.GET_JSON_STRING_FROM_MAP( dataMap );

        return this.buildEntityFromJsonData( jsonData );
    }

    private StringEntity buildEntityFromJsonData( String jsonData ) throws FirebaseException {

        StringEntity result = null;
        try {

            result = new StringEntity( jsonData , "UTF-8" );

        } catch( Throwable t ) {

            String msg = "unable to create entity from data; data was: " + jsonData;
            throw new FirebaseException( msg, t );

        }

        return result;
    }

    private String buildFullUrlFromRelativePath( String path ) throws UnsupportedEncodingException {

        // massage the path (whether it's null, empty, or not) into a full URL
        if( path == null ) {
            path = "";
        }
        path = path.trim();
        if( !path.isEmpty() && !path.startsWith( "/" ) ) {
            path = "/" + path;
        }

        String url = this.baseUrl + path;

        if (useJsonExt) url += FirebaseService.FIREBASE_API_JSON_EXTENSION;

        if(query != null) {
            url += "?";
            Iterator<NameValuePair> it = query.iterator();
            NameValuePair e;
            while(it.hasNext()) {
                e = it.next();
                url += e.getName() + "=" + URLEncoder.encode(e.getValue(), "UTF-8") + "&";
            }
        }

        if(secureToken != null) {
            if(query != null) {
                url += "access_token=" + secureToken;
            } else {
                url += "?access_token=" + secureToken;
            }
        }

        if(url.lastIndexOf("&") == url.length()) {
            StringBuilder str = new StringBuilder(url);
            str.deleteCharAt(str.length());
            url = str.toString();
        }


        return url;
    }


    private HttpResponse makeRequest(HttpRequestBase request ) throws FirebaseException {

        HttpResponse response = null;

        // sanity-check
        if( request == null ) {

            String msg = "request cannot be null";
            throw new FirebaseException( msg );
        }

        try {

            HttpClient client = new DefaultHttpClient();
            response = client.execute( request );

        } catch( Throwable t ) {

            String msg = "unable to receive response from request(" + request.getMethod() +  ") @ " + request.getURI();
            throw new FirebaseException( msg, t );

        }

        return response;
    }

    private FirebaseResponse processResponse( FirebaseRestMethod method, HttpResponse httpResponse ) throws FirebaseException {

        FirebaseResponse response = null;

        // sanity-checks
        if( method == null ) {

            String msg = "method cannot be null";
            throw new FirebaseException( msg );
        }
        if( httpResponse == null ) {

            String msg = "httpResponse cannot be null";

            throw new FirebaseException( msg );
        }

        // get the response-entity
        HttpEntity entity = httpResponse.getEntity();

        // get the response-code
        int code = httpResponse.getStatusLine().getStatusCode();

        // set the response-success
        boolean success = false;
        switch( method ) {
            case DELETE:
                if( httpResponse.getStatusLine().getStatusCode() == 204
                        && "No Content".equalsIgnoreCase( httpResponse.getStatusLine().getReasonPhrase() ) )
                {
                    success = true;
                }
                break;
            case PATCH:
            case PUT:
            case POST:
            case GET:
                if( httpResponse.getStatusLine().getStatusCode() == 200
                        && "OK".equalsIgnoreCase( httpResponse.getStatusLine().getReasonPhrase() ) )
                {
                    success = true;
                }
                break;
            default:
                break;

        }

        // get the response-body
        Writer writer = new StringWriter();
        if( entity != null ) {

            try {

                InputStream is = entity.getContent();
                char[] buffer = new char[1024];
                Reader reader = new BufferedReader( new InputStreamReader( is, "UTF-8" ) );
                int n;
                while( (n=reader.read(buffer)) != -1 ) {
                    writer.write( buffer, 0, n );
                }

            } catch( Throwable t ) {

                String msg = "unable to read response-content; read up to this point: '" + writer.toString() + "'";
                writer = new StringWriter(); // don't want to later give jackson partial JSON it might choke on
                throw new FirebaseException( msg, t );

            }
        }

        // convert response-body to map
        Map<String, Object> body = null;
        try {

            body = JacksonUtility.GET_JSON_STRING_AS_MAP( writer.toString() );

        } catch( JacksonUtilityException jue ) {

            String msg = "unable to convert response-body into map; response-body was: '" + writer.toString() + "'";
            throw new FirebaseException( msg, jue );
        }

        // build the response
        response = new FirebaseResponse( success, code, body, writer.toString() );

        //clear the query
        query.clear(); // query is only initialized in the constructor.

        return response;
    }



///////////////////////////////////////////////////////////////////////////////
//
// INTERNAL CLASSES
//
///////////////////////////////////////////////////////////////////////////////


    public enum FirebaseRestMethod {

        GET,
        PATCH,
        PUT,
        POST,
        DELETE;
    }
}
