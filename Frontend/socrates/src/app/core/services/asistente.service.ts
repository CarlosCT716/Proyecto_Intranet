import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ChatRequest, ChatResponse } from '../models/asistente.interface';

@Injectable({
  providedIn: 'root'
})
export class AsistenteService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:5050/api/asistente';

  enviarMensaje(request: ChatRequest): Observable<ChatResponse> {
    return this.http.post<ChatResponse>(`${this.apiUrl}/chat`, request);
  }
}