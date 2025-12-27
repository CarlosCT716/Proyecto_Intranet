import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProfesorService } from '../../../../../core/services/profesor.service';
import { LoadingSpinnerComponent } from '../../../../../shared/loading-spinner.component';
import { delay } from 'rxjs';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-notas',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingSpinnerComponent],
  templateUrl: './notas.component.html'
})
export class NotasComponent implements OnInit {
  profesorService = inject(ProfesorService);
  route = inject(ActivatedRoute);
  private cdr = inject(ChangeDetectorRef);
  
  notas: any[] = [];
  isLoading = true;
  guardando = false;

  ngOnInit() {
    const idCurso = this.route.snapshot.params['id'];
    this.cargarNotas(idCurso);
  }

  cargarNotas(idCurso: number) {
    this.isLoading = true;
    this.cdr.detectChanges();

    this.profesorService.listarNotasPorCurso(idCurso)
    .pipe(delay(500))
    .subscribe({
      next: (res) => {
        this.notas = res;
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

    Swal.fire({
        title: 'Guardando Notas',
        text: 'Procesando cambios...',
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });

    this.profesorService.guardarNotasMasivo(this.notas).subscribe({
      next: (res) => {
        Swal.close();
        
        // Toast de Éxito
        const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true
        });
        Toast.fire({
            icon: 'success',
            title: 'Notas guardadas correctamente'
        });

        this.notas = res;
        this.guardando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Hubo un problema al guardar las notas.',
            confirmButtonColor: '#d33'
        });
        this.guardando = false;
        this.cdr.detectChanges();
      }
    });
  }
}