#!/bin/sh
set -e
brew install cocoapods
brew install node
cd ../../
npm install
cd ios
pod install