package firebase.service;

import java.util.LinkedHashMap;
import java.util.Map;

public class FirebaseResponse {


///////////////////////////////////////////////////////////////////////////////
//
// PROPERTIES & CONSTRUCTORS
//
///////////////////////////////////////////////////////////////////////////////

    private final boolean success;
    private final int code;
    private final Map<String, Object> body;
    private final String rawBody;

    public FirebaseResponse( boolean success, int code, Map<String, Object> body, String rawBody ) {

        this.success = success;
        this.code = code;

        if( body == null ) {
            body = new LinkedHashMap<String, Object>();
        }
        this.body = body;

        if( rawBody == null ) {
            rawBody = new String();
        }
        this.rawBody = rawBody.trim();
    }



///////////////////////////////////////////////////////////////////////////////
//
// PUBLIC API
//
///////////////////////////////////////////////////////////////////////////////


    /**
     * Returns whether or not the response from the FirebaseService-client was successful
     *
     * @return true if response from the FirebaseService-client was successful
     */
    public boolean getSuccess() {
        return this.success;
    }

    /**
     * Returns the HTTP status code returned from the FirebaseService-client
     *
     * @return an integer representing an HTTP status code
     */
    public int getCode() {
        return this.code;
    }

    /**
     * Returns a map of the data returned by the FirebaseService-client
     *
     * @return a map of Strings to Objects
     */
    public Map<String, Object> getBody() {
        return this.body;
    }

    /**
     * Returns the raw data response returned by the FirebaseService-client
     *
     * @return a String of the JSON-response from the client
     */
    public String getRawBody() {
        return this.rawBody;
    }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();

        result.append( FirebaseResponse.class.getSimpleName() + "[ " )
                .append( "(Success:" ).append( this.success ).append( ") " )
                .append( "(Code:" ).append( this.code ).append( ") " )
                .append( "(Body:" ).append( this.body ).append( ") " )
                .append( "(Raw-body:" ).append( this.rawBody ).append( ") " )
                .append( "]" );

        return result.toString();
    }
}
