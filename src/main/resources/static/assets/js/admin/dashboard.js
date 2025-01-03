/* globals Chart:false, feather:false */

(() => {
  'use strict'

  feather.replace({ 'aria-hidden': 'true' })
  const visitsData = [
    thisWeekVisits.SUNDAY || 0,
    thisWeekVisits.MONDAY || 0,
    thisWeekVisits.TUESDAY || 0,
    thisWeekVisits.WEDNESDAY || 0,
    thisWeekVisits.THURSDAY || 0,
    thisWeekVisits.FRIDAY || 0,
    thisWeekVisits.SATURDAY || 0
  ];

  // Graphs
  const ctx = document.getElementById('myChart')
  // eslint-disable-next-line no-unused-vars
  const myChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: [
        'Monday',
        'Tuesday',
        'Wednesday',
        'Thursday',
        'Friday',
        'Saturday',
        'Sunday'
      ],
      datasets: [{
        label: 'Number of connected users',
        data: visitsData,
        lineTension: 0,
        backgroundColor: 'transparent',
        borderColor: '#007bff',
        borderWidth: 4,
        pointBackgroundColor: '#007bff'
      }]
    },
    options: {
      scales: {
        yAxes: [{
          ticks: {
            beginAtZero: false
          }
        }]
      },
      legend: {
        display: false
      }
    }
  })
})()
