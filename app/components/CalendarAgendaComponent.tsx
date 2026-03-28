import { AgendaList, CalendarProvider, ExpandableCalendar } from "react-native-calendars";
import { useCallback, useRef } from "react";
import testIDs from './calendar/testIDs';
import {agendaItems} from './calendar/mocks/personalmandata';
import {getLightTheme, getDarkTheme, themeColor, lightThemeColor} from './calendar/mocks/theme';
import { Appearance, StyleSheet, View } from "react-native";
import AgendaItem from './calendar/mocks/AgendaItem';

function CalendarAgendaComponent() {

    const colorScheme = Appearance.getColorScheme();

    const ITEMS: any[] = agendaItems;
    const todayBtnTheme = useRef({
        todayButtonTextColor: themeColor,
        backgroundColor: '#000000'
    });
    const theme = useRef(colorScheme === 'dark' ? getDarkTheme() : getLightTheme());

    const leftArrowIcon = require('../img/previous.png');
    const rightArrowIcon = require('../img/next.png');

    const renderItem = useCallback(({item}: any) => {
        const isLongItem = item.itemCustomHeightType === 'LongEvent';
        return <View style={{paddingTop: isLongItem ? 40 : 0}}><AgendaItem item={item}/></View>;
    }, []);

    return (
        <CalendarProvider
            date={ITEMS[0]?.title}
            showTodayButton
            theme={todayBtnTheme.current}
        >
            <ExpandableCalendar
                    testID={testIDs.expandableCalendar.CONTAINER}
                    theme={theme.current}
                    firstDay={1}
                    leftArrowImageSource={leftArrowIcon}
                    rightArrowImageSource={rightArrowIcon}
            />
            <AgendaList
                sections={ITEMS}
                renderItem={renderItem}
                sectionStyle={styles.section}
                infiniteListProps={
                    {
                        itemHeight: 80,
                        titleHeight: 50,
                        itemHeightByType: {
                            LongEvent: 120
                        }
                    }
                }
            />
        </CalendarProvider>
    );

}

export default CalendarAgendaComponent;

const styles = StyleSheet.create({
  section: {
    backgroundColor: lightThemeColor,
    color: 'grey',
    textTransform: 'capitalize'
  }
});