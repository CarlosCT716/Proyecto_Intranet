import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component/login.component';
import { MainLayoutComponent } from './layout/main-layout/main-layout-component';
import { RegisterComponent } from './features/auth/register/register.component/register.component';

export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    {
        path: '',
        component: MainLayoutComponent,
        children: [
            {
                path: 'perfil',
                loadComponent: () => import('./layout/profile/profile.component/profile.component').then(m => m.ProfileComponent)
            },
            {
                path: 'estudiante/inicio',
                loadComponent: () => import('./features/student/dashboard/dashboard.component/dashboard.component')
                    .then(m => m.DashboardComponent)
            },
            {
                path: 'estudiante/cursos',
                loadComponent: () => import('./features/student/cursos/cursos.component/cursos.component').then(m => m.CursosComponent)
            },
            {
                path: 'estudiante/cursos/:id',
                loadComponent: () => import('./features/student/cursos/detalle/detalle-curso.component/detalle-curso.component').then(m => m.DetalleCursoComponent)
            },
            {
                path: 'estudiante/horario',
                loadComponent: () => import('./features/student/horario/horario.component/horario.component').then(m => m.HorarioComponent)
            },
            {
                path: 'estudiante/pagos',
                loadComponent: () => import('./features/student/pago/pago.component/pago.component').then(m => m.PagoComponent)
            },
            {
                path: 'estudiante/asistente',
                loadComponent: () => import('./features/student/asistente/asistente.component/asistente.component').then(m => m.AsistenteComponent)
            },
            {
                path: 'docente/inicio',
                loadComponent: () => import('./features/teacher/dashboard/dashboard.component/dashboard.component')
                    .then(m => m.DashboardComponent)
            },
            {
                path: 'docente/cursos',
                loadComponent: () => import('./features/teacher/curso/curso.component/curso.component').then(m => m.CursoComponent)
            },
            {
                path: 'docente/cursos/:id/notas',
                loadComponent: () => import('./features/teacher/curso/notas/notas.component/notas.component').then(m => m.NotasComponent)
            },
            {
                path: 'docente/cursos/:id/sesiones',
                loadComponent: () => import('./features/teacher/curso/sesion/sesion.component/sesion.component').then(m => m.SesionesComponent)
            },
            {
                path: 'docente/sesiones/:idSesion/asistencia',
                loadComponent: () => import('./features/teacher/curso/sesion/asistencia/asistencia.component/asistencia.component').then(m => m.AsistenciaComponent)
            },
            {
                path: 'docente/agenda',
                loadComponent: () => import('./features/teacher/agenda/agenda.component/agenda.component')
                    .then(m => m.AgendaComponent)
            },
        ]
    },
    {
        path: 'admin',
        component: MainLayoutComponent,
        children: [
            { path: 'dashboard', loadComponent: () => import('./features/admin/dashboard/dashboard.component/dashboard.component').then(m => m.DashboardComponent) },
            { path: 'carreras', loadComponent: () => import('./features/admin/carreras/carreras.component/carreras.component').then(m => m.CarrerasComponent) },
            { path: 'carreras/nuevo', loadComponent: () => import('./features/admin/carreras/carrera-create.component/carrera-create.component').then(m => m.CarreraCreateComponent) },
            { path: 'ciclos', loadComponent: () => import('./features/admin/ciclos/ciclos.component/ciclos.component').then(m => m.CiclosComponent) },
            { path: 'ciclos/nuevo', loadComponent: () => import('./features/admin/ciclos/ciclo-create.component/ciclo-create.component').then(m => m.CicloCreateComponent) },
            { path: 'aulas', loadComponent: () => import('./features/admin/aulas/aulas.component/aulas.component').then(m => m.AulasComponent) },
            { path: 'aulas/nuevo', loadComponent: () => import('./features/admin/aulas/aulas-create.component/aulas-create.component').then(m => m.AulasCreateComponent) },
            { path: 'horarios', loadComponent: () => import('./features/admin/horarios/horarios.component/horarios.component').then(m => m.HorariosComponent) },
            { path: 'horarios/nuevo', loadComponent: () => import('./features/admin/horarios/horario-create.component/horario-create.component').then(m => m.HorarioCreateComponent) },
            { path: 'usuarios', loadComponent: () => import('./features/admin/usuarios/usuarios.component/usuarios.component').then(m => m.UsuariosComponent) },
            { path: 'pagos', loadComponent: () => import('./features/admin/pagos/pagos.component/pagos.component').then(m => m.PagosComponent) },
            { path: 'auditoria', loadComponent: () => import('./features/admin/auditoria/auditoria.component/auditoria.component').then(m => m.AuditoriaComponent) }
        ]
    },
    { path: '**', redirectTo: 'login' }
];