export interface Carrera {
  idCarrera: number; 
  nombreCarrera: string;
}

export interface Ciclo {
  idCiclo: number;
  nombreCiclo: string;
}

export interface Curso {
  idCurso: number;
  nombreCurso: string;
  creditos: number;
  idCarrera: number; 
  idCiclo: number;  
  activo: boolean;
}

export interface RegistroRequest {
  usuario: {
    nombres: string;
    apellidos: string;
    dni: string;
    email: string;
    username: string;
    password: string;
  };
  matricula: {
    idCarrera: number;
    ciclo: number;
    cursosIds: number[];
    montoTotal: number;
  };
}