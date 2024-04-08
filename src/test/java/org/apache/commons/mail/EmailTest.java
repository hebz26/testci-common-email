package org.apache.commons.mail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import java.util.Date;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailTest {
    
    private static final String[] TEST_EMAILS = { "ab@bc.com", "a.b@c.org",
    "abcdefghijkInnopqrst@abcdefghijklmnopqrst.com.bd" };
    private EmailConcrete email;
    private static final String TEST_EMAIL = "replyto@example.com";
    private static final String TEST_NAME = "John Doe";
    private static final int TIME_OUT = 5000; // 5 seconds
	private static final String MAIL_HOST = "coolhostname";
    private Session session;
    
    @Before
    public void setUpEmailTest() throws Exception {
        email = new EmailConcrete();
          
    }
    
    @After
    public void tearDownEmailTest() throws Exception {
    	 // Reset objects
        email = null; 
    }
    
    /*
     * 
     * 
     * TEST Method: addBcc(String...emails)
     * 
     * 
     * 
     */
    
    @Test
    public void testAddBcc() throws Exception{
        
    	//adding and checking for three emails
    	email.addBcc(TEST_EMAILS);
        assertEquals(3, email.getBccAddresses().size());
    }
    
    /*
     * 
     * 
     * 
     * TEST Method: addCc(String email)
     * 
     * 
     * 
     */
    
     @Test
        public void testAddCc() throws Exception{
    	 
    	//adding and checking for one email
            email.addCc(TEST_EMAIL);
            assertEquals(1, email.getCcAddresses().size());
        }
     @Test
     public void testAddCc3() throws Exception{
 	 
 	//adding and checking for 3 emails
         email.addCc(TEST_EMAILS);
         assertEquals(3, email.getCcAddresses().size());
     }
     
     /*
      * 
      * 
      * 
      * TEST Method: addHeader(String name, String value)
      * 
      * 
      * 
      */
     
     //testing that headers are added correctly
      @Test
        public void testAddHeader() throws Exception {
 
           email.addHeader("Fake-Header", "text");
           email.addHeader("Fake-Header2", "number");
         
            assertEquals(2, email.getHeaders().size());
            assertEquals("text", email.getHeaders().get("Fake-Header"));
            assertEquals("number", email.getHeaders().get("Fake-Header2"));
        }
      
      	//testing addHeader with null name, should throw IllegalArgumentException
        @Test(expected = IllegalArgumentException.class)
        public void testAddHeaderWithNullName() throws Exception{

            email.addHeader(null, "value");
        }
        
      //testing addHeader with null value, should throw IllegalArgumentException
        @Test(expected = IllegalArgumentException.class)
        public void testAddHeaderWithNullValue() throws Exception{

            email.addHeader("Fake-Header", null);
            
        }
        
        /*
         * 
         * 
         * 
         * TEST Method: addReplyTo(String email, String name)
         * 
         * 
         * 
         */
        
      //testing that email and name are added correctly
        @Test
        public void testAddReplyTo() throws Exception{
            email.addReplyTo(TEST_EMAIL, TEST_NAME);
            
            assertEquals(1, email.getReplyToAddresses().size());
            assertEquals(TEST_EMAIL, email.getReplyToAddresses().get(0).getAddress());
            assertEquals(TEST_NAME, email.getReplyToAddresses().get(0).getPersonal());
        }
        
        /*
         * 
         * 
         * 
         * TEST Method: buildMimeMessage()
         * 
         * 
         * 
         */
        
        //testing that mime message is built w/correct content
        @Test
        public void testBuildMimeMessage() throws Exception {
            // Set up email data
        	session = Session.getDefaultInstance(System.getProperties());
       
        email.setMailSession(session);
            email.setSubject("Test Subject");
            email.setContent("Test Content", EmailConstants.TEXT_PLAIN);
            email.setFrom("from@example.com");
            email.addTo("to@example.com");
            email.addCc("cc@example.com");
            email.addBcc("bcc@example.com");
            email.addHeader("X-Custom-Header", "Value");


            email.buildMimeMessage();

            // Verify results
            assertNotNull(email.getMimeMessage());

           
        }
        
        //testing that mime message is built w/html content
        @Test
        public void testBuildMimeMessageWithHtmlContent() throws Exception {
            // Set up email data with HTML content
        	session = Session.getDefaultInstance(System.getProperties());
            
            email.setMailSession(session);
            email.setSubject("Test Subject with HTML Content");
            email.setContent("<html><body><h1>Hello</h1></body></html>", EmailConstants.TEXT_HTML);
            email.setFrom("from@example.com");
            email.addTo("to@example.com");
            email.addCc("cc@example.com");
            email.addBcc("bcc@example.com");

            // Invoke the method
            email.buildMimeMessage();

            // Verify results
            assertNotNull(email.getMimeMessage());

            
        }
        
        //testing that exception thrown when there is no from address
        @Test(expected = EmailException.class)
        public void testBuildMimeMessageNoFromAddress() throws EmailException {
        	session = Session.getDefaultInstance(System.getProperties());
            
            email.setMailSession(session);
            email.setSubject("Test Subject");
            email.setContent("Test Content", EmailConstants.TEXT_PLAIN);
            email.addTo("to@example.com");
            email.addCc("cc@example.com");
            email.addBcc("bcc@example.com");

            email.buildMimeMessage(); // Should throw EmailException
        }
        
        /*
         * 
         * 
         * 
         * TEST Method: getHostName()
         * 
         * 
         * 
         */
        
        //testing set and get host name
        @Test
        public void testGetHostName() throws Exception {
           
            email.setHostName(MAIL_HOST);
            assertEquals(MAIL_HOST, email.getHostName());
        }
        
        //testing getHostName w/session
        @Test
        public void testGetHostNameWithSessionProperty() throws Exception {
            // Set up a session with properties
            Properties properties = new Properties();
            properties.setProperty(EmailConstants.MAIL_HOST, TEST_EMAIL);
            Session session = Session.getInstance(properties);

            email.setMailSession(session);

            // assert that getHostName returns the session property value
            assertEquals(TEST_EMAIL, email.getHostName());
        }
        
        //testing getHostName w/no session, no host
        @Test
        public void testGetHostNameNoSessionNoHost() throws Exception {
           
            assertNull(email.getHostName());
        }
        
        /*
         * 
         * 
         * 
         * TEST Method: getMailSession()
         * 
         * 
         * 
         */
        

        @Test
        public void testGetMailSession() throws EmailException {
            // Set up a session
            Properties properties = new Properties();
            properties.setProperty(EmailConstants.MAIL_HOST, TEST_EMAIL);
            Session session = Session.getInstance(properties);
            email.setMailSession(session);

            Session retrievedSession = email.getMailSession();

            // verify that the retrieved session is the same as the mock session
            assertEquals(session, retrievedSession);
        }
        
        @Test
        public void testGetMailSessionwithHost() throws EmailException {

            email.setHostName(MAIL_HOST);
            Session retrievedSession = email.getMailSession();

            // Verify that the retrieved session is not null
            assertNotNull(retrievedSession);

        }
        
        @Test (expected = EmailException.class)
        public void testGetMailSession_InvalidHostname() throws EmailException {
            // Set up invalid host name
            email.setHostName(null);

            // Try to get mail session
            email.getMailSession();
        } 
 
        /*
         * 
         * 
         * 
         * TEST Method: getSentDate()
         * 
         * 
         * 
         */
       
        @Test
        public void testGetSentDate() throws Exception {

            Date sentDate = email.getSentDate();
            assertNotNull(sentDate);
        }
        
        @Test
        public void testGetSentDateWhenSentDateIsNull() throws Exception {
            email.setSentDate(null);
            Date currentDate = new Date();
            assertEquals(currentDate, email.getSentDate());
        }
        
        @Test
        public void testGetSentDateWhenSentDateIsNotNull() throws Exception {
            Date sentDate = new Date(); // Set some specific date
            email.setSentDate(sentDate);
            assertEquals(sentDate, email.getSentDate());
        }
        
        /*
         * 
         * 
         * 
         * TEST Method:   getSocketConnectionTimeout()
         * 
         * 
         * 
         */
       
        @Test
        public void testGetSocketConnectionTimeout() throws Exception {
    
            email.setSocketConnectionTimeout(TIME_OUT );
            
            assertEquals(TIME_OUT , email.getSocketConnectionTimeout());
        }
        
        /*
         * 
         * 
         * 
         * TEST Method: setFrom(String email)
         * 
         * 
         * 
         */
      

        @Test
        public void testSetFromWithEmail() throws EmailException {

            email.setFrom(TEST_EMAIL);
            assertEquals(TEST_EMAIL, email.getFromAddress().getAddress());
            assertEquals(null, email.getFromAddress().getPersonal());
        }
  
    }
 
     
