import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProfesorService } from '../../../../../../core/services/profesor.service';
import { LoadingSpinnerComponent } from '../../../../../../shared/loading-spinner.component';
import { delay } from 'rxjs';

@Component({
  selector: 'app-asistencia',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingSpinnerComponent],
  templateUrl: './asistencia.component.html'
})
export class AsistenciaComponent implements OnInit {
  profesorService = inject(ProfesorService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  private cdr = inject(ChangeDetectorRef);
  
  asistenciaList: any[] = [];
  idSesion: number = 0;
  isLoading = true;
  guardando = false;
  finalizando = false;

  ngOnInit() {
    this.idSesion = this.route.snapshot.params['idSesion'];
    this.cargarAsistencia();
  }

  cargarAsistencia() {
    this.isLoading = true;
    this.cdr.detectChanges();

    this.profesorService.obtenerDetalleAsistencia(this.idSesion)
    .pipe(delay(500))
    .subscribe({
      next: (res) => {
        this.asistenciaList = res;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  guardar() {
    this.guardando = true;
    const payload = this.asistenciaList.map(a => ({
      idAlumno: a.idAlumno,
      estado: Number(a.idEstado),
      observacion: a.observacion
    }));

    this.profesorService.registrarAsistencia(this.idSesion, payload).subscribe({
      next: () => {
        alert('Asistencia guardada temporalmente');
        this.guardando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        alert('Error al guardar asistencia');
        this.guardando = false;
        this.cdr.detectChanges();
      }
    });
  }

  finalizar() {
    const invalidos = this.asistenciaList.filter(a => a.idEstado == 0);
    if(invalidos.length > 0) {
        alert('Debe registrar el estado de asistencia para todos los alumnos antes de finalizar.');
        return;
    }

    if(!confirm('¿Seguro que desea finalizar la sesión? Una vez finalizada, no podrá editar la asistencia.')) return;
    
    this.finalizando = true;
    this.guardarInterno().then(() => {
        this.profesorService.finalizarSesion(this.idSesion).subscribe({
            next: () => {
                alert('Sesión finalizada correctamente.');
                this.router.navigate(['/docente/cursos']); 
            },
            error: () => {
                alert('Error al finalizar sesión');
                this.finalizando = false;
                this.cdr.detectChanges();
            }
        });
    });
  }

  private guardarInterno(): Promise<void> {
    return new Promise((resolve, reject) => {
        const payload = this.asistenciaList.map(a => ({
            idAlumno: a.idAlumno,
            estado: Number(a.idEstado),
            observacion: a.observacion
        }));
        this.profesorService.registrarAsistencia(this.idSesion, payload).subscribe({
            next: () => resolve(),
            error: () => resolve() 
        });
    });
  }
}