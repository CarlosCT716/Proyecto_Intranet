import { Component, inject, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {
  private authService = inject(AuthService);
  @Input() title: string = 'Bienvenido';
  @Input() subtitle: string = 'Panel de Gestión'; 
  @Input() userName: string = 'Usuario';
  @Input() userRoleLabel: string = 'Usuario'; 

  isProfileOpen = false;

  toggleProfile() {
    this.isProfileOpen = !this.isProfileOpen;
  }

  logout() {
    this.authService.logout(); 
  }
}
