import { Alert, Appearance, StyleSheet, View } from "react-native";
import { useEffect } from "react";
import { useNavigation } from "@react-navigation/native";
import IconButton from "../components/IconButton";
import CalendarAgendaComponent from "../components/CalendarAgendaComponent";

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
                <IconButton icon="bar-chart-outline" size={24} color={colorScheme === 'dark' ? 'white' : 'black'} onPress={onViewStatisticsPress}/>
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
            <CalendarAgendaComponent/>
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