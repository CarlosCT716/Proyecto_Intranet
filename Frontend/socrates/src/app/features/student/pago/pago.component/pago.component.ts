import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { delay } from 'rxjs/operators';
import { PagosService } from '../../../../core/services/pago.service';
import { AuthService } from '../../../../core/services/auth.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';

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

    // Cargar pendientes
    this.pagosService.getPendientes(this.userId).pipe(delay(500)).subscribe({
      next: (data) => {
        this.pendientes = data;
        this.cdr.detectChanges();
      }
    });

    // Cargar historial
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
    if(!confirm('¿Confirmar pago de esta cuota?')) return;

    this.pagosService.realizarPago(idPago).subscribe({
      next: () => {
        alert('Pago realizado con éxito');
        this.cargarDatos(); // Recargar tablas
      },
      error: (err) => alert('Error al procesar el pago')
    });
  }
}