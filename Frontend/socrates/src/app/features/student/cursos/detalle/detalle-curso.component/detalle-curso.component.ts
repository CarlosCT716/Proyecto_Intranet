import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { delay } from 'rxjs/operators';
import { AcademicoService } from '../../../../../core/services/academico.service'; 
import { AuthService } from '../../../../../core/services/auth.service'; 
import { LoadingSpinnerComponent } from '../../../../../shared/loading-spinner.component';

@Component({
  selector: 'app-detalle-curso',
  standalone: true,
  imports: [CommonModule, RouterLink, LoadingSpinnerComponent],
  templateUrl: './detalle-curso.component.html'
})
export class DetalleCursoComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private academicoService = inject(AcademicoService);
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  isLoading: boolean = true;
  data: any = null; 

  ngOnInit() {
    const idCurso = Number(this.route.snapshot.paramMap.get('id'));
    const usuario = this.authService.currentUser();
    
    // Iniciamos carga
    this.isLoading = true;
    this.cdr.detectChanges();

    if (idCurso && usuario) {
      this.academicoService.obtenerDetalleCurso(usuario.idUsuario, idCurso)
        .pipe(delay(500)) // Pequeño delay para suavizar la transición
        .subscribe({
          next: (resp) => {
              console.log("Detalle recibido:", resp);
              this.data = resp;
              this.isLoading = false;
              this.cdr.detectChanges(); // Forzamos actualización
          },
          error: (err) => {
              console.error('Error cargando detalle', err);
              this.isLoading = false;
              this.cdr.detectChanges();
          }
        });
    } else {
        this.isLoading = false;
        this.cdr.detectChanges();
    }
  }
}