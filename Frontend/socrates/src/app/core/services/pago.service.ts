import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PagosService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:5050/api/pagos';

  getHistorial(idAlumno: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/alumno/${idAlumno}/historial`);
  }

  getPendientes(idAlumno: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/alumno/${idAlumno}/pendientes`);
  }

  realizarPago(idPago: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${idPago}/pagar`, {});
  }
}