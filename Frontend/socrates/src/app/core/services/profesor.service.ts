import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProfesorService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:5050/api/profesor';

  getDashboard(idProfesor: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/dashboard/${idProfesor}`);
  }

  getAgenda(idProfesor: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/agenda/${idProfesor}`);
  }

  listarCursosAsignados(idProfesor: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/cursos/asignados/${idProfesor}`);
  }

  listarNotasPorCurso(idCurso: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/curso/notas/${idCurso}`);
  }

  guardarNotasMasivo(notas: any[]): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/notas/masivo`, notas);
  }

  listarSesionesPorCurso(idCurso: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/curso/sesiones/${idCurso}`);
  }

  obtenerDetalleAsistencia(idSesion: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/sesion/${idSesion}/asistencia`);
  }

  registrarAsistencia(idSesion: number, asistencias: any[]): Observable<any> {
    const payload = {
      idSesion: idSesion,
      asistencias: asistencias
    };
    return this.http.post<any>(`${this.apiUrl}/asistencia`, payload, { responseType: 'text' as 'json' });
  }

  finalizarSesion(idSesion: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/sesion/${idSesion}/finalizar`, {});
  }
}