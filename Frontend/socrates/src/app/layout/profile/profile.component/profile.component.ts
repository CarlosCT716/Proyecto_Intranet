import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';
import { AdminService } from '../../../core/services/admin.service'; 
import { LoadingSpinnerComponent } from '../../../shared/loading-spinner.component';
import { delay } from 'rxjs';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, LoadingSpinnerComponent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent implements OnInit {
  private authService = inject(AuthService);
  private adminService = inject(AdminService);
  private cdr = inject(ChangeDetectorRef);

  currentUser = this.authService.currentUser; 

  userData: any = null;
  isLoading = true;

  ngOnInit() {
    this.cargarDatosPerfil();
  }

  cargarDatosPerfil() {
    const id = this.currentUser()?.idUsuario;

    if (id) {
      this.isLoading = true;
      
      this.adminService.obtenerUsuario(id)
        .pipe(delay(1500)) 
        .subscribe({
          next: (data) => {
            this.userData = data;
            this.isLoading = false;
            this.cdr.detectChanges(); 
          },
          error: (err) => {
            console.error('Error cargando perfil', err);
            this.isLoading = false;
            this.cdr.detectChanges(); 
          }
        });
    } else {
      this.isLoading = false;
    }
  }
}