package de.davelee.personalman.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the constructor, getter and setter methods of the <code>AddTrainingRequest</code> class.
 */
public class AddTrainingRequestTest {

    @Test
    /**
     * Test the constructor and ensure variables are set together using the getter methods.
     */
    public void testConstructor() {
        AddTrainingRequest addTrainingRequest = new AddTrainingRequest("MyCompany", "dlee", "dlee-ghgkg", "Certified Java Developer");
        assertEquals("MyCompany", addTrainingRequest.getCompany());
        assertEquals("dlee", addTrainingRequest.getUsername());
        assertEquals("dlee-ghgkg", addTrainingRequest.getToken());
        assertEquals("Certified Java Developer", addTrainingRequest.getTrainingCourse());
    }

    @Test
    /**
     * Test the builder and ensure variables are set together using the getter methods.
     */
    public void testBuilder() {
        AddTrainingRequest addTrainingRequest = AddTrainingRequest.builder()
                .company("MyCompany")
                .username("dlee")
                .token("dlee-ghgkg")
                .trainingCourse("Certified Java Developer")
                .build();
        assertEquals("MyCompany", addTrainingRequest.getCompany());
        assertEquals("dlee", addTrainingRequest.getUsername());
        assertEquals("dlee-ghgkg", addTrainingRequest.getToken());
        assertEquals("Certified Java Developer", addTrainingRequest.getTrainingCourse());
    }

    @Test
    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    public void testSettersAndGetters() {
        AddTrainingRequest addTrainingRequest = new AddTrainingRequest();
        addTrainingRequest.setCompany("MyCompany");
        assertEquals("MyCompany", addTrainingRequest.getCompany());
        addTrainingRequest.setUsername("dlee");
        assertEquals("dlee", addTrainingRequest.getUsername());
        addTrainingRequest.setToken("dlee-ghgkg");
        assertEquals("dlee-ghgkg", addTrainingRequest.getToken());
        addTrainingRequest.setTrainingCourse("Certified Java Developer");
        assertEquals("Certified Java Developer", addTrainingRequest.getTrainingCourse());
    }

}
