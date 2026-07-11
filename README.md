<p align="center">
<img src="https://www.davelee.de/common/assets/img/portfolio/personalman.webp" alt="PersonalMan" width="300" height="300">
</p>

<p align=center><a href="https://app.codacy.com/manual/dave_33/personalman?utm_source=github.com&utm_medium=referral&utm_content=daveajlee/personalman&utm_campaign=Badge_Grade_Dashboard"><img src="https://api.codacy.com/project/badge/Grade/14098397910b469084be11762d5c063d" alt="Codacy Badge"> </a>
</p>

PersonalMan is a open source employee management software for non-profit organisations or other companies. It provides management and self-service features in the areas of absences, overtime, trip and room bookings. In addition, it can note when employees have been paid but the payment and tax processes cannot be covered by PersonalMan. 

## Contents

| Folder | Description |
| --- | ----------- |
| desktop | Desktop Client for PersonalMan based on Electron and React for Windows, Linux and Mac OS. |
| docs | Current PersonalMan website with more information about the goals of the PersonalMan project. |
| server | This is the server application of PersonalMan with a RESTful API which any client can then implement in any programming language. |

## Server

### How to use

1.  To use the server you need to specify the user specific configuration parameters in .env
2.  Checkout the repository, change to the server folder and run the command: `npm run build`
3.  Start the server by executing this command: `NODE_ENV=production node dist/main.js`

### Documentation

Personalman comes with a Swagger API documentation which can be used to implement the API in a client on any programming language. The swagger user interface is available at: <http://your-personalman-server:your-port/swagger-ui>

## Desktop

The current architecture of PersonalMan allows multiple clients to be built - each client simply has to implement the API provided by the server. Currently there is a single client available for download: the PersonalMan Web & Desktop Client. This client implements the API in React and can be used in most modern web browsers which support JavaScript. Additional versions of this client are also supplied which run natively on desktop operating systems such as Windows, Linux and Mac by utilising the Electron framework.

## Current Limitations

* Absence management is available but the approval workflow for absences is not yet implemented.
* Overtime can be managed but there is not yet a management workflow for overtime analysis.
* Trip management is available but the approval workflow for trips is not yet implemented.
* Room bookings and departments / teams are not yet implemented.
