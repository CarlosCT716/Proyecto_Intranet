import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loading-spinner',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="flex flex-col items-center justify-center w-full min-h-[50vh] p-8 fade-in">
      
      <div class="relative">
        <i class="fa-solid fa-circle-notch fa-spin text-5xl text-[#0B4D6C]"></i>
        
        </div>

      <p class="mt-4 text-[#002940] font-semibold text-lg animate-pulse tracking-wide">
        {{ message }}
      </p>
    </div>
  `,
  styles: [`
    .fade-in { animation: fadeIn 0.3s ease-in; }
    @keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
  `]
})
export class LoadingSpinnerComponent {
  @Input() message: string = 'Cargando información...';
}