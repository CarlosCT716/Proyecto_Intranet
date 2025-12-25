import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AdminDashboard, Ciclo, Carrera, Aula, Horario, HorarioCreate, Pago, Auditoria } from '../../core/models/admin.interface';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private http = inject(HttpClient);

  private apiBase = 'http://localhost:5050/api';

  getDashboard(): Observable<AdminDashboard> {
    return this.http.get<AdminDashboard>(`${this.apiBase}/admin/dashboard`);
  }

  listarCarreras(): Observable<Carrera[]> {
    return this.http.get<Carrera[]>(`${this.apiBase}/academico/carreras`);
  }

  crearCarrera(carrera: Carrera): Observable<Carrera> {
    return this.http.post<Carrera>(`${this.apiBase}/academico/carreras`, carrera);
  }

  listarCiclos(): Observable<Ciclo[]> {
    return this.http.get<Ciclo[]>(`${this.apiBase}/academico/ciclos`);
  }

  crearCiclo(ciclo: Ciclo): Observable<Ciclo> {
    return this.http.post<Ciclo>(`${this.apiBase}/academico/ciclos`, ciclo);
  }

  listarAulas(): Observable<Aula[]> {
    return this.http.get<Aula[]>(`${this.apiBase}/academico/aulas`);
  }

  crearAula(aula: Aula): Observable<Aula> {
    return this.http.post<Aula>(`${this.apiBase}/academico/aulas`, aula);
  }

  cambiarEstadoAula(idAula: number): Observable<void> {
    return this.http.patch<void>(`${this.apiBase}/academico/aulas/estado/${idAula}`, {});
  }

  listarCursos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiBase}/academico/cursos`);
  }

  listarHorarios(): Observable<Horario[]> {
    return this.http.get<Horario[]>(`${this.apiBase}/academico/horarios`);
  }

  crearHorario(horario: HorarioCreate): Observable<any> {
    return this.http.post<any>(`${this.apiBase}/academico/horarios`, horario);
  }

  cambiarEstadoHorario(idHorario: number): Observable<void> {
    return this.http.patch<void>(`${this.apiBase}/academico/horarios/estado/${idHorario}`, {});
  }

  listarPagos(): Observable<Pago[]> {
    return this.http.get<Pago[]>(`${this.apiBase}/pagos`);
  }

  listarAuditoria(): Observable<Auditoria[]> {
    return this.http.get<Auditoria[]>(`${this.apiBase}/auditoria`);
  }
}