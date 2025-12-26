import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { AdminService } from '../../../../core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { forkJoin, delay } from 'rxjs';

@Component({
  selector: 'app-horario-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, LoadingSpinnerComponent],
  templateUrl: './horario-form.component.html'
})
export class HorarioFormComponent implements OnInit {
  private adminService = inject(AdminService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private cdr = inject(ChangeDetectorRef);

  isLoading = true;
  isEditMode = false;
  titulo = 'Nuevo Horario';

  cursos: any[] = [];
  aulas: any[] = [];
  diasSemana = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO'];

  horario: any = {
    diaSemana: 'LUNES',
    horaInicio: '',
    horaFin: '',
    idCurso: null,
    idAula: null
  };

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    this.isEditMode = !!id;
    this.titulo = id ? 'Editar Horario' : 'Nuevo Horario';

    const obs: any = {
        cursos: this.adminService.listarCursos(),
        aulas: this.adminService.listarAulas()
    };

    if (id) obs.data = this.adminService.obtenerHorario(id);

    forkJoin(obs)
      .pipe(delay(2000))
      .subscribe({
        next: (res: any) => {
            this.cursos = res.cursos;
            this.aulas = res.aulas.filter((a:any) => a.activo);
            
            if (id && res.data) {
                this.horario = res.data;
                if (res.data.curso) this.horario.idCurso = res.data.curso.idCurso;
                if (res.data.aula) this.horario.idAula = res.data.aula.idAula;
            }
            this.isLoading = false;
            this.cdr.detectChanges();
        },
        error: () => {
            this.isLoading = false;
            this.router.navigate(['/admin/horarios']);
            this.cdr.detectChanges();
        }
    });
  }

  guardar() {
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
            alert(this.isEditMode ? 'Horario actualizado' : 'Horario creado'); 
            this.router.navigate(['/admin/horarios']); 
        },
        error: () => { 
            this.isLoading = false; 
            alert('Error al guardar');
            this.cdr.detectChanges();
        }
    });
  }
}