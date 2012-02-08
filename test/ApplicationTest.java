import java.util.HashMap;
import java.util.Map;

import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class ApplicationTest extends FunctionalTest {

    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }

    @Test
    public void testThatSearchPageWorks() {
    		Map<String, String> parameters = new HashMap<String, String>();
    		parameters.put("query", "play!");
        Response response = POST("/search/", parameters);
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }
    
    @Test
    public void testThatShowPageWorks() {
        Response response = GET("/project/ObeoNetwork/Viewpoint-documentation");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }
 
    
    public void testThatShowPageDisplayCommitters() {
        Response response = GET("/project/playframework/play");
        assertIsOk(response);
        assertContentMatch("178 committers", response);

    }
    
    
    
}