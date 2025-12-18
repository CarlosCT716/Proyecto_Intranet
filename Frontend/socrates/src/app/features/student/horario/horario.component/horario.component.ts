import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { delay } from 'rxjs/operators';
import { AcademicoService } from '../../../../core/services/academico.service';
import { AuthService } from '../../../../core/services/auth.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';

@Component({
  selector: 'app-horario',
  standalone: true,
  imports: [CommonModule, LoadingSpinnerComponent],
  templateUrl: './horario.component.html'
})
export class HorarioComponent implements OnInit {
  private academicoService = inject(AcademicoService);
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  isLoading = true;
  eventos: any[] = [];
  
  // Días exactos como vienen o normalizados
  diasSemana = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO'];
  
  // Generamos horas de 07 a 22
  horas = Array.from({length: 16}, (_, i) => i + 7); 

  ngOnInit() {
    const usuario = this.authService.currentUser();
    this.isLoading = true;
    this.cdr.detectChanges();

    if (usuario) {
      this.academicoService.obtenerHorarioAlumno(usuario.idUsuario)
      .pipe(delay(500))
      .subscribe({
        next: (data) => {
          console.log('Horario recibido:', data); // Verifica en consola
          this.eventos = data;
          this.isLoading = false;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error(err);
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
    }
  }

  // --- LÓGICA DE POSICIONAMIENTO ---

  getLeftPosition(dia: string): string {
    // Normalizamos para evitar problemas de tildes o mayúsculas/minúsculas
    const diaNorm = dia.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toUpperCase();
    
    // Mapeo manual por si acaso "MIERCOLES" viene como "MIÉRCOLES" en BD
    let index = this.diasSemana.indexOf(diaNorm);
    
    // Si no lo encuentra directo, probamos lógica manual
    if (index === -1) {
        if (diaNorm.includes("MIER")) index = 2; // Miércoles
        else if (diaNorm.includes("SABA")) index = 5; // Sábado
    }

    if (index === -1) return '0%';
    return `calc(${index} * (100% / 7))`; 
  }

  getTopPosition(horaInicio: string): string {
    // horaInicio viene como "08:00" o "08:00:00"
    const [hora, min] = horaInicio.split(':').map(Number);
    
    const startHour = 7; // El calendario empieza a las 7:00
    const hourHeight = 48; // h-12 de tailwind son 48px

    // CORRECCIÓN: NO sumamos headerHeight porque el div es relativo al body
    const totalMinutosDesdeStart = (hora - startHour) * 60 + min;
    const pixels = totalMinutosDesdeStart * (hourHeight / 60);
    
    return `${pixels}px`;
  }

  getHeight(horaInicio: string, horaFin: string): string {
    const [h1, m1] = horaInicio.split(':').map(Number);
    const [h2, m2] = horaFin.split(':').map(Number);
    
    const durationMinutes = (h2 * 60 + m2) - (h1 * 60 + m1);
    const hourHeight = 48; 
    
    const pixels = durationMinutes * (hourHeight / 60);
    return `${pixels}px`;
  }

  // --- LÓGICA MÓVIL ---
  getEventosPorDia(dia: string) {
    const diaNorm = dia.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toUpperCase();
    return this.eventos.filter(e => {
        const eDia = e.dia.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toUpperCase();
        // Comparación laxa para tildes
        return eDia.includes(diaNorm.substring(0, 3)); 
    }).sort((a, b) => a.horaInicio.localeCompare(b.horaInicio));
  }
}