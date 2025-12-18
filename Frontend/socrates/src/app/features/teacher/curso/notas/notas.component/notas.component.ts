import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProfesorService } from '../../../../../core/services/profesor.service';
import { LoadingSpinnerComponent } from '../../../../../shared/loading-spinner.component';
import { delay } from 'rxjs';

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
    this.profesorService.guardarNotasMasivo(this.notas).subscribe({
      next: (res) => {
        alert('Notas guardadas correctamente');
        this.notas = res;
        this.guardando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        alert('Error al guardar las notas');
        this.guardando = false;
        this.cdr.detectChanges();
      }
    });
  }
}