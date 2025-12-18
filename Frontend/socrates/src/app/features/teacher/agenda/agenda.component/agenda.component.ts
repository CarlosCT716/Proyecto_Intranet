import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfesorService } from '../../../../core/services/profesor.service';
import { AuthService } from '../../../../core/services/auth.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';

@Component({
  selector: 'app-agenda',
  standalone: true,
  imports: [CommonModule, LoadingSpinnerComponent],
  templateUrl: './agenda.component.html'
})
export class AgendaComponent implements OnInit {
  private profesorService = inject(ProfesorService);
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  isLoading = true;
  vistaCalendario = true;
  agenda: any[] = [];
  
  diasSemana = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO'];

  ngOnInit() {
    const usuario = this.authService.currentUser();
    if (usuario) {
      this.profesorService.getAgenda(usuario.idUsuario).subscribe({
        next: (data) => {
          this.agenda = data;
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
    }
  }

  toggleView(calendario: boolean) {
    this.vistaCalendario = calendario;
  }

  getEventosPorDia(dia: string) {
    // Normalizar tildes y mayúsculas para comparar
    const diaNorm = dia.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toUpperCase();
    
    return this.agenda.filter(e => {
        const eDia = e.dia.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toUpperCase();
        return eDia.includes(diaNorm.substring(0, 3)); 
    }).sort((a, b) => a.horaInicio.localeCompare(b.horaInicio));
  }
}