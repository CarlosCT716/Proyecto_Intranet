import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AdminDashboard, Ciclo, Carrera, Aula, Horario, HorarioCreate, Pago, Auditoria,Usuario } from '../../core/models/admin.interface';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private http = inject(HttpClient);

  private apiBase = 'http://localhost:5050/api';

  getDashboard(): Observable<AdminDashboard> {
    return this.http.get<AdminDashboard>(`${this.apiBase}/admin/dashboard`);
  }

 // --- CARRERAS ---
  listarCarreras(): Observable<Carrera[]> {
    return this.http.get<Carrera[]>(`${this.apiBase}/academico/carreras`);
  }
  obtenerCarrera(id: number): Observable<Carrera> {
    return this.http.get<Carrera>(`${this.apiBase}/academico/carreras/${id}`);
  }
  crearCarrera(carrera: Carrera): Observable<Carrera> {
    return this.http.post<Carrera>(`${this.apiBase}/academico/carreras`, carrera);
  }
  actualizarCarrera(id: number, carrera: Carrera): Observable<Carrera> {
    return this.http.put<Carrera>(`${this.apiBase}/academico/carreras/${id}`, carrera);
  }

  listarCiclos(): Observable<Ciclo[]> {
    return this.http.get<Ciclo[]>(`${this.apiBase}/academico/ciclos`);
  }

  crearCiclo(ciclo: Ciclo): Observable<Ciclo> {
    return this.http.post<Ciclo>(`${this.apiBase}/academico/ciclos`, ciclo);
  }

 // --- AULAS ---
  listarAulas(): Observable<Aula[]> {
    return this.http.get<Aula[]>(`${this.apiBase}/academico/aulas`);
  }
  obtenerAula(id: number): Observable<Aula> {
    return this.http.get<Aula>(`${this.apiBase}/academico/aulas/${id}`);
  }
  crearAula(aula: Aula): Observable<Aula> {
    return this.http.post<Aula>(`${this.apiBase}/academico/aulas`, aula);
  }
  actualizarAula(id: number, aula: Aula): Observable<Aula> {
    return this.http.put<Aula>(`${this.apiBase}/academico/aulas/${id}`, aula);
  }
  cambiarEstadoAula(idAula: number): Observable<void> {
    return this.http.patch<void>(`${this.apiBase}/academico/aulas/estado/${idAula}`, {});
  }

 // --- CURSOS ---
  listarCursos(): Observable<any[]> { 
    return this.http.get<any[]>(`${this.apiBase}/academico/cursos`);
  }
  obtenerCurso(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiBase}/academico/cursos/${id}`);
  }
  crearCurso(curso: any): Observable<any> {
    return this.http.post<any>(`${this.apiBase}/academico/cursos`, curso);
  }
  actualizarCurso(id: number, curso: any): Observable<any> {
    return this.http.put<any>(`${this.apiBase}/academico/cursos/${id}`, curso);
  }
  cambiarEstadoCurso(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiBase}/academico/cursos/estado/${id}`, {});
  }

 // --- HORARIOS ---
  listarHorarios(): Observable<Horario[]> {
    return this.http.get<Horario[]>(`${this.apiBase}/academico/horarios`);
  }
  obtenerHorario(id: number): Observable<Horario> {
    return this.http.get<Horario>(`${this.apiBase}/academico/horarios/${id}`);
  }
  crearHorario(horario: HorarioCreate): Observable<any> {
    return this.http.post<any>(`${this.apiBase}/academico/horarios`, horario);
  }
  actualizarHorario(id: number, horario: HorarioCreate): Observable<any> {
    return this.http.put<any>(`${this.apiBase}/academico/horarios/${id}`, horario);
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
  
  listarUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.apiBase}/usuarios`);
  }

  cambiarEstadoUsuario(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiBase}/usuarios/estado/${id}`, {});
  }

 crearUsuario(usuario: any): Observable<any> {
  return this.http.post<any>(`${this.apiBase}/usuarios`, usuario); 
 }

 obtenerUsuario(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiBase}/usuarios/${id}`);
  }

  actualizarUsuario(id: number, usuario: any): Observable<any> {
    return this.http.put<any>(`${this.apiBase}/usuarios/${id}`, usuario);
  }
}