import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { JugadorService } from '../../services/jugador.service';
import { Jugador } from '../../models/jugador.model';

@Component({
  selector: 'app-jugadores',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './jugadores.component.html',
  styleUrl: './jugadores.component.css'
})
export class JugadoresComponent implements OnInit {
  jugadores: Jugador[] = [];
  entrenadorId: number = 0;
  mostrarFormulario: boolean = false;
  jugadorEditando: Jugador | null = null;
  jugadorForm: Jugador = {
    nombre: '',
    dorsal: 0,
    posicion: '',
    piernaDominante: 'AMBAS'
  };

  constructor(
    private authService: AuthService,
    private jugadorService: JugadorService,
    private router: Router
  ) {}

  ngOnInit() {
    const user = this.authService.getCurrentUser();
    if (!user) {
      this.router.navigate(['/login']);
      return;
    }
    this.entrenadorId = user.id;
    this.cargarJugadores();
  }

  cargarJugadores() {
    this.jugadorService.obtenerJugadores(this.entrenadorId).subscribe({
      next: (data) => {
        this.jugadores = data;
      }
    });
  }

  nuevoJugador() {
    this.jugadorEditando = null;
    this.jugadorForm = {
      nombre: '',
      dorsal: 0,
      posicion: '',
      piernaDominante: 'AMBAS'
    };
    this.mostrarFormulario = true;
  }

  editarJugador(jugador: Jugador) {
    this.jugadorEditando = jugador;
    this.jugadorForm = { ...jugador };
    this.mostrarFormulario = true;
  }

  guardarJugador() {
    if (this.jugadorEditando) {
      this.jugadorService.actualizarJugador(this.jugadorEditando.id!, this.jugadorForm).subscribe({
        next: () => {
          this.cargarJugadores();
          this.cancelarFormulario();
        }
      });
    } else {
      this.jugadorService.crearJugador(this.entrenadorId, this.jugadorForm).subscribe({
        next: () => {
          this.cargarJugadores();
          this.cancelarFormulario();
        }
      });
    }
  }

  eliminarJugador(id: number) {
    if (confirm('¿Estás seguro de eliminar este jugador?')) {
      this.jugadorService.eliminarJugador(id).subscribe({
        next: () => {
          this.cargarJugadores();
        }
      });
    }
  }

  cancelarFormulario() {
    this.mostrarFormulario = false;
    this.jugadorEditando = null;
  }

  volver() {
    this.router.navigate(['/dashboard']);
  }
}
