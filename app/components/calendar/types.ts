import {StyleProp, ViewStyle, TextStyle} from 'react-native';
import XDate from 'xdate';

export type ContextProp = {
  context?: CalendarContextProps;
};
export type MarkingTypes = 'dot' | 'multi-dot' | 'period' | 'multi-period' | 'custom';
export type MarkedDates = {
  [key: string]: MarkingProps;
};
export type DayState = 'selected' | 'disabled' | 'inactive' | 'today' | '';
export type Direction = 'left' | 'right';
export type DateData = {
  year: number;
  month: number;
  day: number;
  timestamp: number;
  dateString: string;
};
export interface Theme {
  timelineContainer?: object;
  contentStyle?: ViewStyle;
  event?: object;
  eventTitle?: object;
  eventSummary?: object;
  eventTimes?: object;
  line?: object;
  verticalLine?: object;
  nowIndicatorLine?: object;
  nowIndicatorKnob?: object;
  timeLabel?: object;
  todayTextColor?: string;
  calendarBackground?: string;
  indicatorColor?: string;
  textSectionTitleColor?: string;
  textSectionTitleDisabledColor?: string;
  dayTextColor?: string;
  selectedDayTextColor?: string;
  monthTextColor?: string;
  selectedDayBackgroundColor?: string;
  arrowColor?: string;
  textDisabledColor?: string;
  textInactiveColor?: string;
  backgroundColor?: string; //TODO: remove in V2
  dotColor?: string;
  selectedDotColor?: string;
  disabledArrowColor?: string;
  textDayFontFamily?: TextStyle['fontFamily'];
  textMonthFontFamily?: TextStyle['fontFamily'];
  textDayHeaderFontFamily?: TextStyle['fontFamily'];
  textDayFontWeight?: TextStyle['fontWeight'];
  textMonthFontWeight?: TextStyle['fontWeight'];
  textDayHeaderFontWeight?: TextStyle['fontWeight'];
  textDayFontSize?: number;
  textMonthFontSize?: number;
  textDayHeaderFontSize?: number;
  agendaDayTextColor?: string;
  agendaDayNumColor?: string;
  agendaTodayColor?: string;
  agendaKnobColor?: string;
  todayButtonFontFamily?: TextStyle['fontFamily'];
  todayButtonFontWeight?: TextStyle['fontWeight'];
  todayButtonFontSize?: number;
  textDayStyle?: TextStyle;
  dotStyle?: object;
  arrowStyle?: ViewStyle;
  todayBackgroundColor?: string;
  disabledDotColor?: string;
  inactiveDotColor?: string;
  todayDotColor?: string;
  todayButtonTextColor?: string;
  todayButtonPosition?: string;
  arrowHeight?: number;
  arrowWidth?: number;
  weekVerticalMargin?: number;
  reservationsBackgroundColor?: string;
  stylesheet?: {
    calendar?: {
      main?: object;
      header?: object;
    };
    day?: {
      basic?: object;
      period?: object;
    };
    dot?: object;
    marking?: object;
    'calendar-list'?: {
      main?: object;
    };
    agenda?: {
      main?: object;
      list?: object;
    };
    expandable?: {
      main?: object;
    };
  };
}

export type AgendaEntry = {
  name: string;
  height: number;
  day: string;
};

export type AgendaSchedule = {
  [date: string]: AgendaEntry[];
};

type DOT = {
  key?: string;
  color: string;
  selectedDotColor?: string;
};

type PERIOD = {
  color: string;
  startingDay?: boolean;
  endingDay?: boolean;
};

export interface DayAgenda {
  reservation?: AgendaEntry;
  date?: XDate;
}

export interface DotProps {
  theme?: Theme;
  color?: string;
  marked?: boolean;
  selected?: boolean;
  disabled?: boolean;
  inactive?: boolean;
  today?: boolean;
}

type CustomStyle = {
  container?: ViewStyle;
  text?: TextStyle;
};

export interface MarkingProps extends DotProps {
  type?: MarkingTypes;
  theme?: Theme;
  selected?: boolean;
  marked?: boolean;
  today?: boolean;
  disabled?: boolean;
  inactive?: boolean;
  disableTouchEvent?: boolean;
  activeOpacity?: number;
  textColor?: string;
  selectedColor?: string;
  selectedTextColor?: string;
  customTextStyle?: StyleProp<TextStyle>;
  customContainerStyle?: StyleProp<ViewStyle>;
  dotColor?: string;
  //multi-dot
  dots?: DOT[];
  //multi-period
  periods?: PERIOD[];
  startingDay?: boolean;
  endingDay?: boolean;
  accessibilityLabel?: string;
  customStyles?: CustomStyle;
}

export enum UpdateSources {
  CALENDAR_INIT = 'calendarInit',
  PROP_UPDATE = 'propUpdate',
  TODAY_PRESS = 'todayPress',
  DAY_PRESS = 'dayPress',
  ARROW_PRESS = 'arrowPress',
  WEEK_ARROW_PRESS = 'weekArrowPress',
  LIST_DRAG = 'listDrag',
  PAGE_SCROLL = 'pageScroll',
  WEEK_SCROLL = 'weekScroll'
}

export interface CalendarContextProps {
  date: string;
  prevDate: string;
  selectedDate: string;
  setDate: (date: string, source: UpdateSources) => void;
  updateSource: UpdateSources;
  setDisabled: (disable: boolean) => void;
  numberOfDays?: number;
  timelineLeftInset?: number;
}