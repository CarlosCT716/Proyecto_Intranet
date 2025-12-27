import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { map } from 'rxjs/operators';
import { LoginRequest, LoginResponse } from '../models/auth.interface';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
    private http = inject(HttpClient);
    private router = inject(Router);

    private apiUrl = 'http://localhost:5050/api/auth';

    currentUser = signal<LoginResponse | null>(null);
    constructor() {
    const storedUser = localStorage.getItem('user_data');
    if (storedUser) {
      this.currentUser.set(JSON.parse(storedUser));
    }
  }

  registrarUsuario(usuario: any): Observable<any> {
    return this.http.post<any>('http://localhost:5050/api/usuarios', usuario);
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((response) => {
        // 1. Guardar Token y Datos en LocalStorage
        localStorage.setItem('token', response.token);
        localStorage.setItem('user_data', JSON.stringify(response));
        
        // 2. Actualizar el estado de la app
        this.currentUser.set(response);
      })
    );
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user_data');
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  getRole(): string | null {
    return this.currentUser()?.rol || null;
  }
  
  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }
  cambiarPassword(data: { usuario: string, nuevaContrasena: string }) {
  return this.http.post(`${this.apiUrl}/cambiar-contrasena`, data);
}
}
