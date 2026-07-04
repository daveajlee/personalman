import { StyleSheet, Text, TouchableOpacity } from "react-native";
import { Ionicons } from '@react-native-vector-icons/ionicons';

type IconTextButtonProps = {
    icon: any;
    text: string;
    onPress: Event;
    colour?: string;
}

function IconTextButton({icon, text, onPress, colour}: IconTextButtonProps) {
    return ( 
        <TouchableOpacity style={colour === 'red' ? styles.redButton : styles.greenButton} onPress={onPress}>
            <Ionicons name={icon} size={24} color="white" />
            <Text style={styles.buttonText}>{text}</Text>
        </TouchableOpacity>
    );
}

export default IconTextButton;;

const styles = StyleSheet.create({
    greenButton: {
        alignItems: "center",
        backgroundColor: "#185f92ff",
        width: '30%',
        height: 100,
        padding: 20,
        marginBottom: 20,
    },
    redButton: {
        alignItems: "center",
        backgroundColor: "red",
        width: '30%',
        padding: 20,
        marginBottom: 20,
    },
    buttonText: {
        color: 'white',
        fontSize: 16,
        fontWeight: 'bold',
        marginTop: 8,
        textAlign: 'center'
    }
})