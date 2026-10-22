/**
 * Show the profile screen with user information and where the user can change their information.
 */
import { useState } from 'react';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Alert, Appearance, Image, Platform, ScrollView, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import DateTimePicker from '@react-native-community/datetimepicker';
import IconButton from '../components/IconButton';

type ProfileScreenProps = {
  route: any;
}

type NavigationStackParams = {
  navigate: Function;
  setOptions: Function;
}

export default function ProfileScreen({route}: ProfileScreenProps) {

    const [trainingCourse, setTrainingCourse] = useState('');
    const [hours, setHours] = useState('');
    const [date, setDate] = useState(new Date());
    const [showDateModal, setShowDateModal] = useState(false);

    const colorScheme = Appearance.getColorScheme();
    
    const navigation = useNavigation<NavigationStackParams>();

    /**
     * Set the training course that the user entered.
     * @param {string} enteredText the text that the user entered in the training course field.
     */
    function trainingCourseInputHandler(enteredText: string) {
        setTrainingCourse(enteredText);
    }

    function openDateModalAndroid() {
        setShowDateModal(true);
    }

    function showDateAndroid() {
        return date.getDate() + "-" + convertToTwoDigits(date.getMonth()+1) + "-" + date.getFullYear();
    }

    function convertToTwoDigits(value: number) {
        return ( value > 9 ) ? value : "0" + value;
    }

    /**
     * Set the hours that the user entered.
     * @param {string} enteredText the text that the user entered in the hours field.
     */
    function timesheetInputHandler(enteredText: string) {
        setHours(enteredText);
    }

    /**
     * Attempt to add the training course that was supplied by the user.
     */
    function addTrainingCourseHandler() {
        fetch('http://' + route.params.server + ':8150/api/user/training', {
            method: 'PATCH',
            headers: {
                'Authorization': `Bearer ${route.params.token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                company: "",
                username: route.params.username,
                trainingCourse: trainingCourse,
            })
        }).then(function (response) {
            navigation.navigate("MainMenuScreen", { username: route.params.username, token: route.params.token, server: route.params.server });
        }).catch(function (error) {
            Alert.alert('Training Course could not be saved as an error occurred.');
            console.log(error);
        });
    }

    /**
     * Attempt to add the timesheet that was supplied by the user.
     */
    function addTimesheetHandler() {
        var hoursNum = parseInt(hours);
        if ( isNaN(hoursNum) || hoursNum < 1 || hoursNum > 24 ) {
            Alert.alert('Please enter a valid number between 1 and 24');
            return;
        }
        fetch('http://' + route.params.server + ':8150/api/user/timesheet', {
            method: 'PATCH',
            headers: {
                'Authorization': `Bearer ${route.params.token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                company: "",
                username: route.params.username,
                date: convertDateFormat(),
                hours: hoursNum
            })
        }).then(function (response) {
            if ( response.status === 200 ) {
                navigation.navigate("MainMenuScreen", { username: route.params.username, token: route.params.token, server: route.params.server });
            }
        }).catch(function (error) {
            Alert.alert('Timesheet could not be saved. Please try again later.');
        });
    }

    function convertDateFormat() {
        return addZeroPrefix(date.getDate()) + "-" + addZeroPrefix((date.getMonth()+1)) + "-" + date.getFullYear();
    }

    function addZeroPrefix(num: number) {
        return num < 10 ? "0" + num : num;
    }

    /**
     * Display the screen to the user.
     */
    return (
      <SafeAreaView style={styles.safeContainer}>
        <ScrollView contentContainerStyle={[styles.container, colorScheme === 'dark' ? styles.darkBackground : styles.lightBackground]}>
          <View style={styles.headerContainer}>
            <Text style={[styles.headerText, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>User Profile</Text>
          </View>
          <View style={styles.bodyContainer}>
            <View style={styles.trainingCourseContainer}>
                <Text style={[styles.bodyText, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>Course:</Text>
                <TextInput style={colorScheme === 'dark' ? styles.textInputDark : styles.textInputLight} placeholder='' onChangeText={trainingCourseInputHandler} value={trainingCourse}/>
            </View>
            <View style={styles.buttonContainer}>
                <TouchableOpacity style={styles.button} onPress={addTrainingCourseHandler}>
                    <Text style={styles.buttonText}>Save</Text>
                </TouchableOpacity>
            </View>
          </View>
          <View style={styles.bodyContainer}>
            <View style={styles.trainingCourseContainer}>
                <Text style={[styles.bodyText, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>Date:</Text>
                {Platform.OS === 'ios' && <View style={[styles.dateTimePickerEntry]}>
                    <DateTimePicker
                        testID="dateTimePicker"
                        value={date}
                        mode="date"
                        is24Hour={true}
                        onValueChange={(event, selectedDate) => setDate(selectedDate)}
                        onDismiss={() => setShowDateModal(false)}
                    />
                </View>}
                {Platform.OS === 'android' && 
                    <>
                        <Text style={[styles.entryText]}>{showDateAndroid()}</Text>
                        <IconButton icon="calendar-number-outline" size={24} color="black" onPress={openDateModalAndroid}/>
                        {showDateModal && <DateTimePicker
                            testID="dateTimePicker"
                            value={date}
                            mode="date"
                            is24Hour={true}
                            onValueChange={(event, selectedDate) => { setShowDateModal(false); setDate(selectedDate); }}
                            onDismiss={() => setShowDateModal(false)}
                        />}
                    </>
                }
            </View>
            <View style={styles.trainingCourseContainer}>
                <Text style={[styles.bodyText, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>Hours:</Text>
                <TextInput style={colorScheme === 'dark' ? styles.textInputDark : styles.textInputLight} placeholder='' onChangeText={timesheetInputHandler} value={hours}/>
            </View>
            <View style={styles.buttonContainer}>
                <TouchableOpacity style={styles.button} onPress={addTimesheetHandler}>
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
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
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
    headerContainer: {
        paddingTop: 30
    },
    headerText: {
        fontSize: 32,
        fontWeight: 'bold',
        textAlign: 'center'
    },
    bodyContainer: {
        paddingTop: 20,
        width: '100%',
        alignItems: 'center',
        justifyContent: 'center'
    },
    trainingCourseContainer: {
        flexDirection: 'row',
        width: '80%',
    },
    bodyText: {
        fontSize: 20,
        fontWeight: 'bold',
        textAlign: 'center',
        paddingBottom: 8,
        paddingRight: 8
    },
    textInputLight: {
        borderWidth: 1,
        borderColor: '#e4d0ff',
        backgroundColor: 'white',
        color: '#120438',
        borderRadius: 6,
        width: '70%',
        padding: 8
    },
    textInputDark: {
        borderWidth: 1,
        borderColor: 'white',
        backgroundColor: 'black',
        color: 'white',
        borderRadius: 6,
        width: '70%',
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
    entryText: {
        paddingLeft: 10,
        fontSize: 18,
        width: '40%',
        textAlign: 'right',
        marginRight: 5
    },
    dateTimePickerEntry: {
        paddingLeft: 10,
        fontSize: 18,
        width: '50%',
        alignItems: 'flex-end',
        textAlign: 'right'
    },
})