/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import {
  SafeAreaProvider,
} from 'react-native-safe-area-context';
import AbsenceScreen from './screens/AbsenceScreen';
import LoginScreen from './screens/LoginScreen';
import MainMenuScreen from './screens/MainMenuScreen';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { NavigationContainer, DefaultTheme, DarkTheme } from '@react-navigation/native';
import { Appearance } from 'react-native';

// Define stack navigation
const Stack = createNativeStackNavigator();

const colorScheme = Appearance.getColorScheme();

const MyDefaultTheme = {
  ...DefaultTheme,
  colors: {
    ...DefaultTheme.colors,
    background: '#F0F0F0',
    primary: 'black',
  },
};

const MyDarkTheme = {
  ...DarkTheme,
  colors: {
    ...DarkTheme.colors,
    background: 'black',
    primary: 'white',
  },
};

function App() {

  return (
    <SafeAreaProvider>
      <NavigationContainer theme={colorScheme === 'dark' ? MyDarkTheme : MyDefaultTheme}>
        <Stack.Navigator initialRouteName='LoginScreen'>
          <Stack.Screen name="LoginScreen" component={LoginScreen} options={() => ({
            headerShown: false
          })}/>
          <Stack.Screen name="MainMenuScreen" component={MainMenuScreen} options={{
          title: 'PersonalMan',
          headerBackVisible: false,
          }}/>
          <Stack.Screen name="AbsenceScreen" component={AbsenceScreen} options={{
          title: 'Absences',
          }}/>
        </Stack.Navigator>
      </NavigationContainer>
    </SafeAreaProvider>
  );
}

export default App;
