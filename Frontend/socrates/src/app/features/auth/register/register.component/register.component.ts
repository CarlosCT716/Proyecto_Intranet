import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormArray } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AcademicoService } from '../../../../core/services/academico.service';
import { Carrera, Ciclo, Curso } from '../../../../core/models/register.interface';
import { switchMap } from 'rxjs/operators';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private academicoService = inject(AcademicoService);
  private authService = inject(AuthService);

  currentStep: number = 1;
  showPassword = false;
  readonly ID_ROL_ALUMNO = 3;
  readonly PERIODO_ACTUAL = "2025-1";
  readonly COSTO_POR_CREDITO = 75.00;
  readonly NUMERO_CUOTAS = 6;

  carreras: Carrera[] = [];
  ciclos: Ciclo[] = [];
  cursosFiltrados: Curso[] = [];

  montoInscripcion: number = 300.00;
  montoMensualidad: number = 0.00;
  montoTotal: number = 0.00;
  totalCreditos: number = 0;

  registerForm = this.fb.group({
    datosPersonales: this.fb.group({
      nombres: ['', Validators.required],
      apellidos: ['', Validators.required],
      dni: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
      email: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
      direccion: ['', Validators.required],
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]]
    }),
    seleccionAcademica: this.fb.group({
      carrera: ['', Validators.required],
      ciclo: ['', Validators.required],
      cursos: this.fb.array([])
    })
  });

  get personalForm() { return this.registerForm.get('datosPersonales') as FormGroup; }
  get academicoForm() { return this.registerForm.get('seleccionAcademica') as FormGroup; }
  get cursosFormArray() { return this.academicoForm.get('cursos') as FormArray; }

  ngOnInit() {
    this.cargarDatosMaestros();
    this.academicoForm.get('carrera')?.valueChanges.subscribe(() => this.buscarCursosEnBackend());
    this.academicoForm.get('ciclo')?.valueChanges.subscribe(() => this.buscarCursosEnBackend());
  }

  cargarDatosMaestros() {
    this.academicoService.getCarreras().subscribe(data => this.carreras = data);
    this.academicoService.getCiclos().subscribe(data => this.ciclos = data);
  }

  buscarCursosEnBackend() {
    const carreraId = Number(this.academicoForm.get('carrera')?.value);
    const cicloId = Number(this.academicoForm.get('ciclo')?.value);

    if (carreraId > 0 && cicloId > 0) {
      this.cursosFormArray.clear();
      this.cursosFiltrados = [];
      this.calcularCostos(); 

      this.academicoService.getCursosPorFiltro(carreraId, cicloId).subscribe({
        next: (data) => {
          this.cursosFiltrados = data;
        },
        error: (err) => console.error('Error filtrando cursos', err)
      });
    }
  }

  onCursoChange(e: any, cursoId: number) {
    if (e.target.checked) {
      this.cursosFormArray.push(this.fb.control(cursoId));
    } else {
      const index = this.cursosFormArray.controls.findIndex(x => x.value === cursoId);
      if (index !== -1) {
        this.cursosFormArray.removeAt(index);
      }
    }
    this.calcularCostos();
  }

  calcularCostos() {
    const selectedIds = this.cursosFormArray.value as number[];
    const cursosSeleccionados = this.cursosFiltrados.filter(c => selectedIds.includes(c.idCurso));
    
    this.totalCreditos = cursosSeleccionados.reduce((sum, c) => sum + c.creditos, 0);
    
    this.montoTotal = this.totalCreditos * this.COSTO_POR_CREDITO;
    
    if (this.NUMERO_CUOTAS > 0) {
      this.montoMensualidad = this.montoTotal / this.NUMERO_CUOTAS;
    } else {
      this.montoMensualidad = 0;
    }
  }

  isCursoSelected(cursoId: number): boolean {
    return this.cursosFormArray.value.includes(cursoId);
  }

  nextStep() {
    if (this.currentStep === 1) {
      if (this.personalForm.invalid) {
        this.personalForm.markAllAsTouched();
        return;
      }
    } else if (this.currentStep === 2) {
      if (this.academicoForm.invalid || this.cursosFormArray.length === 0) {
        this.academicoForm.markAllAsTouched();
        if (this.cursosFormArray.length === 0) alert("Debe seleccionar al menos un curso.");
        return;
      }
    }
    this.currentStep++;
  }

  prevStep() {
    this.currentStep--;
  }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  onSubmit() {
    if (this.registerForm.invalid) return;

    const formValue = this.registerForm.value;
    const usuarioPayload = {
      username: formValue.datosPersonales?.username,
      password: formValue.datosPersonales?.password,
      nombres: formValue.datosPersonales?.nombres,
      apellidos: formValue.datosPersonales?.apellidos,
      email: formValue.datosPersonales?.email,
      dni: formValue.datosPersonales?.dni,
      telefono: formValue.datosPersonales?.telefono,
      direccion: formValue.datosPersonales?.direccion,
      idRol: this.ID_ROL_ALUMNO
    };

    this.authService.registrarUsuario(usuarioPayload).pipe(
      switchMap((usuarioCreadoResponse) => {
        const matriculaPayload = {
          idAlumno: usuarioCreadoResponse.idUsuario, 
          idCarrera: Number(formValue.seleccionAcademica?.carrera),
          idCiclo: Number(formValue.seleccionAcademica?.ciclo),
          periodo: this.PERIODO_ACTUAL,
          idCursos: formValue.seleccionAcademica?.cursos 
        };
        return this.academicoService.crearMatricula(matriculaPayload);
      })
    ).subscribe({
      next: (matriculaResponse) => {
        alert('¡Registro y Matrícula completados con éxito! Ahora puedes iniciar sesión.');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error(err);
        alert(err.error?.message || 'Hubo un error en el registro. Verifique vacantes o intente nuevamente.');
      }
    });
  }
}