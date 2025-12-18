import { Component, inject, OnInit, ViewChild, ElementRef, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from '../../../../core/services/admin.service';
import { AdminDashboard } from '../../../../core/models/admin.interface'; 
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, LoadingSpinnerComponent],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit, OnDestroy {
  private adminService = inject(AdminService);
  private cdr = inject(ChangeDetectorRef);

  @ViewChild('studentsChart') studentsChartRef!: ElementRef;
  @ViewChild('usersChart') usersChartRef!: ElementRef;

  data: AdminDashboard | null = null;
  isLoading = true;
  
  chartInstances: Chart[] = [];

  ngOnInit() {
    this.isLoading = true;
    this.adminService.getDashboard().subscribe({
      next: (res) => {
        this.data = res;
        this.isLoading = false;
        
        // Forzamos la detección de cambios para que el @if(isLoading) se resuelva y aparezcan los canvas
        this.cdr.detectChanges(); 

        // Ahora sí inicializamos los gráficos
        this.initCharts();
      },
      error: () => {
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  initCharts() {
    // Limpiar gráficos previos si existen (por si acaso)
    this.chartInstances.forEach(c => c.destroy());
    this.chartInstances = [];

    if (!this.data) return;

    // Gráfico de Estudiantes
    if (this.studentsChartRef?.nativeElement) {
      const labels = this.data.estudiantesPorCarrera.map(d => d.label);
      const values = this.data.estudiantesPorCarrera.map(d => d.value);

      this.chartInstances.push(new Chart(this.studentsChartRef.nativeElement, {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [{
            label: 'Estudiantes',
            data: values,
            backgroundColor: '#0B4D6C',
            borderRadius: 5,
            barThickness: 40 // Ajuste visual opcional
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { 
            legend: { display: false },
            tooltip: {
                backgroundColor: '#002940',
                padding: 10
            }
          },
          scales: {
            y: {
                beginAtZero: true,
                grid: { color: '#f3f4f6' }
            },
            x: {
                grid: { display: false }
            }
          }
        }
      }));
    }

    // Gráfico de Usuarios
    if (this.usersChartRef?.nativeElement) {
      const labels = this.data.distribucionUsuarios.map(d => d.label);
      const values = this.data.distribucionUsuarios.map(d => d.value);

      this.chartInstances.push(new Chart(this.usersChartRef.nativeElement, {
        type: 'doughnut',
        data: {
          labels: labels,
          datasets: [{
            data: values,
            backgroundColor: ['#0B4D6C', '#22c55e', '#eab308', '#a855f7'], // Agregué morado por si hay más roles
            borderWidth: 2,
            borderColor: '#ffffff',
            hoverOffset: 4
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          cutout: '70%', // Hace el agujero del doughnut más grande (estilo moderno)
          plugins: { 
            legend: { 
                position: 'bottom',
                labels: {
                    usePointStyle: true,
                    padding: 20
                }
            } 
          }
        }
      }));
    }
  }

  ngOnDestroy() {
    this.chartInstances.forEach(c => c.destroy());
  }

  getBadgeClass(accion: string): string {
    if (!accion) return 'bg-gray-100 text-gray-800';
    const acc = accion.toUpperCase();
    
    // Mapeo más robusto
    if (acc.includes('CREACION') || acc.includes('REGISTRO')) return 'bg-green-100 text-green-800';
    if (acc.includes('ACTUALIZACION') || acc.includes('MODIFICACION')) return 'bg-blue-100 text-blue-800';
    if (acc.includes('LOGIN') || acc.includes('ACCESO')) return 'bg-purple-100 text-purple-800';
    if (acc.includes('ELIMINACION') || acc.includes('BAJA')) return 'bg-red-100 text-red-800';
    
    return 'bg-gray-100 text-gray-800';
  }
}