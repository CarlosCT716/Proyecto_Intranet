import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormArray } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AcademicoService } from '../../../../core/services/academico.service';
import { Carrera, Ciclo, Curso } from '../../../../core/models/register.interface';
import { switchMap } from 'rxjs/operators';
import { AuthService } from '../../../../core/services/auth.service';
import Swal from 'sweetalert2';

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
  private cdr = inject(ChangeDetectorRef);

  currentStep: number = 1;
  showPassword = false;
  readonly ID_ROL_ALUMNO = 3;
  readonly PERIODO_ACTUAL = "2025-1";
  readonly ID_CICLO_INICIAL = 1;
  readonly COSTO_POR_CREDITO = 75.00;
  readonly NUMERO_CUOTAS = 6;

  carreras: Carrera[] = [];
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
    this.academicoForm.patchValue({
        ciclo: this.ID_CICLO_INICIAL
    });
    this.academicoForm.get('carrera')?.valueChanges.subscribe(() => this.buscarCursosEnBackend());
  }

  cargarDatosMaestros() {
    this.academicoService.listarCarreras().subscribe((data: Carrera[]) => {
        this.carreras = data;
    });
  }

  buscarCursosEnBackend() {
    const carreraId = Number(this.academicoForm.get('carrera')?.value);
    const cicloId = this.ID_CICLO_INICIAL;

    if (carreraId > 0 && cicloId > 0) {
      this.cursosFormArray.clear();
      this.cursosFiltrados = [];
      this.calcularCostos(); 

      this.academicoService.getCursosPorFiltro(carreraId, cicloId).subscribe({
        next: (data: Curso[]) => {
          this.cursosFiltrados = data.filter(c => c.activo === true);
          this.cdr.detectChanges();
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
        Swal.fire({
          icon: 'warning',
          title: 'Datos Incompletos',
          text: 'Por favor complete todos los campos personales correctamente.',
          confirmButtonColor: '#0B4D6C'
        });
        return;
      }
    } else if (this.currentStep === 2) {
      if (this.academicoForm.invalid || this.cursosFormArray.length === 0) {
        this.academicoForm.markAllAsTouched();
        
        if (this.cursosFormArray.length === 0) {
            Swal.fire({
                icon: 'warning',
                title: 'Atención',
                text: 'Debe seleccionar al menos un curso para continuar.',
                confirmButtonColor: '#0B4D6C'
            });
        }
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

    Swal.fire({
        title: 'Procesando Registro',
        text: 'Generando usuario y matrícula...',
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });

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
          idCiclo: this.ID_CICLO_INICIAL,
          periodo: this.PERIODO_ACTUAL,
          idCursos: formValue.seleccionAcademica?.cursos 
        };
        return this.academicoService.crearMatricula(matriculaPayload);
      })
    ).subscribe({
      next: (matriculaResponse) => {
        Swal.fire({
            icon: 'success',
            title: '¡Bienvenido!',
            text: 'Registro y matrícula completados con éxito. Ahora puedes iniciar sesión.',
            confirmButtonColor: '#0B4D6C',
            confirmButtonText: 'Ir al Login'
        }).then((result) => {
            if (result.isConfirmed) {
                this.router.navigate(['/login']);
            }
        });
      },
      error: (err) => {
        console.error("Error en registro:", err);
        
        let mensaje = 'Hubo un error interno. Intente nuevamente.';
        
        if (err.status === 409) {
             mensaje = 'El DNI o Usuario ya se encuentran registrados.';
        } else if (err.error && typeof err.error === 'string') {
             mensaje = err.error;
        } else if (err.error?.message) {
             if (err.error.message.includes('vacantes')) {
                 mensaje = 'Lo sentimos, uno de los cursos seleccionados ya no tiene vacantes disponibles.';
             } else {
                 mensaje = err.error.message;
             }
        }

        Swal.fire({
            icon: 'error',
            title: 'Error de Registro',
            text: mensaje,
            confirmButtonColor: '#d33'
        });
      }
    });
  }
}