import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../../core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { Usuario } from '../../../../core/models/admin.interface';
import { delay } from 'rxjs';

@Component({
  selector: 'app-usuarios-list', 
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, LoadingSpinnerComponent],
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.css'
})
export class UsuariosComponent implements OnInit {
  private adminService = inject(AdminService);
  private cdr = inject(ChangeDetectorRef);
  
  usuarios: Usuario[] = [];
  usuariosFiltrados: Usuario[] = [];
  isLoading = true;
  searchTerm: string = '';
  tabActiva: 'TODOS' | 'ROLE_ALUMNO' | 'ROLE_PROFESOR' | 'ROLE_ADMIN' = 'TODOS';

  ngOnInit() {
    this.cargarUsuarios();
  }

  cargarUsuarios() {
    this.isLoading = true;
    this.adminService.listarUsuarios().subscribe({
      next: (res) => {
        this.usuarios = res;
        this.cambiarTab(this.tabActiva);
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => this.isLoading = false
    });
  }

  cambiarTab(tab: any) {
    this.tabActiva = tab;
    this.filtrar();
  }

  filtrar() {
    let temporal = this.usuarios;
    if (this.tabActiva !== 'TODOS') {
      temporal = temporal.filter(u => u.rol === this.tabActiva);
    }

    const term = this.searchTerm.toLowerCase();
    this.usuariosFiltrados = temporal.filter(u => 
      u.nombres.toLowerCase().includes(term) || 
      u.apellidos.toLowerCase().includes(term) ||
      u.username.toLowerCase().includes(term) ||
      u.dni.includes(term)
    );
  }

  getRoleBadgeClass(rol: string): string {
    switch (rol) {
      case 'ROLE_ADMIN': return 'bg-blue-100 text-blue-800 border-blue-200';
      case 'ROLE_PROFESOR': return 'bg-green-100 text-green-800 border-green-200';
      case 'ROLE_ALUMNO': return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      default: return 'bg-gray-100 text-gray-800';
    }
  }

  formatRoleName(rol: string): string {
    return rol ? rol.replace('ROLE_', '') : 'N/A';
  }

  toggleEstado(usuario: Usuario) {
    const accion = usuario.activo ? 'desactivar' : 'activar';
    
    if (confirm(`¿Estás seguro de que deseas ${accion} al usuario "${usuario.username}"?`)) {
      this.isLoading = true;
      
      this.adminService.cambiarEstadoUsuario(usuario.idUsuario).subscribe({
        next: () => {
          this.isLoading = false;
          this.cargarUsuarios(); 
        },
        error: (err) => {
          console.error(err);
          this.isLoading = false;
          alert('Ocurrió un error al cambiar el estado del usuario.');
        }
      });
    }
  }
}