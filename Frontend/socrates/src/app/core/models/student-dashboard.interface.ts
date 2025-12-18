export interface DashboardData {
    cursos: {
        siglas: string;
        nombre: string;
        modalidad: string;
        promedio: number;
    }[];
    proximasClases: {
        horario: string;
        curso: string;
        aula: string;
        esVirtual: boolean;
    }[];
    estadoCuenta: {
        concepto: string;
        vencimiento: string;
        monto: number;
        tieneDeuda: boolean;
    };
}