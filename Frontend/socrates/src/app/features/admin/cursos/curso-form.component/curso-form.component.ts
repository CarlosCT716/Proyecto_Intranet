import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { AcademicoService } from '../../../../core/services/academico.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { forkJoin, delay, of } from 'rxjs';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-curso-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, LoadingSpinnerComponent],
  templateUrl: './curso-form.component.html'
})
export class CursoFormComponent implements OnInit {
  private academicoService = inject(AcademicoService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private cdr = inject(ChangeDetectorRef);

  isLoading = true;
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
      carreras: this.academicoService.listarCarreras(),
      ciclos: this.academicoService.listarCiclos(),
      usuarios: this.academicoService.listarUsuariosActivos()
    };

    if (id) {
        obs.curso = this.academicoService.obtenerCurso(id);
    } else {
        obs.curso = of(null);
    }

    this.cdr.detectChanges();

    forkJoin(obs)
      .pipe(delay(500))
      .subscribe({
        next: (res: any) => {
          this.carreras = res.carreras;
          this.ciclos = res.ciclos;
          
          this.profesores = res.usuarios.filter((u: any) => {
             const nombreRol = u.rol?.nombreRol || u.rolUsuario || ''; 
             const rolUpper = typeof nombreRol === 'string' ? nombreRol.toUpperCase() : '';
             
             return rolUpper.includes('PROFESOR') || rolUpper.includes('DOCENTE') || u.rol?.idRol === 2 || u.idRol === 2;
          });
          
          if (id && res.curso) {
              this.curso = res.curso;
              
              if (res.curso.idCarrera) this.curso.idCarrera = res.curso.idCarrera;
              else if (res.curso.carrera) this.curso.idCarrera = res.curso.carrera.idCarrera;

              if (res.curso.idCiclo) this.curso.idCiclo = res.curso.idCiclo;
              else if (res.curso.ciclo) this.curso.idCiclo = res.curso.ciclo.idCiclo;

              if (res.curso.idProfesor) this.curso.idProfesor = res.curso.idProfesor;
              else if (res.curso.profesor) this.curso.idProfesor = res.curso.profesor.idUsuario;
          }
          this.isLoading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.isLoading = false;
          this.cdr.detectChanges();
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Error al cargar los datos del curso',
            confirmButtonColor: '#d33'
          }).then(() => {
            this.router.navigate(['/admin/cursos']);
          });
        }
      });
  }

  guardar() {
    if (!this.curso.nombreCurso || !this.curso.idCarrera || !this.curso.idCiclo || !this.curso.idProfesor) {
        Swal.fire('Atención', 'Por favor complete todos los campos obligatorios.', 'warning');
        return;
    }

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

    const request$ = this.isEditMode
        ? this.academicoService.actualizarCurso(this.curso.idCurso, this.curso)
        : this.academicoService.crearCurso(this.curso);

    request$.subscribe({
        next: () => { 
            Swal.fire({
                icon: 'success',
                title: 'Éxito',
                text: this.isEditMode ? 'Curso actualizado correctamente' : 'Curso creado correctamente',
                confirmButtonColor: '#0B4D6C'
            }).then(() => {
                this.router.navigate(['/admin/cursos']); 
            });
        },
        error: (err) => { 
            this.isLoading = false; 
            this.cdr.detectChanges();
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: err.error?.message || 'Hubo un problema al guardar el curso',
                confirmButtonColor: '#d33'
            });
        }
    });
  }
}