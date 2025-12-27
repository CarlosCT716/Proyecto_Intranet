import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { delay } from 'rxjs/operators';
import { PagosService } from '../../../../core/services/pago.service';
import { AuthService } from '../../../../core/services/auth.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-pagos',
  standalone: true,
  imports: [CommonModule, LoadingSpinnerComponent],
  templateUrl: './pago.component.html'
})
export class PagoComponent implements OnInit {
  private pagosService = inject(PagosService);
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  isLoading = true;
  pendientes: any[] = [];
  historial: any[] = [];
  userId: number = 0;

  ngOnInit() {
    const usuario = this.authService.currentUser();
    if (usuario) {
      this.userId = usuario.idUsuario;
      this.cargarDatos();
    }
  }

  cargarDatos() {
    this.isLoading = true;
    this.cdr.detectChanges();

    this.pagosService.getPendientes(this.userId).pipe(delay(500)).subscribe({
      next: (data) => {
        this.pendientes = data;
        this.cdr.detectChanges();
      }
    });

    this.pagosService.getHistorial(this.userId).pipe(delay(800)).subscribe({
      next: (data) => {
        this.historial = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => this.isLoading = false
    });
  }

  pagar(idPago: number) {
    Swal.fire({
      title: '¿Confirmar pago?',
      text: "¿Estás seguro de pagar esta cuota ahora?",
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#0B4D6C',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, pagar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        
        Swal.fire({
            title: 'Procesando pago',
            text: 'Por favor espere...',
            allowOutsideClick: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });

        this.pagosService.realizarPago(idPago).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Pago realizado',
              text: 'La cuota ha sido pagada con éxito.',
              confirmButtonColor: '#0B4D6C'
            });
            this.cargarDatos(); 
          },
          error: (err) => {
            console.error(err);
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'Hubo un problema al procesar el pago.',
              confirmButtonColor: '#d33'
            });
          }
        });
      }
    });
  }
}