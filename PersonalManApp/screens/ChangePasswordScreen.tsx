/**
 * Show the change password screen with current password, new password and confirm password.
 */
import { useState } from 'react';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Alert, Appearance, Image, ScrollView, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useNavigation } from '@react-navigation/native';

type ChangePasswordScreenProps = {
  route: any;
}

type NavigationStackParams = {
  navigate: Function;
  setOptions: Function;
}

export default function ChangePasswordScreen({route}: ChangePasswordScreenProps) {

    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const colorScheme = Appearance.getColorScheme();
    
    const navigation = useNavigation<NavigationStackParams>();

    /**
     * Set the current password that the user entered.
     * @param {string} enteredText the text that the user entered in the current password field.
     */
    function currentPasswordInputHandler(enteredText: string) {
        setOldPassword(enteredText);
    }

    /**
     * Set the new password that the user entered.
     * @param {string} enteredText the text that the user entered in the new password field.
     */
    function newPasswordInputHandler(enteredText: string) {
        setNewPassword(enteredText);
    }

    /**
     * Set the confirm password that the user entered.
     * @param {string} enteredText the text that the user entered in the confirm password field.
     */
    function confirmPasswordInputHandler(enteredText: string) {
        setConfirmPassword(enteredText);
    }

    /**
     * Attempt to save the change of password with the credentials that were supplied by the user.
     */
    function saveHandler() {
        if ( newPassword != confirmPassword ) {
            Alert.alert('Please ensure that the new passwords match.');
        } else {
            fetch('http://' + route.params.server + ':8150/api/user/password', {
                method: 'PATCH',
                headers: {
                    'Authorization': `Bearer ${route.params.token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    company: "",
                    username: route.params.username,
                    currentPassword: oldPassword,
                    newPassword: newPassword
                })
            }).then(function (response) {
                navigation.navigate("MainMenuScreen", { username: route.params.username, token: route.params.token, server: route.params.server });
            }).catch(function (error) {
                    Alert.alert('Password could not be changed as an error occurred.');
                    console.log(error);
            });
        }
    }

    /**
     * Display the screen to the user.
     */
    return (
      <SafeAreaView style={styles.safeContainer}>
        <ScrollView contentContainerStyle={[styles.container, colorScheme === 'dark' ? styles.darkBackground : styles.lightBackground]}>
          <View style={styles.headerContainer}>
            <Text style={[styles.headerText, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>Change Password</Text>
          </View>
          <View style={styles.bodyContainer}>
            <View style={styles.oldPasswordContainer}>
                <Text style={[styles.bodyText, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>Current Password:</Text>
                <TextInput secureTextEntry={true} style={colorScheme === 'dark' ? styles.textInputDark : styles.textInputLight} placeholder='' onChangeText={currentPasswordInputHandler} value={oldPassword}/>
            </View>
            <View style={styles.newPasswordContainer}>
                <Text style={[styles.bodyText, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>New Password:</Text>
                <TextInput secureTextEntry={true} style={colorScheme === 'dark' ? styles.textInputDark : styles.textInputLight} placeholder='' onChangeText={newPasswordInputHandler} value={newPassword}/>
            </View>
            <View style={styles.confirmPasswordContainer}>
                <Text style={[styles.bodyText, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>Confirm Password:</Text>
                <TextInput secureTextEntry={true} style={colorScheme === 'dark' ? styles.textInputDark : styles.textInputLight} placeholder='' onChangeText={confirmPasswordInputHandler} value={confirmPassword}/>
            </View>
            <View style={styles.buttonContainer}>
                <TouchableOpacity style={styles.button} onPress={saveHandler}>
                    <Text style={styles.buttonText}>Save</Text>
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
    oldPasswordContainer: {
        flexDirection: 'column',
        width: '80%',
    },
    newPasswordContainer: {
        flexDirection: 'column',
        width: '80%',
        marginTop: 10
    },
    confirmPasswordContainer: {
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