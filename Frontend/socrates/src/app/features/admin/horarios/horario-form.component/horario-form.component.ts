import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core'; import { CommonModule } from '@angular/common'; import { FormsModule } from '@angular/forms'; import { Router, RouterLink, ActivatedRoute } from '@angular/router'; import { AdminService } from '../../../../core/services/admin.service'; import { AcademicoService } from '../../../../core/services/academico.service'; import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component'; import { forkJoin, delay, of } from 'rxjs'; import Swal from 'sweetalert2';

@Component({ selector: 'app-horario-form', standalone: true, imports: [CommonModule, FormsModule, RouterLink, LoadingSpinnerComponent], templateUrl: './horario-form.component.html' }) export class HorarioFormComponent implements OnInit {
  private adminService = inject(AdminService); private academicoService = inject(AcademicoService); private router = inject(Router); private route = inject(ActivatedRoute); private cdr = inject(ChangeDetectorRef);

  isLoading = true; isEditMode = false; titulo = 'Nuevo Horario';

  cursos: any[] = []; aulas: any[] = []; diasSemana = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO'];

  horario: any = { diaSemana: 'LUNES', horaInicio: '', horaFin: '', idCurso: null, idAula: null, fechaInicio: new Date().toISOString().split('T')[0] };

  ngOnInit() {
    const id = this.route.snapshot.params['id']; this.isEditMode = !!id; this.titulo = id ? 'Editar Horario' : 'Nuevo Horario';

    const obs: any = {
      cursos: this.academicoService.listarCursosActivos(),
      aulas: this.academicoService.listarAulasActivas()
    };

    if (id) {
      obs.data = this.adminService.obtenerHorario(id);
    } else {
      obs.data = of(null);
    }

    forkJoin(obs)
      .pipe(delay(500))
      .subscribe({
        next: (res: any) => {
          this.cursos = res.cursos;
          this.aulas = res.aulas;

          if (id && res.data) {
            this.horario = res.data;
            if (res.data.idCurso) this.horario.idCurso = res.data.idCurso;
            else if (res.data.curso) this.horario.idCurso = res.data.curso.idCurso;

            if (res.data.idAula) this.horario.idAula = res.data.idAula;
            else if (res.data.aula) this.horario.idAula = res.data.aula.idAula;
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
            text: 'Error al cargar datos del formulario',
            confirmButtonColor: '#d33'
          }).then(() => {
            this.router.navigate(['/admin/horarios']);
          });
        }
      });
  }

  guardar() {
    if (!this.horario.idCurso || !this.horario.idAula || !this.horario.horaInicio || !this.horario.horaFin) { Swal.fire('Atención', 'Por favor complete todos los campos obligatorios.', 'warning'); return; }

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

    const payload = {
      ...this.horario,
      horaInicio: this.horario.horaInicio.length === 5 ? this.horario.horaInicio + ':00' : this.horario.horaInicio,
      horaFin: this.horario.horaFin.length === 5 ? this.horario.horaFin + ':00' : this.horario.horaFin
    };

    const request$ = this.isEditMode
      ? this.adminService.actualizarHorario(this.horario.idHorario, payload)
      : this.adminService.crearHorario(payload);

    request$.subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: 'Éxito',
          text: this.isEditMode ? 'Horario actualizado correctamente' : 'Horario creado correctamente',
          confirmButtonColor: '#0B4D6C'
        }).then(() => {
          this.router.navigate(['/admin/horarios']);
        });
      },
      error: (err) => {
        this.isLoading = false;
        this.cdr.detectChanges();

        let msg = 'Hubo un problema al guardar';
        if (err.error?.message) msg = err.error.message;

        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: msg,
          confirmButtonColor: '#d33'
        });
      }
    });
  }
}