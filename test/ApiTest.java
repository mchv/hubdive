import org.junit.Test;

import play.mvc.Http.Response;
import play.test.FunctionalTest;


public class ApiTest extends FunctionalTest {

    @Test
    public void testThatSearchApiWorks() {
        Response response = GET("/api/v1/json/repos/search/" + "java");
        assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }

    @Test
    public void testThatCommittersApiWorks() {
        Response response = GET("/api/v1/json/repos/committers/" + "torvalds" +"/" + "linux");
        assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }
   
    @Test
    public void testThatImpactApiWorks() {
        Response response = GET("/api/v1/json/repos/impact/" + "obeo" +"/" + "fr.obeo.performance" + "/" + "pcdavid");
        assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }

    
    @Test
    public void testThatStatsApiWorks() {
        Response response = GET("/api/v1/json/repos/stats/" + "torvalds" +"/" + "linux");
        assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }
	
}
