import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { AdminService } from '../../../../core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { delay } from 'rxjs';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, LoadingSpinnerComponent], 
  templateUrl: './usuario-form.component.html'
})
export class UsuarioFormComponent implements OnInit {
  private adminService = inject(AdminService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private cdr = inject(ChangeDetectorRef);

  isLoading = false;
  isEditMode = false;
  titulo = 'Nuevo Usuario';
  
  usuario: any = {
    username: '',
    password: '',
    nombres: '',
    apellidos: '',
    dni: '',
    telefono: '',
    direccion: '',
    email: '',
    idRol: 2, 
    activo: true
  };

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.isEditMode = true;
      this.titulo = 'Editar Usuario';
      this.cargarUsuario(id);
    }
  }

  cargarUsuario(id: number) {
    this.isLoading = true;
    this.adminService.obtenerUsuario(id)
      .pipe(delay(2000))
      .subscribe({
        next: (res: any) => {
          this.usuario = res;
          if (res.rol && typeof res.rol === 'object') {
              this.usuario.idRol = res.rol.idRol;
          } else if (res.rol === 'ROLE_ADMIN') {
              this.usuario.idRol = 1;
          } else if (res.rol === 'ROLE_PROFESOR') {
              this.usuario.idRol = 2;
          } else {
              this.usuario.idRol = 3;
          }
          this.usuario.password = ''; 
          this.isLoading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.isLoading = false;
          this.cdr.detectChanges();
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Error al cargar usuario',
            confirmButtonColor: '#d33'
          }).then(() => {
            this.router.navigate(['/admin/usuarios']);
          });
        }
      });
  }

  guardar() {
    Swal.fire({
      title: this.isEditMode ? 'Actualizando...' : 'Guardando...',
      text: 'Por favor espere',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });

    this.isLoading = true;
    this.cdr.detectChanges();

    const payload: any = {
      ...this.usuario,
      idRol: Number(this.usuario.idRol)
    };
    
    if (this.isEditMode && !payload.password) {
        delete payload.password;
    }

    const request$ = this.isEditMode
        ? this.adminService.actualizarUsuario(this.usuario.idUsuario, payload)
        : this.adminService.crearUsuario(payload);

    request$.subscribe({
        next: () => {
          Swal.fire({
            icon: 'success',
            title: 'Éxito',
            text: this.isEditMode ? 'Usuario actualizado correctamente' : 'Usuario creado correctamente',
            confirmButtonColor: '#0B4D6C'
          }).then(() => {
            this.router.navigate(['/admin/usuarios']);
          });
        },
        error: () => { 
            this.isLoading = false; 
            this.cdr.detectChanges();
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'Hubo un problema al guardar el usuario',
              confirmButtonColor: '#d33'
            });
        }
    });
  }
}