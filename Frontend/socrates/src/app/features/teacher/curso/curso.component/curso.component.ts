import { Component, inject, OnInit,ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProfesorService } from '../../../../core/services/profesor.service';
import { AuthService } from '../../../../core/services/auth.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { delay } from 'rxjs';

@Component({
  selector: 'app-curso',
  standalone: true,
  imports: [CommonModule, RouterLink, LoadingSpinnerComponent],
  templateUrl: './curso.component.html'
})
export class CursoComponent implements OnInit {
  profesorService = inject(ProfesorService);
  authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);
  
  cursos: any[] = [];
  isLoading = true;

  ngOnInit() {
    const user = this.authService.currentUser();
    if(user) {
      this.profesorService.listarCursosAsignados(user.idUsuario)
      .pipe(delay(500))
      .subscribe({
        next: (res) => {
          this.cursos = res;
          this.isLoading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
    }
  }
}