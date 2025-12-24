/**
 * Show the login screen with username, password, activation code and register.
 */
import { useState } from 'react';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Appearance, Image, ScrollView, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useNavigation } from '@react-navigation/native';

type NavigationStackParams = {
  navigate: Function;
  setOptions: Function;
}

export default function LoginScreen() {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [activationCode, setActivationCode] = useState('');

    const colorScheme = Appearance.getColorScheme();

    const logoImage = require('./../assets/images/logo-1024.png');
    
    const navigation = useNavigation<NavigationStackParams>();

    /**
     * Set the username that the user entered.
     * @param {string} enteredText the text that the user entered in the username field.
     */
    function usernameInputHandler(enteredText: string) {
        setUsername(enteredText);
    }

    /**
     * Set the password that the user entered.
     * @param {string} enteredText the text that the user entered in the password field.
     */
    function passwordInputHandler(enteredText: string) {
        setPassword(enteredText);
    }

    /**
     * Set the activation code that the user entered.
     * @param {string} enteredText the text that the user entered in the activation code field.
     */
    function activationCodeInputHandler(enteredText: string) {
        setActivationCode(enteredText);
    }

    /**
     * Attempt to login the user with the credentials supplied.
     */
    function loginHandler() {
        navigation.navigate("MainMenuScreen", { username: username });
    }

    /**
     * Attempt to activate the user with the code supplied.
     */
    function activateHandler() {
        console.log('Activate button pressed');
    }

    /**
     * Display the screen to the user.
     */
    return (
      <SafeAreaView style={styles.safeContainer}>
        <ScrollView contentContainerStyle={[styles.container, colorScheme === 'dark' ? styles.darkBackground : styles.lightBackground]}>
          <Image
            style={styles.logo}
            source={logoImage}
          />
          <View style={styles.headerContainer}>
            <Text style={[styles.headerText, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>Login</Text>
          </View>
          <View style={styles.bodyContainer}>
            <View style={styles.usernameContainer}>
                <Text style={[styles.bodyText, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>Username:</Text>
                <TextInput style={colorScheme === 'dark' ? styles.textInputDark : styles.textInputLight} placeholder='Your Username' onChangeText={usernameInputHandler} value={username}/>
            </View>
            <View style={styles.passwordContainer}>
                <Text style={[styles.bodyText, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>Password:</Text>
                <TextInput secureTextEntry={true} style={colorScheme === 'dark' ? styles.textInputDark : styles.textInputLight} placeholder='' onChangeText={passwordInputHandler} value={password}/>
            </View>
            <View style={styles.buttonContainer}>
                <TouchableOpacity style={styles.button} onPress={loginHandler}>
                    <Text style={styles.buttonText}>Login</Text>
                </TouchableOpacity>
            </View>
            <View style={styles.headerContainer}>
                <Text style={[styles.headerText, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>OR Activate Account</Text>
            </View>
            <View style={styles.activationContainer}>
                <Text style={[styles.bodyText, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>Activation Code:</Text>
                <TextInput style={colorScheme === 'dark' ? styles.textInputDark : styles.textInputLight} placeholder='Your Activation Code' onChangeText={activationCodeInputHandler} value={activationCode}/>
            </View>
            <View style={styles.buttonContainer}>
                <TouchableOpacity style={styles.button} onPress={activateHandler}>
                    <Text style={styles.buttonText}>Activate / Register</Text>
                </TouchableOpacity>
            </View>
          </View>
        </ScrollView>
      </SafeAreaView>
    )

}

const styles = StyleSheet.create({
    safeContainer: {
        flex: 1,
    },
    logo: {
        marginTop: 10, 
        width: 128, 
        height: 128
    },
    darkBackground: {
        backgroundColor: 'black',
    },
    lightBackground: {
        backgroundColor: '#F0F0F0',
    },
    darkText: {
        color: 'white'
    },
    lightText: {
        color: 'black'
    },
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    },
    headerContainer: {
        paddingTop: 30
    },
    bodyContainer: {
        paddingTop: 20,
        width: '100%',
        alignItems: 'center',
        justifyContent: 'center'
    },
    usernameContainer: {
        flexDirection: 'column',
        width: '80%',
    },
    passwordContainer: {
        flexDirection: 'column',
        width: '80%',
        marginTop: 10
    },
    activationContainer: {
        flexDirection: 'column',
        width: '80%',
        marginTop: 10
    },
    headerText: {
        fontSize: 32,
        fontWeight: 'bold',
        textAlign: 'center'
    },
    bodyText: {
        fontSize: 20,
        fontWeight: 'bold',
        textAlign: 'center',
        paddingBottom: 16
    },
    textInputLight: {
        borderWidth: 1,
        borderColor: '#e4d0ff',
        backgroundColor: 'white',
        color: '#120438',
        borderRadius: 6,
        width: '100%',
        padding: 8
    },
    textInputDark: {
        borderWidth: 1,
        borderColor: 'white',
        backgroundColor: 'black',
        color: 'white',
        borderRadius: 6,
        width: '100%',
        padding: 8
    },
    buttonContainer: {
        marginTop: 20,
        flexDirection: 'row'
    },
    button: {
        alignItems: "center",
        backgroundColor: "#185f92ff",
        width: '90%',
        padding: 10,
        marginBottom: 30,
        borderRadius: 50
    },
    buttonText: {
        color: 'white',
        fontSize: 20,
        fontWeight: 'bold',
        textAlign: 'center',
    },
});