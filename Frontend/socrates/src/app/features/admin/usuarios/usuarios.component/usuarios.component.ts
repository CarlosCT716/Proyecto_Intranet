import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../../core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { Usuario } from '../../../../core/models/admin.interface';
import { delay } from 'rxjs';
import Swal from 'sweetalert2';
import { NgxPaginationModule } from 'ngx-pagination';

@Component({
  selector: 'app-usuarios-list', 
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, LoadingSpinnerComponent, NgxPaginationModule],
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
  
  p: number = 1;
  itemsPerPage: number = 10; 

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
    this.p = 1;
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
    this.p = 1;
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
    
    Swal.fire({
      title: '¿Estás seguro?',
      text: `¿Deseas ${accion} al usuario "${usuario.username}"?`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#0B4D6C',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, cambiar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.isLoading = true;
        this.cdr.detectChanges();
        
        this.adminService.cambiarEstadoUsuario(usuario.idUsuario).subscribe({
          next: () => {
            this.isLoading = false;
            this.cargarUsuarios(); 
            
            const Toast = Swal.mixin({
              toast: true,
              position: 'top-end',
              showConfirmButton: false,
              timer: 3000,
              timerProgressBar: true
            });
            Toast.fire({
              icon: 'success',
              title: `Usuario ${accion === 'activar' ? 'activado' : 'desactivado'} correctamente`
            });
          },
          error: (err) => {
            console.error(err);
            this.isLoading = false;
            this.cdr.detectChanges();
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'Ocurrió un error al cambiar el estado del usuario.',
              confirmButtonColor: '#d33'
            });
          }
        });
      }
    });
  }
}