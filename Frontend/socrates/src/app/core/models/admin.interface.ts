export interface ChartData {
    label: string;
    value: number;
}

export interface AuditoriaResumen {
    usuario: string;
    accion: string;
    tabla: string;
    fecha: string;
    ip: string;
}

export interface AdminDashboard {
    totalEstudiantes: number;
    docentesActivos: number;
    incidenciasIA: number;
    auditoriaHoy: number;
    estudiantesPorCarrera: ChartData[];
    distribucionUsuarios: ChartData[];
    ultimosMovimientos: AuditoriaResumen[];
}
export interface Carrera {
    idCarrera?: number;
    nombreCarrera: string;
    descripcion: string;
}

export interface Ciclo {
    idCiclo?: number;
    nombreCiclo: string;
}
export interface Aula {
    idAula?: number;
    descripcion: string;
    activo: boolean;
    aforoActual: number;
    aforoMaximo: number;
}
export interface Horario {
    idHorario?: number;
    diaSemana: string;
    horaInicio: string;
    horaFin: string;
    nombreCurso: string;
    nombreAula: string;
    activo: boolean;
}

export interface HorarioCreate {
    diaSemana: string;
    horaInicio: string;
    horaFin: string;
    idCurso: number;
    idAula: number;
}

export interface CursoSimple {
    idCurso: number;
    nombreCurso: string;
}

export interface EstadoPago {
    idEstadoPago: number;
    descripcion: string;
}

export interface Pago {
    idPago: number;
    concepto: string;
    monto: number;
    fechaVencimiento: string;
    fechaPago?: string;
    estadoPago: EstadoPago;
    matricula?: {
        alumno?: {
            nombres: string;
            apellidos: string;
            dni: string;
        }
    };
}

export interface UsuarioSimple {
    username: string;
}

export interface Auditoria {
    idAuditoria: number;
    nombreUsuario: string;
    rolUsuario: string;
    accion: string;
    tablaAfectada: string;
    detalleAnterior?: string;
    detalleNuevo?: string;
    fecha: string;
    ipOrigen: string;
}
export interface Usuario {
    idUsuario: number;
    username: string;
    nombres: string;
    apellidos: string;
    email: string;
    dni: string;           
    rol: string;           
    activo: boolean;       
}
export interface CursoDTO {
    idCurso: number;
    nombreCurso: string;
    creditos: number;
    cupoMaximo: number;
    cupoActual: number;
    nombreCarrera?: string;
    nombreCiclo?: string;
    nombreProfesor?: string;
    activo: boolean;
    idCarrera?: number;
    idCiclo?: number;
    idProfesor?: number;
}

export interface CursoCreateDTO {
    nombreCurso: string;
    creditos: number;
    cupoMaximo: number;
    cupoActual: number;
    idCarrera: number;
    idCiclo: number;
    idProfesor: number;
}