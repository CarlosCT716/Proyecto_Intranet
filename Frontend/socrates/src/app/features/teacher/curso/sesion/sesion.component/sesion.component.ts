import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProfesorService } from '../../../../../core/services/profesor.service';
import { LoadingSpinnerComponent } from '../../../../../shared/loading-spinner.component';
import { delay } from 'rxjs';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-sesiones',
  standalone: true,
  imports: [CommonModule, RouterLink, LoadingSpinnerComponent],
  templateUrl: './sesion.component.html'
})
export class SesionesComponent implements OnInit {
  profesorService = inject(ProfesorService);
  route = inject(ActivatedRoute);
  private cdr = inject(ChangeDetectorRef);
  
  sesiones: any[] = [];
  isLoading = true;

  ngOnInit() {
    const idCurso = this.route.snapshot.params['id'];
    this.profesorService.listarSesionesPorCurso(idCurso)
    .pipe(delay(500))
    .subscribe({
      next: (res) => {
        this.sesiones = res;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.isLoading = false;
        this.cdr.detectChanges();
        
        // Alerta opcional si falla la carga
        const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 3000
        });
        Toast.fire({
            icon: 'error',
            title: 'Error al cargar sesiones'
        });
      }
    });
  }
}