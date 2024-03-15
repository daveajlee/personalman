# PersonalMan Desktop Client (Electron)

This is a Desktop Client for PersonalMan which provides absence management for employees or volunteers. Please note that you need a running PersonalMan Server to use this client! The client is based on React & Electron. 
Both Windows and Mac OS are the supported operating systems.

## Using the client

*   First of all, you need a running PersonalMan server - this can be done either locally or remote. Please follow the instructions in the personalman repository (<https://github.com/daveajlee/personalman/server>).
*   You can run the client by cloning this repository and running the following command: `npm run electron`
*   The PersonalMan server can be configured by editing the .env file before starting the Electron application.

## Viewing the source

*   Clone the git branch and import the project in your favourite IDE.
*   The project can be built via `npm run build`.
*   The application is sorted into individual components. An overview of the components can be found via the styleguide: `npm run styleguide` (this is generated using Reactstyleguidist (https://github.com/styleguidist/react-styleguidist/)
*   Dependencies are managed via the package.json file and NPM (Node 18 is required).
*   Unit tests can be run via `npm run test` (interactive watch mode) or `npm run test:nowatch` (single run with no watch mode).

## Create Windows Electron Binary on Mac

* Wine Cask must be installed via HomeBrew: `brew install --cask wine-stable`

## Limitations of the client

*   I cannot accept any warranty for loss of data through using this client or server.
*   Currently the client is only available in English and German.
