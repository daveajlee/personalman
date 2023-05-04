<p align="center">
<img src="https://www.davelee.de/common/assets/img/portfolio/personalman-logo.png" alt="PersonalMan" width="300" height="300">
</p>

<p align=center><a href="https://app.codacy.com/manual/dave_33/personalman?utm_source=github.com&utm_medium=referral&utm_content=daveajlee/personalman&utm_campaign=Badge_Grade_Dashboard"><img src="https://api.codacy.com/project/badge/Grade/14098397910b469084be11762d5c063d" alt="Codacy Badge"> </a>
</p>

PersonalMan is a open source employee management software for non-profit organisations or other companies. It provides management and self-service features in the areas of absences, overtime, trip and room bookings. In addition, it can note when employees have been paid but the payment and tax processes cannot be covered by PersonalMan. 

## Contents

| Folder | Description |
| --- | ----------- |
| desktop | Future Desktop Client for PersonalMan based on Electron and React for Windows, Linux and Mac OS. (Currently being developed and not yet feature complete). Please use the old Java Desktop Client instead: https://github.com/daveajlee/personalman_desktop_client |
| docs | Current PersonalMan website with more information about the goals of the PersonalMan project. |
| java-api | The current API of PersonalMan in the Java language. This can be used to write a Java client which talks with the Personalman server. To use these libraries you can simply define the API module as a maven dependency in your Java Client. |
| server | This is the server application of PersonalMan with a RESTful API which any client can then implement in any programming language. |

## Server

### How to use

1.  To use the server you need to specify the user specific configuration parameters in application.properties
2.  Create an executable jar using mvn clean install.
3.  Run the jar (for example in dev-test mode): java -Dspring.profiles.active=dev-test -jar personalman_server.jar

### Documentation

Personalman comes with a Swagger API documentation which can be used to implement the API in a client on any programming language. The swagger user interface is available at: <http://your-personalman-server:your-port/swagger-ui.html>

### Available Profiles
*   dev-test - This profile activates the swagger API documentation. This works well for development and testing.

## Desktop

Since the future desktop client based on Electron is still being developed, the old Java Desktop Client can be used instead: https://github.com/daveajlee/personalman_desktop_client

## Current Limitations

* Absence management is available but the approval workflow for absences is not yet implemented.
* Overtime can be managed but there is not yet a management workflow for overtime analysis.
* Trip management is available but the approval workflow for trips is not yet implemented.
* Room bookings and departments / teams are not yet implemented.
