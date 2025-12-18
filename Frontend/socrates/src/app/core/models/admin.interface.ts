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