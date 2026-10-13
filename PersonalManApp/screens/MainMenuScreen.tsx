import { Alert, Appearance, Pressable, ScrollView, StyleSheet, Text, View } from "react-native";
import IconTextButton from "../components/IconTextButton";
import { useNavigation } from "@react-navigation/native";
import { useEffect } from "react";
import IconButton from "../components/IconButton";
import { SafeAreaView } from "react-native-safe-area-context";

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
        <SafeAreaView style={styles.centeredView}>
        <ScrollView contentContainerStyle={styles.container}>
          <View style={styles.infoContainer}>
            <View style={styles.titleContainer}>
              <IconButton icon="calendar-outline" size={28} color="black"/>
              <Text style={styles.leftInfoText}>Welcome, {route.params.username}</Text>
            </View>
          </View>
          
          
          <View style={styles.menuContainer}>
                <View style={styles.menuButtonLeft}>
                    <Pressable onPress={onAbsencePress}>
                        <IconButton icon="calendar-outline" size={48} color="white" onPress={onAbsencePress}/>
                        <Text style={styles.textStyle}>{"Absences"}</Text>
                    </Pressable>
                </View>
                <View style={styles.menuButtonRight}>
                    <Pressable onPress={onProfilePress}>
                        <IconButton icon="person-outline" size={48} color="white" onPress={onProfilePress}/>
                        <Text style={styles.textStyle}>{"Profile"}</Text>
                    </Pressable>     
                </View>
          </View>
          <View style={styles.menuContainer}>
                <View style={styles.menuButtonLeft}>
                    <Pressable onPress={onUsersPress}>
                        <IconButton icon="people-outline" size={48} color="white" onPress={onUsersPress}/>
                        <Text style={styles.textStyle}>{"Users"}</Text>
                    </Pressable>
                </View>
                {/*<View style={styles.menuButtonRight}>
                    <Pressable onPress={onMessagePress}>
                        <IconButton icon="mail-outline" size={48} color="black" onPress={onMessagePress}/>
                        <Text style={styles.textStyle}>{"Messages"}</Text>
                    </Pressable>
                </View>*/}
          </View>
        </ScrollView>
      </SafeAreaView>
    );

}

const styles = StyleSheet.create({
    centeredView: {
        flex: 1,
        width: '100%',
        alignItems: 'center',
        justifyContent: 'center'
    },
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    },
    infoContainer: {
        borderRadius: 25,
        backgroundColor: '#f2e9e9',
        width: '90%',
        marginBottom: '10%',
        borderStyle: 'solid',
        borderWidth: 3,
        borderColor: 'black'
    },
    titleContainer: {
        flexDirection: 'row',
        alignItems: 'flex-start',
        gap: 8,
        justifyContent: 'flex-start',
        marginTop: 5,
        marginLeft: 15,
    },
    leftInfoText: {
        color: 'black',
        marginLeft: 10,
        fontSize: 20,
        width: '30%',
        fontWeight: "bold"
    },
    rightInfoText: {
        color: 'black',
        marginLeft: 10,
        fontSize: 20,
        fontWeight: "bold",
        textAlign: 'right',
        width: '45%',
    },
    menuContainer: {
        flexDirection: 'row',
        marginTop: 10,
    },
    menuButtonLeft: {
        flexDirection: 'column',
        width: '45%',
        borderRadius: 25,
        backgroundColor: '#185f92ff',
        padding: 10,
        marginBottom: 10,
        marginRight: 10,
    },
    menuButtonRight: {
        flexDirection: 'column',
        width: '45%',
        borderRadius: 25,
        backgroundColor: '#185f92ff',
        padding: 10,
        marginLeft: 10,
        marginBottom: 10
    },
    textStyle: {
        color: 'white',
        fontWeight: 'bold',
        textAlign: 'center',
        fontSize: 20,
        marginTop: 10
    },
})