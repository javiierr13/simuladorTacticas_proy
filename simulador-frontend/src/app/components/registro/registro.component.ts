import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { RegistroRequest } from '../../models/entrenador.model';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './registro.component.html',
  styleUrl: './registro.component.css'
})
export class RegistroComponent {
  registroData: RegistroRequest = { nombre: '', correo: '', contrasena: '' };
  error: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  registrar() {
    this.error = '';
    this.authService.registro(this.registroData).subscribe({
      next: (response) => {
        this.authService.setCurrentUser(response);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.error = err.error?.mensaje || 'Error al registrar';
      }
    });
  }
}
