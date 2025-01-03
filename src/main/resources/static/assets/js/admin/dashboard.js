/* globals Chart:false, feather:false */

(() => {
  'use strict'

  feather.replace({ 'aria-hidden': 'true' });
  // 데이터 준비
  const visitsData = [];
  const labels = [];
  const sortedVisits = thisWeekVisits.sort((a, b) => {
    const dateA = new Date(a.localDate);
    const dateB = new Date(b.localDate);
    return dateA - dateB; // 오름차순 정렬
  });
  sortedVisits.forEach(visit => {
    labels.push(visit.localDate); // 날짜를 라벨에 추가
    visitsData.push(visit.connect); // 접속자 수를 데이터에 추가
  });


  // Graphs
  const ctx = document.getElementById('myChart')
  // eslint-disable-next-line no-unused-vars
  const myChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels,
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
