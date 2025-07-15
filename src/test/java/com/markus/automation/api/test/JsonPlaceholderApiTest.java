package com.markus.automation.api.test; // 1. Package Declaration:
// This line declares the package that the JsonPlaceholderApiTest class belongs to.
// Packages help organize related classes and prevent naming conflicts in larger projects.
// This specific path (com.markus.automation.api.tests) should match your folder structure in src/test/java.

// 2. Import Statements:
// These lines import necessary classes from external libraries (Apache HttpClient, Jackson, JUnit)
// that your code uses. Without these, Java wouldn't know what classes like 'HttpGet' or 'Test' refer to.

// JUnit 5 imports: The testing framework used to structure and run tests.
import org.junit.jupiter.api.Test;             // @Test annotation marks a method as an executable test case.
import static org.junit.jupiter.api.Assertions.*; // Imports static assertion methods like assertEquals, assertTrue, etc.,
// so you can use them directly (e.g., assertEquals(...) instead of Assertions.assertEquals(...)).

// Apache HttpClient imports: The core library used for making HTTP requests.
import org.apache.hc.client5.http.classic.methods.HttpGet;  // Class representing an HTTP GET request.
import org.apache.hc.client5.http.classic.methods.HttpPost; // Class representing an HTTP POST request.
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient; // The main client interface for executing HTTP requests.
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse; // Represents the response received from an HTTP request.
import org.apache.hc.client5.http.impl.classic.HttpClients; // Utility class to create instances of CloseableHttpClient.
import org.apache.hc.core5.http.HttpEntity;       // Represents the content body of an HTTP message (request or response).
import org.apache.hc.core5.http.ParseException;   // An exception class for issues encountered while parsing HTTP messages.
import org.apache.hc.core5.http.io.entity.EntityUtils;    // A utility class for safely consuming (e.g., converting to a String)
// the content of an HttpEntity. It ensures the content stream is properly closed.
import org.apache.hc.core5.http.io.entity.StringEntity; // A specific HttpEntity implementation for creating request bodies from strings.
import org.apache.hc.core5.http.ContentType;     // Represents standard HTTP content types (e.g., application/json, text/plain).

// Jackson imports: A popular library for processing JSON data (serializing Java objects to JSON and deserializing JSON to Java objects).
import com.fasterxml.jackson.databind.JsonNode;   // Represents a node in a JSON tree, allowing easy navigation of JSON structures.
import com.fasterxml.jackson.databind.ObjectMapper; // The main class for Jackson, used to perform JSON serialization and deserialization.

import java.io.IOException; // Standard Java exception for input/output operations, often thrown during network communication.

public class JsonPlaceholderApiTest { // 3. Class Declaration:
    // This is the main class where your API test methods are defined.

    // 4. Class-level Constants and Instances:

    // BASE_URL: A constant string holding the common base URL for all API requests.
    // Using a constant makes it easy to change the target environment (e.g., from test to staging) later.
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    // objectMapper: An instance of Jackson's ObjectMapper. It's best practice to reuse this instance
    // because its creation can be computationally expensive. It's thread-safe.
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 5. Test Method: testGetPostById()
     * This method defines a specific test scenario for performing an HTTP GET request.
     * It retrieves a single post from the API and then verifies its content.
     */
    @Test // JUnit annotation: Marks this method as an executable test.
    // JUnit will automatically discover and run methods annotated with @Test.
    void testGetPostById() throws IOException, ParseException {
        // 5.1. API Endpoint Definition:
        // Constructs the full URL for the specific API resource (post with ID 1).
        String apiUrl = BASE_URL + "/posts/1";

        // 5.2. HTTP Client Initialization (try-with-resources):
        // CloseableHttpClient is the main component for sending requests.
        // The 'try-with-resources' statement ensures that the httpClient resource is automatically
        // closed when the try block finishes or an exception occurs, preventing resource leaks.
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 5.3. Create HTTP Request:
            // An HttpGet object is created, representing a GET request to the specified URL.
            HttpGet request = new HttpGet(apiUrl);

            // 5.4. Execute Request and Get Response (try-with-resources):
            // The httpClient executes the GET request.
            // CloseableHttpResponse represents the response from the server.
            // 'try-with-resources' also ensures the response stream is closed.
            try (CloseableHttpResponse response = httpClient.execute(request)) {

                // 5.5. Assertion: Verify HTTP Status Code.
                // response.getCode() retrieves the HTTP status code (e.g., 200, 404, 500).
                // assertEquals: A JUnit assertion that checks if two values are equal. If not, the test fails.
                int statusCode = response.getCode();
                System.out.println("GET Request Status Code: " + statusCode); // Prints status to console.
                assertEquals(200, statusCode, "Expected status code 200 OK for GET /posts/1"); // Asserts 200 OK for success.

                // 5.6. Get Response Body:
                // response.getEntity() retrieves the content body of the HTTP response.
                HttpEntity entity = response.getEntity();
                assertNotNull(entity, "Response entity should not be null"); // Asserts that a response body exists.

                // 5.7. Convert Response Body to String:
                // EntityUtils.toString(entity) safely converts the HttpEntity into a String (your raw JSON response).
                String responseBody = EntityUtils.toString(entity);
                System.out.println("GET Response Body:\n" + responseBody); // Prints the raw JSON to console.

                // 5.8. Parse JSON Response:
                // objectMapper.readTree(responseBody) parses the JSON string into a JsonNode tree.
                // This tree structure allows you to easily navigate and extract specific values from the JSON.
                JsonNode rootNode = objectMapper.readTree(responseBody);

                // 5.9. Assert Specific JSON Values:
                // rootNode.get("fieldName") accesses a field within the JSON.
                // .asInt() and .asText() convert the JSON node's value to a Java int or String.
                // These assertions verify that the retrieved data is correct and as expected.
                assertEquals(1, rootNode.get("id").asInt(), "ID should be 1");
                assertEquals(1, rootNode.get("userId").asInt(), "User ID should be 1");
                assertNotNull(rootNode.get("title"), "Title node should exist");
                assertTrue(rootNode.get("title").asText().length() > 0, "Title should not be empty");
                assertNotNull(rootNode.get("body"), "Body node should exist");
            }
        }
    }

    /**
     * 6. Test Method: testCreateNewPost()
     * This method defines a test scenario for performing an HTTP POST request.
     * It simulates creating a new post resource on the API and validates the creation.
     */
    @Test
    void testCreateNewPost() throws IOException, ParseException {
        // 6.1. API Endpoint Definition:
        // The URL for creating new posts is typically the collection URL.
        String apiUrl = BASE_URL + "/posts";

        // 6.2. Define Request Body:
        // The data to be sent in the POST request, formatted as a JSON string.
        String requestBody = "{\"title\":\"foo\",\"body\":\"bar\",\"userId\":1}";

        // 6.3. HTTP Client Initialization:
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 6.4. Create HTTP Request:
            // An HttpPost object is created for the specified URL.
            HttpPost request = new HttpPost(apiUrl);

            // 6.5. Set Request Body and Content Type:
            // request.setEntity: Attaches the JSON string as the request's body.
            // StringEntity: Converts the Java string into an HttpEntity.
            // ContentType.APPLICATION_JSON: Sets the 'Content-Type' HTTP header to indicate
            // that the body format is JSON. This is crucial for APIs to correctly interpret the data.
            request.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));

            // 6.6. Execute Request and Get Response:
            try (CloseableHttpResponse response = httpClient.execute(request)) {

                // 6.7. Assertion: Verify HTTP Status Code. 201 Created indicates successful resource creation.
                int statusCode = response.getCode();
                System.out.println("POST Request Status Code: " + statusCode);
                assertEquals(201, statusCode, "Expected status code 201 Created for POST /posts");

                // 6.8. Get Response Body:
                HttpEntity entity = response.getEntity();
                assertNotNull(entity, "Response entity should not be null");
                String responseBody = EntityUtils.toString(entity);
                System.out.println("POST Response Body:\n" + responseBody);

                // 6.9. Parse JSON Response and Assert Created Data:
                // The API typically returns the newly created resource, often with a generated ID.
                JsonNode rootNode = objectMapper.readTree(responseBody);
                assertEquals("foo", rootNode.get("title").asText(), "Title should be 'foo'");
                assertEquals("bar", rootNode.get("body").asText(), "Body should be 'bar'");
                assertEquals(1, rootNode.get("userId").asInt(), "User ID should be 1");
                assertNotNull(rootNode.get("id"), "New post should have been assigned an ID"); // Asserts that an ID was generated.
            }
        }
    }
}