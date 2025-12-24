import { Appearance, StyleSheet, Text, TouchableOpacity, View } from "react-native";

type MainMenuScreenProps = {
  route: any;
}

export default function MainMenuScreen({route}: MainMenuScreenProps) {

    const colorScheme = Appearance.getColorScheme();

    function onAddAbsencePress() {
        console.log('Add Absence button pressed');
    }

    function onViewStatisticsPress() {
        console.log('View Statistics button pressed');
    }

    return (
        <View style={[styles.container, colorScheme === 'dark' ? styles.darkBackground : styles.lightBackground]}>
            <View style={styles.headerContainer}>
                <Text style={[styles.header, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>Welcome, {route.params.username}</Text>
            </View>
            <View style={styles.bodyContainer}>
                <Text style={[styles.header, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>01.01.2025 - Public Holiday</Text>
                <Text style={[styles.header, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>01.06.2025 to 15.06.2025 - Holiday</Text>
                <Text style={[styles.header, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>15.11.2025 to 19.11.2025 - Illness</Text>
                <Text style={[styles.header, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>25.12.2025 - Public Holiday</Text>
                <View style={styles.buttonContainer}>
                    <TouchableOpacity style={styles.button} onPress={onAddAbsencePress}>
                        <Text style={styles.buttonText}>Add Absence</Text>
                    </TouchableOpacity>
                </View>
                <TouchableOpacity style={styles.button} onPress={onViewStatisticsPress}>
                    <Text style={styles.buttonText}>View Statistics</Text>
                </TouchableOpacity>
            </View>
        </View>
    );

}

const styles = StyleSheet.create({
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
        paddingTop: 100
    },
    header: {
        fontSize: 24,
        fontWeight: 'bold',
        textAlign: 'center'
    },
    bodyContainer: {
        flex: 4,
        width: '100%',
        alignItems: 'center',
        marginTop: 30
    },
    buttonContainer: {
        marginTop: 50,
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
        fontSize: 18,
        fontWeight: 'bold',
        textAlign: 'center'
    }
})