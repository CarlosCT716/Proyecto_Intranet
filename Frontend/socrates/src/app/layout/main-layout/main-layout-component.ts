import { Component } from "@angular/core";
import { RouterOutlet } from "@angular/router";
import { SidebarComponent } from "../sidebar/sidebar.component/sidebar.component";
import { NavbarComponent } from "../navbar/navbar.component/navbar.component";
import { FooterComponent } from "../footer/footer.component/footer.component";

@Component({
    selector: 'app-main-layout',
  standalone: true, 
  imports: [
    RouterOutlet,     
    SidebarComponent,  
    NavbarComponent,   
    FooterComponent    
  ],
  templateUrl: './main-layout.component.html'
})
export class MainLayoutComponent {
    currentUserRole: string = 'STUDENT'; 
  currentPageTitle: string = 'Bienvenido';
}