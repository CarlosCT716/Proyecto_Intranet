import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Carrera, Ciclo, Curso } from '../models/register.interface';
import { DashboardData } from '../models/student-dashboard.interface';
@Injectable({
    providedIn: 'root'
})
export class AcademicoService {
    private http = inject(HttpClient);
    private apiUrl = 'http://localhost:5050/api/academico';

    getCarreras(): Observable<Carrera[]> {
        return this.http.get<Carrera[]>(`${this.apiUrl}/carreras`);
    }

    getCiclos(): Observable<Ciclo[]> {
        return this.http.get<Ciclo[]>(`${this.apiUrl}/ciclos`);
    }

    getCursos(): Observable<Curso[]> {
        return this.http.get<Curso[]>(`${this.apiUrl}/cursos`);
    }
    getCursosPorFiltro(idCarrera: number, idCiclo: number): Observable<Curso[]> {
        const params = new HttpParams()
            .set('idCarrera', idCarrera.toString())
            .set('idCiclo', idCiclo.toString());

        return this.http.get<Curso[]>(`${this.apiUrl}/cursos/filtro`, { params });
    }

    crearMatricula(matriculaPayload: any): Observable<any> {
        return this.http.post<any>(`${this.apiUrl.replace('/academico', '')}/matriculas`, matriculaPayload);
    }

    getDashboardAlumno(idAlumno: number): Observable<DashboardData> {
        return this.http.get<DashboardData>(`${this.apiUrl}/alumno/${idAlumno}/dashboard`);
    }
    obtenerDetalleCurso(idAlumno: number, idCurso: number): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}/alumno/${idAlumno}/curso/${idCurso}/detalle`);
    }
    listarCursosAlumno(idAlumno: number): Observable<any[]> {
        return this.http.get<any[]>(`${this.apiUrl}/alumno/${idAlumno}/cursos`);
    }
    obtenerHorarioAlumno(idAlumno: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/alumno/${idAlumno}/horario`);
}

}