import { Component, ElementRef, ViewChild, inject, AfterViewChecked, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AsistenteService } from '../../../../core/services/asistente.service';
import { ChatRequest } from '../../../../core/models/asistente.interface';
import { AuthService } from '../../../../core/services/auth.service';

interface Message {
  text: string;
  isUser: boolean; 
  timestamp: Date;
}

@Component({
  selector: 'app-asistente',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './asistente.component.html',
  styles: [`
    .no-scrollbar::-webkit-scrollbar { display: none; }
    .no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
  `]
})
export class AsistenteComponent implements OnInit, AfterViewChecked {
  private asistenteService = inject(AsistenteService);
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef); // Inyectado para forzar la vista

  @ViewChild('scrollContainer') private scrollContainer!: ElementRef;

  messages: Message[] = [];
  userMessage: string = '';
  isTyping: boolean = false;
  userId: number = 0;

  ngOnInit() {
    const usuario = this.authService.currentUser();
    if (usuario) {
      this.userId = usuario.idUsuario;
      
      this.messages.push({
        text: `¡Hola ${usuario.nombres}! Soy Socrates, tu asistente académico. ¿En qué puedo ayudarte hoy?`,
        isUser: false,
        timestamp: new Date()
      });
      // Forzar actualización inicial
      this.cdr.detectChanges();
    }
  }

  ngAfterViewChecked() {
    this.scrollToBottom();
  }

  sendMessage() {
    if (!this.userMessage.trim()) return;

    // 1. Agregar mensaje del usuario y mostrar "Escribiendo..."
    const textoUsuario = this.userMessage;
    this.messages.push({ text: textoUsuario, isUser: true, timestamp: new Date() });
    this.userMessage = '';
    this.isTyping = true;
    
    // ACTUALIZAR VISTA: Muestra el mensaje del usuario inmediatamente
    this.cdr.detectChanges(); 

    // 2. Preparar request
    const request: ChatRequest = {
      mensaje: textoUsuario,
      userId: this.userId
    };

    // 3. Llamar al backend
    this.asistenteService.enviarMensaje(request).subscribe({
      next: (resp) => {
        this.isTyping = false;
        
        if (resp.error) {
          this.messages.push({ text: "Lo siento, hubo un error técnico.", isUser: false, timestamp: new Date() });
        } else {
          this.messages.push({ text: resp.respuesta, isUser: false, timestamp: new Date() });
        }

        // ACTUALIZAR VISTA: Muestra la respuesta del bot
        this.cdr.detectChanges(); 
      },
      error: (err) => {
        console.error(err);
        this.isTyping = false;
        this.messages.push({ text: "No pude conectar con el servidor.", isUser: false, timestamp: new Date() });
        
        // ACTUALIZAR VISTA: Muestra el error
        this.cdr.detectChanges();
      }
    });
  }

  private scrollToBottom(): void {
    try {
      this.scrollContainer.nativeElement.scrollTop = this.scrollContainer.nativeElement.scrollHeight;
    } catch(err) { }
  }
}