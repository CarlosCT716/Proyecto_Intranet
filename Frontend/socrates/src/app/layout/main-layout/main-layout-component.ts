import { Component, inject, OnInit } from "@angular/core";
import { RouterOutlet } from "@angular/router";
import { SidebarComponent } from "../sidebar/sidebar.component/sidebar.component";
import { NavbarComponent } from "../navbar/navbar.component/navbar.component";
import { FooterComponent } from "../footer/footer.component/footer.component";
import { AuthService } from "../../core/services/auth.service";

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [
    RouterOutlet,
    SidebarComponent,
    NavbarComponent,
    FooterComponent
  ],
  templateUrl: './main-layout.component.html'
})
export class MainLayoutComponent implements OnInit {
  private authService = new AuthService();
  currentUserRole: string = 'STUDENT';
  currentUserName: string = 'Usuario';
  currentPageTitle: string = 'Bienvenido';
  currentSubtitle: string = '';
  currentUserRoleLabel: string = '';

  ngOnInit() {
    const user = this.authService.currentUser();
    if (user) {
      this.currentUserRole = this.mapRole(user.rol);
      this.currentUserName = user.username;


      // Configuración dinámica de textos
      if (this.currentUserRole === 'ADMIN') {
        this.currentPageTitle = 'Panel de Administración';
        this.currentSubtitle = 'Reportes y Control General';
        this.currentUserRoleLabel = 'Administrador';
      } else if (this.currentUserRole === 'TEACHER') {
        this.currentPageTitle = 'Bienvenido, Profesor';
        this.currentSubtitle = 'Panel de Gestión Académica';
        this.currentUserRoleLabel = 'Docente';
      } else {
        this.currentPageTitle = 'Bienvenido';
        this.currentSubtitle = 'Panel del Estudiante';
        this.currentUserRoleLabel = 'Estudiante';
      }
    }
  }
  private mapRole(backendRole: string): string {
    if (backendRole === 'ROLE_ADMIN') return 'ADMIN';
    if (backendRole === 'ROLE_PROFESOR') return 'TEACHER';
    return 'STUDENT';
  }
}