# personalman
PersonalMan provides absence management for employees or volunteers. It is designed as a server application with a RESTful API which any client can then implement in any programming language.

**How to use**

1.  To use the server you need to specify the user specific configuration parameters in application.properties
2.  Create an executable jar using mvn clean install.
3.  Run the jar (for example in dev-test mode): java -Dspring.profiles.active=dev-test -jar personalman_server.jar

**How to use the server in your client**
*   Option 1: Personalman is a multi-module Maven project which also contains the API defined in Java. These classes can be used to write a Java client which talks with the Personalman server. To use these libraries you can simply define the API module as a maven dependency in your Java Client.
*   Option 2: Personalman comes with a Swagger API documentation which can be used to implement the API in a client on any programming language. The swagger user interface is available at: <http://your-personalman-server:your-port/swagger-ui.html>

**Available Profiles**
*   dev-test - This profile activates the swagger API documentation. This works well for development and testing.
