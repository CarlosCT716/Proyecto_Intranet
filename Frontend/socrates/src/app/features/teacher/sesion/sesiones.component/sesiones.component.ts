import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProfesorService } from '../../../../core/services/profesor.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { delay } from 'rxjs';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-sesiones',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, LoadingSpinnerComponent],
  templateUrl: './sesiones.component.html'
})
export class SesionesComponent implements OnInit {
  private profesorService = inject(ProfesorService);
  private route = inject(ActivatedRoute);
  private cdr = inject(ChangeDetectorRef);

  sesiones: any[] = [];
  isLoading = true;
  idCurso: number = 0;

  ngOnInit() {
    this.idCurso = Number(this.route.snapshot.params['id']);
    if (this.idCurso) {
        this.cargarSesiones();
    }
  }

  cargarSesiones() {
    this.isLoading = true;
    this.profesorService.listarSesionesPorCurso(this.idCurso)
      .pipe(delay(300))
      .subscribe({
        next: (data) => {
          this.sesiones = data;
          this.isLoading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
  }

  guardarCambioFila(sesion: any) {
    if (!sesion.temaTratado || sesion.temaTratado.trim() === '') {
        Swal.fire({
            icon: 'warning',
            title: 'Campo vacío',
            text: 'El tema tratado no puede estar vacío.',
            confirmButtonColor: '#0B4D6C'
        });
        return;
    }


    this.profesorService.actualizarTema(sesion.idSesion, sesion)
      .subscribe({
        next: (res) => {
          const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: true
          });
          
          Toast.fire({
            icon: 'success',
            title: 'Tema actualizado'
          });

          sesion.estado = res.estado; 
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error(err);
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudo guardar el tema. Intente nuevamente.',
            confirmButtonColor: '#d33'
          });
        }
      });
  }
}