export interface ChatRequest {
  mensaje: string;
  userId: number;
}

export interface ChatResponse {
  respuesta: string;
  error: string;
}