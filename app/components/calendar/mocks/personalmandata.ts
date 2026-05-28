import {MarkedDates} from '../types';

export const agendaItems = [
  {
    title: new Date("2026-03-12T23:00:00").toISOString().split('T')[0],
    data: [{hour: '12am', duration: '24h', title: 'Holiday'}]
  },
  {
    title: new Date("2026-03-13T23:00:00").toISOString().split('T')[0],
    data: [{hour: '12am', duration: '24h', title: 'Holiday'}]
  },
  {
    title: new Date("2026-03-23T23:00:00").toISOString().split('T')[0],
    data: [{hour: '12am', duration: '24h', title: 'Illness'}]
  },
  {
    title: new Date("2026-03-24T23:00:00").toISOString().split('T')[0],
    data: [{hour: '12am', duration: '24h', title: 'Illness'}]
  },
  {
    title: new Date("2026-04-03T23:00:00").toISOString().split('T')[0],
    data: [{hour: '12am', duration: '24h', title: 'Public Holiday'}]
  },
  {
    title: new Date("2026-04-06T23:00:00").toISOString().split('T')[0],
    data: [{hour: '12am', duration: '24h', title: 'Public Holiday'}]
  },
];

export function getMarkedDates() {
  const marked: MarkedDates = {};

  /*agendaItems.forEach(item => {
    console.log(item.title);
    // NOTE: only mark dates with data
    if (item.data && item.data.length > 0 && !isEmpty(item.data[0])) {
      marked[item.title] = {marked: true};
    } else {
      marked[item.title] = {disabled: true};
    }
  });*/
  return marked;
}