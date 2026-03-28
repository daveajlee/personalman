import isEmpty from 'lodash/isEmpty';
import React, {useCallback} from 'react';
import {StyleSheet, Appearance, View, Text, TouchableOpacity, Button} from 'react-native';
import testIDs from '../testIDs';

interface ItemProps {
  item: any;
}

const AgendaItem = (props: ItemProps) => {
  const {item} = props;

  const colorScheme = Appearance.getColorScheme();

  /*const buttonPressed = useCallback(() => {
    Alert.alert(item.title);
  }, [item]);

  const itemPressed = useCallback(() => {
    Alert.alert(item.title);
  }, [item]);*/

  if (isEmpty(item)) {
    return (
      <View style={styles.emptyItem}>
        <Text style={styles.emptyItemText}>No Events Planned Today</Text>
      </View>
    );
  }

  return (
    <View style={colorScheme === 'dark' ? styles.itemDark : styles.item} testID={testIDs.agenda.ITEM}>
      <View>
        <Text style={colorScheme === 'dark' ? styles.itemHourTextDark : styles.itemHourText}>{item.hour}</Text>
        <Text style={colorScheme === 'dark' ? styles.itemDurationTextDark : styles.itemDurationText}>{item.duration}</Text>
      </View>
      <Text style={colorScheme === 'dark' ? styles.itemTitleTextDark : styles.itemTitleText}>{item.title}</Text>
      {/*<View style={styles.itemButtonContainer}>
        <Button color={'grey'} title={'Info'} onPress={buttonPressed}/>
      </View>*/}
    </View>
  );
};

export default React.memo(AgendaItem);

const styles = StyleSheet.create({
  item: {
    padding: 20,
    backgroundColor: 'white',
    borderBottomWidth: 1,
    borderBottomColor: 'lightgrey',
    flexDirection: 'row'
  },
  itemDark: {
    padding: 20,
    backgroundColor: 'black',
    flexDirection: 'row'
  },
  itemHourText: {
    color: 'black'
  },
  itemHourTextDark: {
    color: 'white'
  },
  itemDurationText: {
    color: 'grey',
    fontSize: 12,
    marginTop: 4,
    marginLeft: 4
  },
  itemDurationTextDark: {
    color: 'lightgrey',
    fontSize: 12,
    marginTop: 4,
    marginLeft: 4
  },
  itemTitleText: {
    color: 'black',
    marginLeft: 16,
    fontWeight: 'bold',
    fontSize: 16
  },
  itemTitleTextDark: {
    color: 'white',
    marginLeft: 16,
    fontWeight: 'bold',
    fontSize: 16
  },
  itemButtonContainer: {
    flex: 1,
    alignItems: 'flex-end'
  },
  emptyItem: {
    paddingLeft: 20,
    height: 52,
    justifyContent: 'center',
    borderBottomWidth: 1,
    borderBottomColor: 'lightgrey'
  },
  emptyItemText: {
    color: 'lightgrey',
    fontSize: 14
  }
});