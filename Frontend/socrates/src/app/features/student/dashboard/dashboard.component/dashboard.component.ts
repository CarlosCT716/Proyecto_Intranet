import { Component, inject, OnInit, ChangeDetectorRef} from '@angular/core';
import { delay } from 'rxjs/operators';
import { CommonModule } from '@angular/common';
import { AcademicoService } from '../../../../core/services/academico.service';
import { AuthService } from '../../../../core/services/auth.service';
import { DashboardData } from '../../../../core/models/student-dashboard.interface';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';

@Component({
  selector: 'app-student-dashboard',
  standalone: true,
  imports: [CommonModule,LoadingSpinnerComponent],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  private academicoService = inject(AcademicoService);
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  isLoading: boolean = true;

  data: DashboardData = {
    cursos: [],
    proximasClases: [],
    estadoCuenta: { concepto: '', vencimiento: '', monto: 0, tieneDeuda: false }
  };

  ngOnInit() {
    const usuario = this.authService.currentUser();
    this.isLoading = true;
    console.log('Usuario logueado:', usuario); // 1. Ver si hay usuario

    if (usuario && usuario.idUsuario) {
 this.academicoService.getDashboardAlumno(usuario.idUsuario)
        .pipe(
          delay(2000) 
        )
        .subscribe({
        next: (resp) => {
          console.log('🔥 DATA RECIBIDA DEL BACKEND:', resp); // 2. Ver la respuesta cruda
          this.data = resp;
          this.isLoading = false;
          this.cdr.markForCheck();
          
          // 3. Verificaciones específicas
          if (!this.data.cursos) console.warn('⚠️ Cursos llegó NULL');
          if (!this.data.estadoCuenta) console.warn('⚠️ EstadoCuenta llegó NULL');
        },
        error: (err) =>{console.error('❌ Error cargando dashboard', err);
          this.isLoading = false;
        } 
      });
    }
  }
}