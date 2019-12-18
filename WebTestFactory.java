import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class WebTestFactory {
  @Factory
  public Object[] createInstances() {
   Object[] result = new Object[10]; 
   for (int i = 0; i < 10; i++) {
      result[i] = new WebTest(i * 10);
    }
    return result;
  }

public class WebTest {
	  private int m_numberOfTimes;
	  public WebTest(int numberOfTimes) {
	    m_numberOfTimes = numberOfTimes;
	  }
	 
	  @Test
	  public void testServer() {
	   for (int i = 0; i < m_numberOfTimes; i++) {
	     System.out.println(i);
	    }
	  }
	}
}