import { Alert, Appearance, StyleSheet, Text, TouchableOpacity, View } from "react-native";
import { useEffect } from "react";
import { useNavigation } from "@react-navigation/native";
import IconButton from "../components/IconButton";

type AbsenceScreenProps = {
    route: any;
}

type NavigationStackParams = {
  navigate: Function;
  setOptions: Function;
}

export default function AbsenceScreen({route}: AbsenceScreenProps) {

    const colorScheme = Appearance.getColorScheme();

    const navigation = useNavigation<NavigationStackParams>();

    useEffect(() => {
        navigation.setOptions({
            title: route.params.username + ' - Absences',
            headerRight: () => <View style={{marginLeft: 10, flexDirection: 'row'}}>             
                <IconButton icon="add" size={24} color={colorScheme === 'dark' ? 'white' : 'black'} onPress={onAddAbsencePress}/>
                </View>,
        });
    });
    
    function onAddAbsencePress() {
        Alert.alert("Coming Soon!", "Not yet available!");
    }
    
    function onViewStatisticsPress() {
        Alert.alert("Coming Soon!", "Not yet available!");
    }
    
    return (
        <View style={[styles.container, colorScheme === 'dark' ? styles.darkBackground : styles.lightBackground]}>
            <View style={styles.bodyContainer}>
                <Text style={[styles.header, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>01.01.2025 - Public Holiday</Text>
                <Text style={[styles.header, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>01.06.2025 to 15.06.2025 - Holiday</Text>
                <Text style={[styles.header, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>15.11.2025 to 19.11.2025 - Illness</Text>
                <Text style={[styles.header, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>25.12.2025 - Public Holiday</Text>
                <View style={styles.buttonContainer}>
                    <TouchableOpacity style={styles.button} onPress={onViewStatisticsPress}>
                        <Text style={styles.buttonText}>View Statistics</Text>
                    </TouchableOpacity>
                </View>
            </View>
        </View>
    );

}

const styles = StyleSheet.create({
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
    header: {
        fontSize: 24,
        fontWeight: 'bold',
        textAlign: 'center'
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
});