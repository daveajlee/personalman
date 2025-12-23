/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import {
  SafeAreaProvider,
} from 'react-native-safe-area-context';
import LoginScreen from './screens/LoginScreen';

function App() {

  return (
    <SafeAreaProvider>
      <LoginScreen/>
    </SafeAreaProvider>
  );
}

export default App;
