import { Component, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard.component',
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements AfterViewInit {
  @ViewChild('studentsChart') studentsChartRef!: ElementRef;
  @ViewChild('usersChart') usersChartRef!: ElementRef;

  chart1: any;
  chart2: any;

  ngAfterViewInit() {
    this.initStudentsChart();
    this.initUsersChart();
  }
initStudentsChart() {
    if (this.studentsChartRef?.nativeElement) {
      this.chart1 = new Chart(this.studentsChartRef.nativeElement, {
        type: 'bar',
        data: {
          labels: ['Ing. Sistemas', 'Diseño Gráfico', 'Administración', 'Marketing', 'Redes y Com.'],
          datasets: [{
            label: 'Estudiantes Activos',
            data: [450, 320, 280, 150, 210],
            backgroundColor: '#0B4D6C',
            borderRadius: 5
          }]
        },
        options: { responsive: true, maintainAspectRatio: false }
      });
    }
  }

  initUsersChart() {
    if (this.usersChartRef?.nativeElement) {
      this.chart2 = new Chart(this.usersChartRef.nativeElement, {
        type: 'doughnut',
        data: {
          labels: ['Estudiantes', 'Docentes', 'Administrativos'],
          datasets: [{
            data: [1245, 84, 15],
            backgroundColor: ['#0B4D6C', '#10B981', '#F59E0B'],
            hoverOffset: 4
          }]
        },
        options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'bottom' } } }
      });
    }
  }
}
