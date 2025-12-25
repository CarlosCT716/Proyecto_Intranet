import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AdminService } from '../../../../core/services/admin.service';
import { Aula } from '../../../../core/models/admin.interface';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { forkJoin, delay } from 'rxjs';

@Component({
  selector: 'app-horario-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, LoadingSpinnerComponent],
  templateUrl: './horario-create.component.html'
})
export class HorarioCreateComponent implements OnInit {
  private fb = inject(FormBuilder);
  private adminService = inject(AdminService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  isSubmitting = false;
  isLoading = true; 
  cursos: any[] = [];
  aulas: Aula[] = [];
  
  diasSemana = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO'];

  form = this.fb.group({
    diaSemana: ['', [Validators.required]],
    horaInicio: ['', [Validators.required]],
    horaFin: ['', [Validators.required]],
    idCurso: ['', [Validators.required]],
    idAula: ['', [Validators.required]]
  });

  ngOnInit() {
    this.cargarDatosMaestros();
  }

  cargarDatosMaestros() {
    this.isLoading = true;
    
    forkJoin({
      cursos: this.adminService.listarCursos(),
      aulas: this.adminService.listarAulas()
    }).pipe(
      delay(2000) 
    ).subscribe({
      next: (res) => {
        this.cursos = res.cursos;
        this.aulas = res.aulas.filter(a => a.activo);
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        alert('Error al cargar datos del sistema');
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  onSubmit() {
    if (this.form.invalid) return;
    const hInicio = this.form.get('horaInicio')?.value;
    const hFin = this.form.get('horaFin')?.value;
    
    if (hInicio && hFin && hInicio >= hFin) {
        alert('La hora de inicio debe ser menor a la hora de fin.');
        return;
    }

    this.isSubmitting = true;
    
    const payload = {
        diaSemana: this.form.get('diaSemana')?.value,
        horaInicio: this.form.get('horaInicio')?.value + ':00', 
        horaFin: this.form.get('horaFin')?.value + ':00',
        idCurso: Number(this.form.get('idCurso')?.value),
        idAula: Number(this.form.get('idAula')?.value)
    };

    this.adminService.crearHorario(payload as any).subscribe({
      next: () => {
        alert('Horario registrado exitosamente');
        this.router.navigate(['/admin/horarios']);
      },
      error: () => {
        alert('Error al registrar horario. Verifique que no haya cruces.');
        this.isSubmitting = false;
        this.cdr.detectChanges();
      }
    });
  }
}