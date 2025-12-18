import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { ProfesorService } from '../../../../core/services/profesor.service';
import { AuthService } from '../../../../core/services/auth.service';
import { delay } from 'rxjs';

@Component({
  selector: 'app-teacher-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, LoadingSpinnerComponent],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  private profesorService = inject(ProfesorService);
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  isLoading = true;
  data: any = null; // Aquí guardaremos el TeacherDashboardDTO

  ngOnInit() {
    const usuario = this.authService.currentUser();
    this.isLoading = true;
    this.cdr.detectChanges();

    if (usuario) {
      this.profesorService.getDashboard(usuario.idUsuario).pipe(delay(2000)).subscribe({
        next: (resp) => {
          this.data = resp;
          console.log('Dashboard recibido:', this.data);
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
}