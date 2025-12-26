import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { AdminService } from '../../../../core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { forkJoin, delay } from 'rxjs';

@Component({
  selector: 'app-curso-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, LoadingSpinnerComponent],
  templateUrl: './curso-form.component.html'
})
export class CursoFormComponent implements OnInit {
  private adminService = inject(AdminService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private cdr = inject(ChangeDetectorRef);

  isLoading = true; // Empieza true porque cargamos catálogos (Carrera/Ciclo)
  isEditMode = false;
  titulo = 'Nuevo Curso';

  carreras: any[] = [];
  ciclos: any[] = [];
  profesores: any[] = [];

  curso: any = {
    nombreCurso: '',
    creditos: 3,
    cupoMaximo: 30,
    cupoActual: 0,
    idCarrera: null,
    idCiclo: null,
    idProfesor: null
  };

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    this.isEditMode = !!id;
    this.titulo = id ? 'Editar Curso' : 'Nuevo Curso';

    const obs: any = {
      carreras: this.adminService.listarCarreras(),
      ciclos: this.adminService.listarCiclos(),
      usuarios: this.adminService.listarUsuarios()
    };

    if (id) {
        obs.curso = this.adminService.obtenerCurso(id);
    }

    forkJoin(obs)
      .pipe(delay(2000))
      .subscribe({
        next: (res: any) => {
          this.carreras = res.carreras;
          this.ciclos = res.ciclos;
          this.profesores = res.usuarios.filter((u: any) => u.rol === 'ROLE_PROFESOR' || u.idRol === 2);
          
          if (id && res.curso) {
              this.curso = res.curso;
              // Mapeos seguros
              if (res.curso.carrera) this.curso.idCarrera = res.curso.carrera.idCarrera;
              if (res.curso.ciclo) this.curso.idCiclo = res.curso.ciclo.idCiclo;
              if (res.curso.profesor) this.curso.idProfesor = res.curso.profesor.idUsuario;
          }
          this.isLoading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.isLoading = false;
          this.router.navigate(['/admin/cursos']);
          this.cdr.detectChanges();
        }
      });
  }

  guardar() {
    this.isLoading = true; 
    this.cdr.detectChanges();

    const request$ = this.isEditMode
        ? this.adminService.actualizarCurso(this.curso.idCurso, this.curso)
        : this.adminService.crearCurso(this.curso);

    request$.subscribe({
        next: () => { 
            alert(this.isEditMode ? 'Curso actualizado' : 'Curso creado'); 
            this.router.navigate(['/admin/cursos']); 
        },
        error: () => { 
            this.isLoading = false; 
            alert('Error al guardar');
            this.cdr.detectChanges();
        }
    });
  }
}