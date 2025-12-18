import { Component, inject } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-profile.component',
  imports: [],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent {
  private authService = inject(AuthService);
  user = this.authService.currentUser();

  academicInfo = {
    carrera: 'Computación e Informática',
    ciclo: 'V Ciclo (2025-02)',
    campus: 'Lima Centro',
    dni: '70487041',
    codigo: 'N00181060',
    email: 'I202402283@cibertec.edu.pe'
  };
}
