import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { delay } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AcademicoService } from '../../../../core/services/academico.service';
import { AuthService } from '../../../../core/services/auth.service'; 
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';

@Component({
  selector: 'app-cursos',
  standalone: true,
  imports: [CommonModule, RouterLink, LoadingSpinnerComponent],
  templateUrl: './cursos.component.html',
  styleUrl: './cursos.component.css', 
})
export class CursosComponent implements OnInit {
  private academicoService = inject(AcademicoService);
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  isLoading: boolean = true;
  cursos: any[] = [];

  ngOnInit() {
    const usuario = this.authService.currentUser();
    this.isLoading = true;
    this.cdr.detectChanges();

    if (usuario) {
      this.academicoService.listarCursosAlumno(usuario.idUsuario)
      .pipe(delay(1000))
      .subscribe({
        next: (data) => {
            this.cursos = data;
            this.isLoading = false;
            this.cdr.detectChanges();
        },
        error: (err) => {
            console.error('Error cargando cursos', err);
            this.isLoading = false;
            this.cdr.detectChanges();
        }
      });
    } else {
        this.isLoading = false;
        this.cdr.detectChanges();
    }
  }
}