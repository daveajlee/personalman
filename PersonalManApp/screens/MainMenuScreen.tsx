import { Alert, Appearance, StyleSheet, Text, View } from "react-native";
import IconTextButton from "../components/IconTextButton";
import { useNavigation } from "@react-navigation/native";
import { useEffect } from "react";
import IconButton from "../components/IconButton";

type MainMenuScreenProps = {
  route: any;
}

type NavigationStackParams = {
  navigate: Function;
  setOptions: Function;
}

export default function MainMenuScreen({route}: MainMenuScreenProps) {

    const colorScheme = Appearance.getColorScheme();

    const navigation = useNavigation<NavigationStackParams>();

    useEffect(() => {
        navigation.setOptions({
            headerRight: () => <View style={{marginLeft: 10, flexDirection: 'row'}}>             
                <IconButton icon="log-out-outline" size={24} color={colorScheme === 'dark' ? 'white' : 'black'} onPress={onLogoutPress}/>
                </View>,
        });
    });

    function onLogoutPress() {
        navigation.navigate("LoginScreen");
    }

    function onAbsencePress() {
        navigation.navigate("AbsenceScreen", { username: route.params.username });
    }

    function onProfilePress() {
        Alert.alert("Coming Soon!", "Not yet available!");
    }

    function onUsersPress() {
        Alert.alert("Coming Soon!", "Not yet available!");
    }

    return (
        <View style={[styles.container, colorScheme === 'dark' ? styles.darkBackground : styles.lightBackground]}>
            <View style={styles.headerContainer}>
                <Text style={[styles.header, colorScheme === 'dark' ? styles.darkText : styles.lightText]}>Welcome, {route.params.username}</Text>
            </View>
            <View style={styles.bodyContainer}>
                <View style={styles.row}>
                    <IconTextButton icon="calendar-outline" text="Absences" onPress={onAbsencePress}/>
                    <IconTextButton icon="person-outline" text="Profile" onPress={onProfilePress}/>
                </View>
                <View style={styles.break}></View>
                <View style={styles.row}>
                    <IconTextButton icon="people-outline" text="Users" onPress={onUsersPress}/>
                </View>
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
    row: {
        flexDirection: 'row',
        justifyContent: 'space-around',
        width: '100%',
    },
    break: {
        marginTop: 20
    }
})